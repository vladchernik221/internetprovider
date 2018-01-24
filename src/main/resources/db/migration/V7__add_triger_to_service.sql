DELIMITER //

CREATE TRIGGER `insert_contract_annex_has_service`
  AFTER INSERT
  ON `contract_annex_has_service`
  FOR EACH ROW
  BEGIN
    UPDATE `account` a
      JOIN `service` s ON s.service_id = NEW.service_id
    SET a.balance = a.balance - s.price
    WHERE a.contract_annex_id = NEW.contract_annex_id;
  END //

DELIMITER ;