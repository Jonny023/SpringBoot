-- db1.orders_0 definition
CREATE DATABASE IF NOT EXISTS db1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
CREATE TABLE IF NOT EXISTS `orders_0` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `order_no` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    `total_amount` decimal(10,2) NOT NULL,
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE DATABASE IF NOT EXISTS db2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- db2.orders_1 definition
CREATE TABLE IF NOT EXISTS `orders_1` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `order_no` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    `total_amount` decimal(10,2) NOT NULL,
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;