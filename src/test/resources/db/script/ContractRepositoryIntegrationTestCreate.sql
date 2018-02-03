INSERT INTO `individual_client_information`
VALUES (1, 'FirstName test 1', 'SecondName test 1', 'LastName test 1', 'PassportUnique test 1', 'Address test 1',
        'PhoneNumber test 1');
INSERT INTO `legal_entity_client_information`
VALUES
  (1, 'PayerAccountNumber test 1', 'CheckingAccount test 1', 'Name test 1', 'Address test 1', 'PhoneNumber test 1');
INSERT INTO `individual_client_information`
VALUES (2, 'FirstName test 2', 'SecondName test 2', 'LastName test 2', 'PassportUnique test 2', 'Address test 2',
        'PhoneNumber test 2');
INSERT INTO `legal_entity_client_information`
VALUES
  (2, 'PayerAccountNumber test 2', 'CheckingAccount test 2', 'Name test 2', 'Address test 2', 'PhoneNumber test 2');

INSERT INTO `contract` VALUES (1, 0, 'INDIVIDUAL', '2018-01-28 00:00:00', NULL, NULL, 1);
INSERT INTO `contract` VALUES (2, 1, 'LEGAL', '2018-01-28 00:00:00', NULL, 1, NULL);
INSERT INTO `contract` VALUES (3, 0, 'LEGAL', '2018-01-28 00:00:00', NULL, 1, NULL);
INSERT INTO `contract` VALUES (4, 1, 'INDIVIDUAL', '2018-01-28 00:00:00', NULL, NULL, 2);
INSERT INTO `contract` VALUES (5, 1, 'LEGAL', '2018-01-28 00:00:00', NULL, 2, NULL);

INSERT INTO `tariff_plan` VALUES (1, 'Tariff plan 1', 'Description for tariff plan 1', 12, 6, 1000, 0.45, 12.99, 0);

INSERT INTO `contract_annex` VALUES (1, 'Contract annex address 1', '2018-01-28 00:00:00', 0, 1, 1);
INSERT INTO `contract_annex` VALUES (2, 'Contract annex address 2', '2018-01-28 00:00:00', 1, 1, 3);

INSERT INTO `user` VALUES (1, 'customer', MD5('test password'), 'CUSTOMER', '\0', 1);