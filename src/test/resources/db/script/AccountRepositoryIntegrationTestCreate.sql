INSERT INTO `contract` VALUES (1, 0, 'LEGAL', NOW(), NULL, NULL, NULL);
INSERT INTO `tariff_plan` VALUES (1, 'Tariff plan 1', 'Description for tariff plan 1', 12, 6, 1000, 0.45, 12.99, 0);
INSERT INTO `contract_annex` VALUES (1, 'Contract annex address 1', NOW(), 0, 1, 1);
INSERT INTO `user` VALUES (1, 'customer', MD5('test password'), 'CUSTOMER', '\0', 1);