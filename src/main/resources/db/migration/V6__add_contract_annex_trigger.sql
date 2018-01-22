ALTER TABLE `account`
  CHANGE COLUMN `trafficked_trafic` `used_trafic` INT NULL DEFAULT 0;

DELIMITER //

CREATE TRIGGER `insert_contract_annex`
  AFTER INSERT
  ON `contract_annex`
  FOR EACH ROW
  BEGIN
    INSERT INTO `account` (`contract_annex_id`) VALUES (NEW.contract_annex_id);
  END //

CREATE TRIGGER `insert_transaction`
  AFTER INSERT
  ON `transaction`
  FOR EACH ROW
  BEGIN
    IF (NEW.type = 'WRITE_OFF')
    THEN
      UPDATE `account` a
      SET a.balance = a.balance - NEW.amount
      WHERE a.account_id = NEW.account_id;
    END IF;
  END //

CREATE EVENT `monthly_write_off`
  ON SCHEDULE
    EVERY '1' MONTH
    STARTS
      DATE_SUB(
          LAST_DAY(
              DATE_ADD(NOW(), INTERVAL 1 MONTH)
          ),
          INTERVAL DAY(LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH))) - 1 DAY)
DO
  BEGIN
    DECLARE `cursor_end` BOOLEAN DEFAULT FALSE;
    DECLARE `account_id` INT;
    DECLARE `montly_fee` DECIMAL(15, 2);
    DECLARE `used_traffic` INT;
    DECLARE `included_traffic` INT;
    DECLARE `price_over_traffic` DECIMAL(15, 2);
    DECLARE `write_off_sumary` DECIMAL(15, 2);
    DECLARE `traffic_difference` INT;
    DECLARE `discount_sum` INT;
    DECLARE `contract_annex_cursor` CURSOR FOR SELECT
                                                 a.account_id,
                                                 tp.monthly_fee,
                                                 a.used_trafic,
                                                 tp.included_traffic,
                                                 tp.price_over_traffic,
                                                 (SELECT SUM(d.amount)
                                                  FROM `discount` d
                                                    JOIN `tariff_plan_has_discount` tphd
                                                      ON d.discount_id = tphd.discount_id
                                                    JOIN `tariff_plan` t ON tphd.tariff_plan_id = t.tariff_plan_id
                                                  WHERE tphd.tariff_plan_id = ca.tariff_plan_id) AS `discount_sum`
                                               FROM `account` a
                                                 JOIN `contract_annex` ca
                                                   ON a.contract_annex_id = ca.contract_annex_id AND ca.canceled = 0
                                                 JOIN tariff_plan tp ON ca.tariff_plan_id = tp.tariff_plan_id;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET `cursor_end` = TRUE;

    OPEN `contract_annex_cursor`;

    read_loop: LOOP
      FETCH `contract_annex_cursor`
      INTO `account_id`, `montly_fee`, `used_traffic`, `included_traffic`, `price_over_traffic`, `discount_sum`;
      IF `cursor_end`
      THEN
        LEAVE read_loop;
      END IF;

      SET `write_off_sumary` = `montly_fee`;
      SET `write_off_sumary` = `write_off_sumary` * `discount_sum` / 100;

      IF (`included_traffic` IS NOT NULL)
      THEN
        SET `traffic_difference` = `used_traffic` - `included_traffic`;
        IF (`traffic_difference` > 0)
        THEN
          SET `write_off_sumary` = `write_off_sumary` + `traffic_difference` * `price_over_traffic`;
        END IF;
      END IF;

      INSERT INTO `transaction` (`type`, `amount`, `date`, `account_id`)
      VALUES ('WRITE_OFF', `write_off_sumary`, NOW(), `account_id`);

    END LOOP;

    CLOSE `contract_annex_cursor`;
  END //

DELIMITER ;