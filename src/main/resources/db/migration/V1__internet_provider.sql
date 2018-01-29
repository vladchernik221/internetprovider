CREATE TABLE `individual_client_information` (
  `individual_client_information_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_name`                       VARCHAR(45)  NOT NULL,
  `second_name`                      VARCHAR(45)  NOT NULL,
  `last_name`                        VARCHAR(45)  NOT NULL,
  `passport_unique_identification`   VARCHAR(45)  NOT NULL,
  `address`                          VARCHAR(45)  NOT NULL,
  `phone_number`                     VARCHAR(45)  NULL,
  PRIMARY KEY (`individual_client_information_id`),
  UNIQUE INDEX `passport_unique_identification_UNIQUE` (`passport_unique_identification`)
);

CREATE TABLE `tariff_plan` (
  `tariff_plan_id`     INT UNSIGNED   NOT NULL AUTO_INCREMENT,
  `name`               VARCHAR(45)    NOT NULL,
  `description`        TEXT           NULL,
  `down_speed`         INT            NOT NULL,
  `up_speed`           INT            NOT NULL,
  `included_traffic`   INT            NULL,
  `price_over_traffic` DECIMAL(15, 2) NULL     DEFAULT 0,
  `monthly_fee`        DECIMAL(15, 2) NOT NULL,
  `archived`           BIT(1)         NOT NULL DEFAULT 0,
  PRIMARY KEY (`tariff_plan_id`),
  UNIQUE INDEX `tariff_plan_name_UNIQUE` (`name`)
);

CREATE TABLE `legal_entity_client_information` (
  `legal_entity_client_information_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `payer_account_number`               VARCHAR(75)  NOT NULL,
  `checking_account`                   VARCHAR(75)  NOT NULL,
  `name`                               VARCHAR(75)  NOT NULL,
  `address`                            VARCHAR(45)  NOT NULL,
  `phone_number`                       VARCHAR(45)  NOT NULL,
  PRIMARY KEY (`legal_entity_client_information_id`),
  UNIQUE INDEX `payer_account_number_UNIQUE` (`payer_account_number`)
);

CREATE TABLE `contract` (
  `contract_id`                        INT UNSIGNED                 NOT NULL AUTO_INCREMENT,
  `dissolved`                          BIT(1)                       NOT NULL DEFAULT 0,
  `client_type`                        SET ('INDIVIDUAL', 'LEGAL') NOT NULL,
  `conclude_date`                      DATETIME                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dissolve_date`                      DATETIME,
  `legal_entity_client_information_id` INT UNSIGNED                 NULL,
  `individual_client_information_id`   INT UNSIGNED                 NULL,
  PRIMARY KEY (`contract_id`),
  CONSTRAINT `fk_contract_legal_entity_client_information`
  FOREIGN KEY (`legal_entity_client_information_id`)
  REFERENCES `legal_entity_client_information` (`legal_entity_client_information_id`),
  CONSTRAINT `fk_contract_individual_client_information`
  FOREIGN KEY (`individual_client_information_id`)
  REFERENCES `individual_client_information` (`individual_client_information_id`)
);

CREATE INDEX `fk_contract_legal_entity_client_information_idx`
  ON `contract` (`legal_entity_client_information_id`);
CREATE INDEX `fk_contract_individual_client_information_idx`
  ON `contract` (`individual_client_information_id`);

CREATE TABLE `contract_annex` (
  `contract_annex_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `address`           VARCHAR(45)  NOT NULL,
  `conclude_date`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `canceled`          BIT(1)       NOT NULL DEFAULT 0,
  `tariff_plan_id`    INT UNSIGNED NOT NULL,
  `contract_id`       INT UNSIGNED NOT NULL,
  PRIMARY KEY (`contract_annex_id`),
  CONSTRAINT `fk_contract_tariff_plan`
  FOREIGN KEY (`tariff_plan_id`)
  REFERENCES `tariff_plan` (`tariff_plan_id`),
  CONSTRAINT `fk_contract_annex_contract`
  FOREIGN KEY (`contract_id`)
  REFERENCES `contract` (`contract_id`)
);

CREATE INDEX `fk_contract_tariff_plan_idx`
  ON `contract_annex` (`tariff_plan_id`);
CREATE INDEX `fk_contract_annex_contract_idx`
  ON `contract_annex` (`contract_id`);

CREATE TABLE `account` (
  `balance`           DECIMAL(15, 2) NOT NULL DEFAULT 0,
  `used_traffic`      INT            NULL     DEFAULT 0,
  `contract_annex_id` INT UNSIGNED   NOT NULL,
  PRIMARY KEY (`contract_annex_id`),
  CONSTRAINT `fk_account_contract_annex`
  FOREIGN KEY (`contract_annex_id`)
  REFERENCES `contract_annex` (`contract_annex_id`)
);

CREATE INDEX `fk_account_contract_annex_idx`
  ON `account` (`contract_annex_id`);

CREATE TABLE `transaction` (
  `transaction_id`    INT UNSIGNED                 NOT NULL AUTO_INCREMENT,
  `type`              SET ('WRITE_OFF', 'REFILL') NOT NULL,
  `amount`            DECIMAL(15, 2)               NOT NULL,
  `date`              DATETIME                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `contract_annex_id` INT UNSIGNED                 NOT NULL,
  PRIMARY KEY (`transaction_id`),
  CONSTRAINT `fk_transaction_account`
  FOREIGN KEY (`contract_annex_id`)
  REFERENCES `account` (`contract_annex_id`)
);

CREATE INDEX `fk_transaction_account_idx`
  ON `transaction` (`contract_annex_id`);

CREATE TABLE `discount` (
  `discount_id`         INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`                VARCHAR(45)  NOT NULL,
  `description`         TEXT         NULL,
  `amount`              INT          NOT NULL,
  `start_date`          DATETIME     NOT NULL,
  `end_date`            DATETIME     NOT NULL,
  `only_for_new_client` BIT(1)       NOT NULL DEFAULT 0,
  PRIMARY KEY (`discount_id`)
);

CREATE TABLE `tariff_plan_has_discount` (
  `discount_id`    INT UNSIGNED NOT NULL,
  `tariff_plan_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`discount_id`, `tariff_plan_id`),
  CONSTRAINT `fk_tariff_plan_has_discount_discount`
  FOREIGN KEY (`discount_id`)
  REFERENCES `discount` (`discount_id`),
  CONSTRAINT `fk_tariff_plan_has_discount_tariff_plan`
  FOREIGN KEY (`tariff_plan_id`)
  REFERENCES `tariff_plan` (`tariff_plan_id`)
);

CREATE INDEX `fk_tariff_plan_has_discount_tariff_plan_idx`
  ON `tariff_plan_has_discount` (`tariff_plan_id`);
CREATE INDEX `fk_tariff_plan_has_discount_discount_idx`
  ON `tariff_plan_has_discount` (`discount_id`);

CREATE TABLE `user` (
  `user_id`     INT UNSIGNED                         NOT NULL AUTO_INCREMENT,
  `login`       VARCHAR(45)                          NOT NULL,
  `password`    VARCHAR(45)                          NOT NULL,
  `role`        SET ('ADMIN', 'SELLER', 'CUSTOMER') NOT NULL,
  `blocked`     BIT(1)                               NOT NULL DEFAULT 0,
  `contract_id` INT UNSIGNED                         NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `login_UNIQUE` (`login`),
  CONSTRAINT `fk_user_contract`
  FOREIGN KEY (`contract_id`)
  REFERENCES `contract` (`contract_id`)
);

CREATE INDEX `fk_user_contract_idx`
  ON `user` (`contract_id`);

CREATE TABLE `service` (
  `service_id`  INT UNSIGNED   NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(45)    NOT NULL,
  `description` VARCHAR(45)    NULL,
  `archived`    BIT(1)         NOT NULL DEFAULT 0,
  `price`       DECIMAL(15, 2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`service_id`),
  UNIQUE INDEX `service_name_UNIQUE` (`name`)
);

CREATE TABLE `contract_annex_has_service` (
  `contract_annex_id` INT UNSIGNED NOT NULL,
  `service_id`        INT UNSIGNED NOT NULL,
  `service_date`      DATETIME     NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`contract_annex_id`, `service_id`, `service_date`),
  CONSTRAINT `fk_contract_annex_has_service_contract`
  FOREIGN KEY (`contract_annex_id`)
  REFERENCES `contract_annex` (`contract_annex_id`),
  CONSTRAINT `fk_contract_has_service_service`
  FOREIGN KEY (`service_id`)
  REFERENCES `service` (`service_id`)
);


CREATE INDEX `fk_contract_annex_has_service_service_idx`
  ON `contract_annex_has_service` (`service_id`);
CREATE INDEX `fk_contract_annex_has_service_contract_idx`
  ON `contract_annex_has_service` (`contract_annex_id`);


INSERT INTO `user` VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'ADMIN', '\0', NULL);
