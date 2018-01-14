CREATE TABLE `individual_client_information` (
  `individual_client_information_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_name`                       VARCHAR(45)  NOT NULL,
  `second_name`                      VARCHAR(45)  NOT NULL,
  `last_name`                        VARCHAR(45)  NOT NULL,
  `passport_unique_identification`   VARCHAR(45)  NOT NULL,
  `address`                          VARCHAR(45)  NOT NULL,
  `phone_number`                     VARCHAR(45)  NULL,
  PRIMARY KEY (`individual_client_information_id`),
  UNIQUE INDEX `passport_unique_identification_UNIQUE` (`passport_unique_identification` ASC)
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
  UNIQUE INDEX `name_UNIQUE` (`name` ASC)
);

CREATE TABLE `legal_entity_client_information` (
  `legal_entity_client_information_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `payer_account_number`               VARCHAR(75)  NOT NULL,
  `name`                               VARCHAR(75)  NOT NULL,
  `address`                            VARCHAR(45)  NOT NULL,
  `phone_number`                       VARCHAR(45)  NOT NULL,
  PRIMARY KEY (`legal_entity_client_information_id`),
  UNIQUE INDEX `payer_account_number_UNIQUE` (`payer_account_number` ASC)
);

CREATE TABLE `contract` (
  `contract_id`                        INT UNSIGNED                NOT NULL AUTO_INCREMENT,
  `dissolved`                          BIT(1)                      NOT NULL DEFAULT 0,
  `client_type`                        SET ('INDIVIDUAL', 'LEGAL') NOT NULL,
  `legal_entity_client_information_id` INT UNSIGNED                NULL,
  `individual_client_information_id`   INT UNSIGNED                NULL,
  PRIMARY KEY (`contract_id`),
  INDEX `fk_contract_legal_entity_client_information_idx` (`legal_entity_client_information_id` ASC),
  INDEX `fk_contract_individual_client_information_idx` (`individual_client_information_id` ASC),
  CONSTRAINT `fk_contract_legal_entity_client_information`
  FOREIGN KEY (`legal_entity_client_information_id`)
  REFERENCES `legal_entity_client_information` (`legal_entity_client_information_id`),
  CONSTRAINT `fk_contract_individual_client_information`
  FOREIGN KEY (`individual_client_information_id`)
  REFERENCES `individual_client_information` (`individual_client_information_id`)
);

CREATE TABLE `contract_annex` (
  `contract_annex_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `address`           VARCHAR(45)  NOT NULL,
  `conclude_date`     DATETIME     NOT NULL,
  `canceled`          BIT(1)       NOT NULL DEFAULT 0,
  `tariff_plan_id`    INT UNSIGNED NOT NULL,
  `contract_id`       INT UNSIGNED NOT NULL,
  PRIMARY KEY (`contract_annex_id`),
  INDEX `fk_contract_tariff_plan_idx` (`tariff_plan_id` ASC),
  INDEX `fk_contract_annex_contract_idx` (`contract_id` ASC),
  CONSTRAINT `fk_contract_tariff_plan`
  FOREIGN KEY (`tariff_plan_id`)
  REFERENCES `tariff_plan` (`tariff_plan_id`),
  CONSTRAINT `fk_contract_annex_contract`
  FOREIGN KEY (`contract_id`)
  REFERENCES `contract` (`contract_id`)
);

CREATE TABLE `account` (
  `account_id`        INT UNSIGNED   NOT NULL AUTO_INCREMENT,
  `balance`           DECIMAL(15, 2) NOT NULL DEFAULT 0,
  `trafficked_trafic` INT            NULL,
  `contract_annex_id` INT UNSIGNED   NOT NULL,
  PRIMARY KEY (`account_id`),
  INDEX `fk_account_contract_annex_idx` (`contract_annex_id` ASC),
  CONSTRAINT `fk_account_contract_annex`
  FOREIGN KEY (`contract_annex_id`)
  REFERENCES `contract_annex` (`contract_annex_id`)
);

CREATE TABLE `transaction` (
  `transaction_id` INT UNSIGNED                NOT NULL AUTO_INCREMENT,
  `type`           SET ('WRITE_OFF', 'REFILL') NOT NULL,
  `amount`         DECIMAL(15, 2)              NOT NULL,
  `date`           DATETIME                    NOT NULL,
  `account_id`     INT UNSIGNED                NOT NULL,
  PRIMARY KEY (`transaction_id`),
  INDEX `fk_transaction_account_idx` (`account_id` ASC),
  CONSTRAINT `fk_transaction_account`
  FOREIGN KEY (`account_id`)
  REFERENCES `account` (`account_id`)
);

CREATE TABLE `discount` (
  `discount_id`         INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `description`         TEXT         NULL,
  `amount`              INT          NOT NULL,
  `start_date`          DATETIME     NOT NULL,
  `end_date`            DATETIME     NOT NULL,
  `only_for_new_client` BIT(1)       NOT NULL DEFAULT 0,
  PRIMARY KEY (`discount_id`)
);

CREATE TABLE `discount_has_tariff_plan` (
  `discount_id`    INT UNSIGNED NOT NULL,
  `tariff_plan_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`discount_id`, `tariff_plan_id`),
  INDEX `fk_discount_has_tariff_plan_tariff_plan_idx` (`tariff_plan_id` ASC),
  INDEX `fk_discount_has_tariff_plan_discount_idx` (`discount_id` ASC),
  CONSTRAINT `fk_discount_has_tariff_plan_discount`
  FOREIGN KEY (`discount_id`)
  REFERENCES `discount` (`discount_id`),
  CONSTRAINT `fk_discount_has_tariff_plan_tariff_plan`
  FOREIGN KEY (`tariff_plan_id`)
  REFERENCES `tariff_plan` (`tariff_plan_id`)
);

CREATE TABLE `user` (
  `user_id`     INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login`       VARCHAR(45)  NOT NULL,
  `password`    VARCHAR(45)  NULL,
  `role`        SET ('ADMIN', 'SELLER', 'CUSTOMER'),
  `blocked`     BIT(1)       NOT NULL DEFAULT 0,
  `contract_id` INT UNSIGNED NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC),
  INDEX `fk_user_contract_idx` (`contract_id` ASC),
  CONSTRAINT `fk_user_contract`
  FOREIGN KEY (`contract_id`)
  REFERENCES `contract` (`contract_id`)
);

CREATE TABLE `service` (
  `service_id`  INT UNSIGNED   NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(45)    NOT NULL,
  `description` VARCHAR(45)    NULL,
  `archived`    BIT(1)         NOT NULL DEFAULT 0,
  `price`       DECIMAL(15, 2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`service_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC)
);

CREATE TABLE `contract_has_service` (
  `contract_id`  INT UNSIGNED NOT NULL,
  `service_id`   INT UNSIGNED NOT NULL,
  `service_date` DATETIME     NOT NULL,
  PRIMARY KEY (`contract_id`, `service_id`, `service_date`),
  INDEX `fk_contract_has_service_service_idx` (`service_id` ASC),
  INDEX `fk_contract_has_service_contract_idx` (`contract_id` ASC),
  CONSTRAINT `fk_contract_has_service_contract`
  FOREIGN KEY (`contract_id`)
  REFERENCES `contract_annex` (`contract_id`),
  CONSTRAINT `fk_contract_has_service_service`
  FOREIGN KEY (`service_id`)
  REFERENCES `service` (`service_id`)
);

INSERT INTO `user` VALUES (1, 'vladchernik221', '81dc9bdb52d04dc20036dbd8313ed055', 'ADMIN', '\0', NULL);
