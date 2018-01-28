INSERT INTO `tariff_plan` VALUES (1, 'Tariff plan 1', 'Description for tariff plan 1', 12, 6, 1000, 0.45, 12.99, 0);

INSERT INTO `discount` VALUES (1, 'Discount 1', 'Description 1', 12, '2018-01-28 00:00:00', '2018-01-28 00:00:00', 0);
INSERT INTO `discount` VALUES (2, 'Discount 2', 'Description 2', 24, '2018-01-28 00:00:00', '2018-01-28 00:00:00', 0);
INSERT INTO `discount` VALUES (3, 'Discount 3', 'Description 3', 36, '2018-01-28 00:00:00', '2018-01-28 00:00:00', 0);

INSERT INTO `tariff_plan_has_discount` VALUES (1, 1);
INSERT INTO `tariff_plan_has_discount` VALUES (3, 1);