CREATE TABLE `borrow_records` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `user_id` int NOT NULL COMMENT '用户ID',
                                  `book_id` int NOT NULL COMMENT '图书ID',
                                  `borrow_date` date NOT NULL COMMENT '借阅日期',
                                  `due_date` date NOT NULL COMMENT '应还日期',
                                  `return_date` datetime DEFAULT NULL COMMENT '实际归还日期',
                                  `status` enum('借阅中','已归还','逾期') COLLATE utf8mb4_unicode_ci DEFAULT '借阅中' COMMENT '状态',
                                  `renew_count` int DEFAULT '0' COMMENT '续借次数',
                                  PRIMARY KEY (`id`),
                                  KEY `idx_user_id` (`user_id`),
                                  KEY `idx_book_id` (`book_id`),
                                  KEY `idx_status` (`status`),
                                  CONSTRAINT `borrow_records_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                                  CONSTRAINT `borrow_records_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='借阅记录表';