DROP DATABASE IF EXISTS `system`;

CREATE DATABASE `system` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE USER 'system'@'%' IDENTIFIED BY 'secdt@3.14';

GRANT ALL ON `system`.* TO 'system'@'%';