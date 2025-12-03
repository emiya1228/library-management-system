CREATE TABLE `fine` (
                        `id` bigint(20) NOT NULL COMMENT '罚金ID，使用Redis序列生成，非自增',
                        `user_id` int(11) NOT NULL COMMENT '用户ID',
                        `borrow_record_id` int(11) NOT NULL COMMENT '关联的借阅记录ID',
                        `amount` decimal(10,2) NOT NULL COMMENT '罚金金额（单位：元）',
                        `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：0-未支付，1-已支付',
                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
                        PRIMARY KEY (`id`),
                        KEY `idx_user_id` (`user_id`),
                        KEY `idx_borrow_record_id` (`borrow_record_id`),
                        KEY `idx_status` (`status`),
                        CONSTRAINT `fk_fine_borrow_record` FOREIGN KEY (`borrow_record_id`) REFERENCES `borrow_records` (`id`) ON DELETE CASCADE,
                        CONSTRAINT `fk_fine_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='罚金记录表';