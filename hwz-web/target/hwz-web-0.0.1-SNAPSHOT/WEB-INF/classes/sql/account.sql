DROP TABLE IF EXISTS `tb_account`;
create table `tb_account`(
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键',
	`name` varchar(20) NOT NULL COMMENT '账号名称',
	`money` double DEFAULT NULL COMMENT '账户金额',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
DEFAULT CHARSET = utf8
ROW_FORMAT=DEFAULT
COMMENT='用户表'
AUTO_INCREMENT=1
;
