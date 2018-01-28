INSERT INTO `contract` VALUES (1, 0, 'LEGAL', NOW(), NULL, NULL, NULL);
INSERT INTO `tariff_plan` VALUES (1, 'Tariff plan 1', 'Description for tariff plan 1', 12, 6, 1000, 0.45, 12.99, 0);
INSERT INTO `contract_annex` VALUES (1, 'Contract annex address 1', NOW(), 0, 1, 1);
INSERT INTO `transaction` VALUES (1, 'REFILL', 12, '2018-01-28 00:00:00', 1);
INSERT INTO `transaction` VALUES (2, 'WRITE_OFF', 145, '2018-01-28 00:00:00', 1);
INSERT INTO `transaction` VALUES (3, 'REFILL', 7, '2018-01-28 00:00:00', 1);