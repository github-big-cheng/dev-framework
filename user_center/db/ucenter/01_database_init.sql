DROP DATABASE IF EXISTS `ucenter`;

CREATE DATABASE `ucenter` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE USER 'ucenter'@'%' IDENTIFIED BY 'secdt@3.14';

GRANT ALL ON `ucenter`.* TO 'ucenter'@'%';