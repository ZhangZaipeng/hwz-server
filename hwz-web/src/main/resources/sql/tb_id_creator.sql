DROP TABLE IF EXISTS `tb_id_creator`;
CREATE TABLE `tb_id_creator` (
   `id_value` BIGINT(11) DEFAULT NULL COMMENT '主键当前值',
   `id_name` VARCHAR(50) NOT NULL COMMENT '主键名称',
   `id_desc` VARCHAR(50) DEFAULT NULL COMMENT '主键描述',
   PRIMARY KEY (`id_name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='序列编号表'