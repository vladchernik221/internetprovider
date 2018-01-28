INSERT INTO `individual_client_information`
VALUES (1, 'FirstName test 1', 'SecondName test 1', 'LastName test 1', 'PassportUnique test 1', 'Address test 1',
        'PhoneNumber test 1');

INSERT INTO `contract` VALUES (1, 0, 'INDIVIDUAL', '2018-01-28 00:00:00', NULL, NULL, 1);

INSERT INTO `tariff_plan` VALUES (1, 'Tariff plan 1', 'Description for tariff plan 1', 12, 6, 1000, 0.45, 12.99, 0);

INSERT INTO `contract_annex` VALUES (1, 'Contract annex address 1', '2018-01-28 00:00:00', 0, 1, 1);

INSERT INTO `service` VALUES (1, 'Service 1', 'Description 1', 0, 12.22);
INSERT INTO `service` VALUES (2, 'Service 2', 'Description 2', 1, 6.99);
INSERT INTO `service` VALUES (3, 'Service 3', 'Description 3', 0, 8.99);

INSERT INTO `contract_annex_has_service` VALUES (1, 1, '2018-01-28 00:00:00');
INSERT INTO `contract_annex_has_service` VALUES (1, 2, '2018-01-28 00:00:00');
INSERT INTO `contract_annex_has_service` VALUES (1, 3, '2018-01-28 00:00:00');

