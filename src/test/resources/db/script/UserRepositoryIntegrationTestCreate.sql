INSERT INTO `individual_client_information`
VALUES (1, 'FirstName test 1', 'SecondName test 1', 'LastName test 1', 'PassportUnique test 1', 'Address test 1',
        'PhoneNumber test 1');

INSERT INTO `contract` VALUES (1, 0, 'INDIVIDUAL', '2018-01-28 00:00:00', NULL, NULL, 1);

INSERT INTO `user` VALUES (1, 'Login 1', MD5('Password 1'), 'ADMIN', 0, 1);
INSERT INTO `user` VALUES (2, 'Login 2', MD5('Password 2'), 'SELLER', 1, 1);
INSERT INTO `user` VALUES (3, 'Login 3', MD5('Password 3'), 'CUSTOMER', 0, 1);
INSERT INTO `user` VALUES (4, 'Login 4', MD5('Password 4'), 'ADMIN', 0, 1);
INSERT INTO `user` VALUES (5, 'Login 5', MD5('Password 5'), 'SELLER', 1, 1);
INSERT INTO `user` VALUES (6, 'Login 6', MD5('Password 6'), 'CUSTOMER', 0, 1);
INSERT INTO `user` VALUES (7, 'Login 7', MD5('Password 7'), 'ADMIN', 0, 1);
INSERT INTO `user` VALUES (8, 'Login 8', MD5('Password 8'), 'SELLER', 0, 1);
INSERT INTO `user` VALUES (9, 'Login 9', MD5('Password 9'), 'CUSTOMER', 1, 1);