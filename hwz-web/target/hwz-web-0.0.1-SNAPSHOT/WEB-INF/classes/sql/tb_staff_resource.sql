CREATE TABLE `tb_staff_resource` (
	`resource_id` int(11) NOT NULL COMMENT '资源编号',
	`resource_category` varchar(50) DEFAULT NULL COMMENT '功能大类',
	`resource_name` varchar(50) DEFAULT NULL COMMENT '功能名称',
	`resource_url` varchar(128) DEFAULT NULL COMMENT '功能对应的url',
	`sorted` float DEFAULT NULL COMMENT '排序属性',
	`resource_img` varchar(50) DEFAULT NULL COMMENT '导航图标样式',
	`deleted` smallint(1) DEFAULT '0' COMMENT '0 有效 1 无效',
	`created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	PRIMARY KEY (`resource_id`)
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8
COLLATE='utf8_unicode_ci'
AUTO_INCREMENT=100
COMMENT='功能'
;


INSERT INTO tb_staff_resource (resource_id, resource_category, resource_name, resource_url, sorted, resource_img, deleted)
VALUES('100', '油管家', '充值查询1', '/admin/oil_now.htm', '1.0', 'resource_img', '0');

INSERT INTO tb_staff_resource (resource_id, resource_category, resource_name, resource_url, sorted, resource_img, deleted)
VALUES('101', '油管家', '充值查询2', '/admin/oil_now.htm', '1.1', 'resource_img', '0');

INSERT INTO tb_staff_resource (resource_id, resource_category, resource_name, resource_url, sorted, resource_img, deleted)
VALUES('102', '自驾游', '充值查询1', '/admin/oil_now.htm', '3.0', 'resource_img', '0');

INSERT INTO tb_staff_resource (resource_id, resource_category, resource_name, resource_url, sorted, resource_img, deleted)
VALUES('103', '自驾游', '充值查询2', '/admin/oil_now.htm', '3.1', 'resource_img', '0');

