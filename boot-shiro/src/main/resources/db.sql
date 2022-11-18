create table if not exists `sys_user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` varchar(30) NOT NULL COMMENT '用户名',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
    `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
    `password` varchar(64) DEFAULT NULL COMMENT '密码',
    `status` int(5) NOT NULL COMMENT '状态',
    `created` datetime DEFAULT NULL COMMENT '创建时间',
    `last_login` datetime DEFAULT NULL COMMENT '最近登陆时间',
    PRIMARY KEY (`id`),
    KEY `uk_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8  COMMENT '用户信息';


INSERT INTO `sys_user` (`id`, `username`, `avatar`, `email`, `password`, `status`, `created`, `last_login`) VALUES ('1', 'demo', 'https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg', NULL, '96e79218965eb72c92a549dd5a330112', '0', '2021-08-20 10:44:01', NULL);