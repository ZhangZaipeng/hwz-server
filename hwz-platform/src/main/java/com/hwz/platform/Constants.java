package com.hwz.platform;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * 静态常量常量
 * @author zhangzaipeng
 * 
 */
public class Constants {
	// *********************** 从配置文件中     获取变量值  放到静态的变量中

    /**
     * 图片上传的服务器路径 *
     */
    public static final String       UPLOAD_IMG_PATH                   = ConfigUtils.getString("upload.img.path", "/");

    /**
     * 图片上传后访问的相对路径 *
     */
    public static final String       UPLOAD_IMG_URL_RELATIVE           = ConfigUtils.getString("upload.img.url_relative", "/");


    /**
     * 图片上传的扩展名 *
     */
    public static final List<String> UPLOAD_IMG_FILE_EXTENSTION        = Arrays.asList(ConfigUtils.getString("upload.img.file_extension",
                                                                                                 "").split(" "));

    /**
     * 上传图片文件的最大限制 *
     */
    public static final int          UPLOAD_IMG_MAXSIZE                = Conv.NI(ConfigUtils.getString("upload.img.maxsize"), 0);

    /**
     * 文件上传的最大限制  *
     */
    public static final int          UPLOAD_FILE_MAXSIZE               = Conv.NI(ConfigUtils.getString("upload.file.maxsize"), 0);

    /**
     * 文件上传的服务器文件路径 *
     */
    public static final String       UPLOAD_FILE_PATH                  = ConfigUtils.getString("upload.file.path", "/upload/file");

    /**
     * 文件上传后访问的相对路径 *
     */
    public static final String       UPLOAD_FILE_URL_RELATIVE          = ConfigUtils.getString("upload.file.url_relative", "/");
    
    /**
     * 网站上传图片的路径
     */
    public static final String       UPLOAD_IMAGE_PATH                 = ConfigUtils.getString("upload.image.path");
    
    /**
     * fastdfs 服务器路径
     */
    public static final String		 FASTDFS_ADDRESS 				   = ConfigUtils.getString("fastdfs_address");

    /**
     * fastdfs 服务器端口
     */
    public static final int 		 FASTDFS_PORT 					   = Conv.NI(ConfigUtils.getString("fastdfs_port"), 0);

    /**
     * 订单环境标识
     */
    public static final String 		ENV_ORDER						  = ConfigUtils.getString("env_order");
}
