--liquibase formatted sql

--changeset keegan:1 dbms:mysql
CREATE TABLE `transactions` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `timestamp` datetime NOT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `entries` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `transactionId` int(11) unsigned NOT NULL,
  `toAccountId` int(11) unsigned NOT NULL,
  `fromAccountId` int(11) unsigned NOT NULL,
  `amount` decimal(60, 30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `accounts` (
  `id` int(11) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `balance` decimal(60, 30) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `accounts` (`id`, `name`) VALUES (1, 'Employees'), (2, 'Inventory'), (3, 'Cash'), (4, 'Revenues'), (5, 'Cost of Goods Sold'), (6, 'Sales Tax Payable'), (7, 'Refunds Paid');

--changeset keegan:2 dbms:mysql
INSERT INTO `accounts` (`id`, `name`) VALUES (8, 'Investment');