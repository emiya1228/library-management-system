CREATE TABLE `users` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
                         `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱',
                         `password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
                         `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
                         `role` enum('管理员','普通用户') COLLATE utf8mb4_unicode_ci DEFAULT '普通用户' COMMENT '角色',
                         `status` enum('正常','冻结') COLLATE utf8mb4_unicode_ci DEFAULT '正常' COMMENT '状态',
                         `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `login_status` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                         `last_login_time` datetime DEFAULT NULL COMMENT '上次登录时间',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `username` (`username`),
                         UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';