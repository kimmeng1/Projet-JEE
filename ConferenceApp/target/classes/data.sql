REPLACE INTO `roles` VALUES (1,'ADMIN');
REPLACE INTO `roles` VALUES (2,'USER');

UPDATE user_role SET role_id = 1 where user_id = 1
