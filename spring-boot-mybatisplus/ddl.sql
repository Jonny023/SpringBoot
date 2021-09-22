CREATE TABLE `sys_user` (
       `user_id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
       `username` varchar(30)  NOT NULL COMMENT '用户名',
       `password` varchar(100) NOT NULL COMMENT '密码',
       PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息'