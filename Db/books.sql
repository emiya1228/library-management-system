CREATE TABLE `books` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '书名',
                         `author` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '作者',
                         `isbn` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ISBN',
                         `publisher` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '出版社',
                         `publish_date` date DEFAULT NULL COMMENT '出版日期',
                         `category` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类',
                         `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
                         `total_copies` int DEFAULT '1' COMMENT '总册数',
                         `available_copies` int DEFAULT '1' COMMENT '可借册数',
                         `description` text COLLATE utf8mb4_unicode_ci COMMENT '描述',
                         `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `isbn` (`isbn`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图书表';