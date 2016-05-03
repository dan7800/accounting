--liquibase formatted sql

--changeset:1
create table `transactions` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `timestamp` datetime NOT NULL,
  PRIMARY KEY (`id`)
) EGINE=InnoDB DEFAULT CHARSET=utf8;

create table `entries` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `transactionId` int(11) unsigned NOT NULL,
  `toAccountId` int(11) unsigned NOT NULL,
  `fromAccountId` int(11) unsigned NOT NULL,
  `amount` decimal(60, 30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `accounts` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

