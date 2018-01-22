ALTER TABLE `transaction`
  MODIFY COLUMN `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

RENAME TABLE
    `discount_has_tariff_plan` TO `tariff_plan_has_discount`;