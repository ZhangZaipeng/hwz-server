/**
 * @(#)FileUtils.java Copyright 2011 jointown, Inc. All rights reserved.
 */
package com.hwz.platform;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * description
 * 
 * @author zhangzaipeng
 * @version 1.0,2016-3-17
 */
public class FileUploadUtils {

    private static final Log    LOG              = LogFactory.getLog(FileUploadUtils.class);
    private static final String DEFALUT_SUFFIX   = "temp";
    private static final String FILE_SEPARATOR   = "/";
    
    /**
     * 上传文件的最大文件字节数(单位k)
     */
    private static final long   DEFAULT_MAX_SIZE = 2048;

    public static UploadResult uploadFile(HttpServletRequest request, String fileParamter,
                                          List<String> allowPass) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile(fileParamter);
        if (multipartFile == null || multipartFile.getSize() == 0) {
            throw new IOException("上传文件不存在");
        }
        
        // multipartFile.getOriginalFilename() 获取上传文件的原名
        String fileName = multipartFile.getOriginalFilename();
        if (StringUtils.isNullOrEmpty(fileName)) {
            throw new IOException("非法文件");
        }
        
        // multipartFile.getSize() 获取文件大小  单位byte
        if (multipartFile.getSize() > Constants.UPLOAD_FILE_MAXSIZE * 1024) {
            throw new IOException("上传文件不能超过  " + Constants.UPLOAD_FILE_MAXSIZE + " K");
        }
        
        //获取源文件名后缀名
        String fileExt = "";
        int suffixPos = fileName.lastIndexOf(".");
        if (suffixPos > 0) {
            fileExt = fileName.substring(suffixPos + 1);
        }
        //allowPass 允许通过的后缀名
        if (!allowPass.contains(fileExt.toLowerCase())) {
            throw new IOException("非法文件格式" + fileExt.toLowerCase());
        }

        String day = DateUtils.format(new Date(), "yyyy-MM-dd");
        String filePrefix = UUID.randomUUID().toString();
        String lastFileName = filePrefix + "." + fileExt;

        String serverFilePath = Constants.UPLOAD_FILE_PATH;
        if (serverFilePath.endsWith("/") || serverFilePath.endsWith("\\")) {
            serverFilePath = serverFilePath.substring(0, serverFilePath.length() - 1);
        }
        serverFilePath = serverFilePath + File.separatorChar + day + File.separatorChar ;
        File filePathDir = new File(serverFilePath);
        String serverFileFullPath = serverFilePath + lastFileName;

        if (!filePathDir.isDirectory()) {
            filePathDir.mkdirs();
        }

        String fullRelative = Constants.UPLOAD_FILE_URL_RELATIVE;
        String relative = day + "/" + lastFileName;
        if (fullRelative.endsWith("/") || fullRelative.endsWith("\\")) {//配置路径最后是不是"/"结尾
            fullRelative = fullRelative.substring(0, fullRelative.length() - 1);
        }
        fullRelative = fullRelative + "/" + relative;

        File destFile = new File(serverFileFullPath);
        try {
        	//保存到一个目标文件中
            multipartFile.transferTo(destFile);
        } catch (Exception e) {
            LOG.error("upload image error", e);
            throw new IOException("upload image error", e);
        }

        UploadResult ret = new UploadResult();
        ret.setServerFileFullPath(serverFileFullPath);
        ret.setFullRelative(fullRelative);
        ret.setRelative(relative);
        ret.setFileName(fileName);
        ret.setFileSize(multipartFile.getSize()/1024);

        LOG.info("upload file to server path success: " + serverFileFullPath);

        return ret;
    }

    /**
     * 上传图片
     * 
     * @param request
     * @param fileParamter
     * @return
     * @throws IOException
     */
    public static UploadResult uploadImage(HttpServletRequest request, String fileParamter) throws IOException {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile(fileParamter);
        
        if (multipartFile == null || multipartFile.getSize() == 0) {
            UploadResult ret = new UploadResult();
            ret.setServerFileFullPath(null);
            ret.setFullRelative(null);
            ret.setRelative(null);
            return ret;
        }
        
        // multipartFile.getOriginalFilename() 获取上传文件的原名
        String fileName = multipartFile.getOriginalFilename();
        if (StringUtils.isNullOrEmpty(fileName)) {
            throw new IOException("非法文件");
        }
        
        // multipartFile.getSize() 获取文件大小  单位byte
        if (multipartFile.getSize() > Constants.UPLOAD_IMG_MAXSIZE * 1024) {
            throw new IOException("上传文件不能超过  " + Constants.UPLOAD_IMG_MAXSIZE + " K");
        }
        
        // 获取图片后缀名
        String fileExt = "";
        int suffixPos = fileName.lastIndexOf(".");
        if (suffixPos > 0) {
            fileExt = fileName.substring(suffixPos + 1);
        }
        if (!Constants.UPLOAD_IMG_FILE_EXTENSTION.contains(fileExt.toLowerCase())) {
            throw new IOException("非法文件格式" + fileExt.toLowerCase());
        }
        
        // 上传后的文件名
        String filePrefix = UUID.randomUUID().toString();
        String lastFileName = filePrefix + "." + fileExt.toLowerCase();;

        // 物理磁盘路径
        String serverFilePath = Constants.UPLOAD_IMG_PATH;
        if (serverFilePath.endsWith("/") || serverFilePath.endsWith("\\")) {
            serverFilePath = serverFilePath.substring(0, serverFilePath.length() - 1);
        }
        
        //上传文件按天存放
        String day = DateUtils.format(new Date(), "yyyy-MM-dd");
        serverFilePath = serverFilePath + File.separatorChar + day + File.separatorChar ;
        File filePathDir = new File(serverFilePath);

        if (!filePathDir.isDirectory()) {
            filePathDir.mkdirs();
        }
        
        //按天分割改名后   图片的绝对路径 + 文件名
        String serverFileFullPath = serverFilePath + lastFileName;
        File destFile = new File(serverFileFullPath);

        // 网址访问路径
        String fullRelative = Constants.UPLOAD_IMG_URL_RELATIVE;
        String relative = "" + day + "/" + lastFileName;
        if (fullRelative.endsWith("/") || fullRelative.endsWith("\\")) {//配置路径最后是不是"/"结尾
            fullRelative = fullRelative.substring(0, fullRelative.length() - 1);
        }
        fullRelative = fullRelative + "/" + relative;

        
        // 得到当前图片的路径
        // String currentUrl = destFile.getAbsolutePath();
        // 通过当前图片的路径，得到缩略图的路径
        // String finalUrl = destFile.getAbsolutePath();
        // finalUrl = finalUrl.substring(0, finalUrl.indexOf(".")) + "_s"
        // + currentUrl.substring(currentUrl.indexOf("."), currentUrl.length());
        try {

            // 将上传的图片保存在指定文件夹里
            multipartFile.transferTo(destFile);

            // 将上传的图片生成缩略图片，保存在同样的文件夹里
            // JpegTool tool = new JpegTool();
            // tool.SetScale(0.7);
            // 设置缩略图片的高度
            // tool.SetSmallHeight(Constants.SMALL_IMG_HEIGHT);
            // tool.doFinal(currentUrl, finalUrl);
        } catch (Exception e) {
            LOG.error("upload image error", e);
            throw new IOException("upload image error", e);
        }

        UploadResult ret = new UploadResult();
        ret.setServerFileFullPath(serverFileFullPath);
        ret.setFullRelative(fullRelative);
        ret.setRelative(relative);
        ret.setFileName(fileName);
        ret.setFileSize(multipartFile.getSize()/1024);
        
        LOG.info("upload image to server path success: " + serverFileFullPath);

        return ret;
    }

    /**
     * 批量上传图片
     * 
     * @param request
     * @param fileParamter
     * @return
     * @throws IOException
     */
    public static List<UploadResult> uploadMultImages(HttpServletRequest request,
                                                      String fileParamter) throws IOException {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        // MultipartFile multipartFile = multipartRequest.getFile(fileParamter);
        List<MultipartFile> list = multipartRequest.getFiles(fileParamter);
        List<UploadResult> listResult = new ArrayList<UploadResult>();
        for (MultipartFile multipartFile : list) {
            if (multipartFile == null || multipartFile.getSize() == 0) {
                UploadResult ret = new UploadResult();
                ret.setServerFileFullPath(null);
                ret.setFullRelative(null);
                ret.setRelative(null);
                break;
            }
            String filename = multipartFile.getOriginalFilename();
            if (StringUtils.isNullOrEmpty(filename)) {
                throw new IOException("非法文件");
            }

            if (multipartFile.getSize() > Constants.UPLOAD_IMG_MAXSIZE * 1024) {
                throw new IOException("上传文件不能超过  " + Constants.UPLOAD_IMG_MAXSIZE + " K");
            }

            String fileExt = "";
            int suffixPos = filename.lastIndexOf(".");
            if (suffixPos > 0) {
                fileExt = filename.substring(suffixPos + 1);
            }
            if (!Constants.UPLOAD_IMG_FILE_EXTENSTION.contains(fileExt.toLowerCase())) {
                throw new IOException("非法文件格式" + fileExt.toLowerCase());
            }

            String day = DateUtils.format(new Date(), "yyyy-MM-dd");
            String filePrefix = UUID.randomUUID().toString();
            filename = filePrefix + "." + fileExt;

            String serverFilePath = Constants.UPLOAD_IMG_PATH;
            if (serverFilePath.endsWith("/") || serverFilePath.endsWith("\\")) {
                serverFilePath = serverFilePath.substring(0, serverFilePath.length() - 1);
            }
            serverFilePath = serverFilePath + File.separatorChar + day + File.separatorChar + File.separatorChar;
            File filePathDir = new File(serverFilePath);
            String serverFileFullPath = serverFilePath + filename;

            if (!filePathDir.isDirectory()) {
                filePathDir.mkdirs();
            }

            String fullRelative = Constants.UPLOAD_IMG_URL_RELATIVE;
            String relative = day + "/" + filename;
            if (fullRelative.endsWith("/") || fullRelative.endsWith("\\")) {
                fullRelative = fullRelative.substring(0, fullRelative.length() - 1);
            }
            fullRelative = fullRelative + "/" + relative;

            File destFile = new File(serverFileFullPath);
            try {
                multipartFile.transferTo(destFile);
            } catch (Exception e) {
                LOG.error("upload image error", e);
                throw new IOException("upload image error", e);
            }

            UploadResult ret = new UploadResult();
            ret.setServerFileFullPath(serverFileFullPath);
            ret.setFullRelative(fullRelative);
            ret.setRelative(relative);
            listResult.add(ret);
        }

        return listResult;
    }

    public static class UploadResult { //上传文件返回结果
    	// 图片在磁盘上真实路径
        private String serverFileFullPath;
        // 图片网络访问路径
        private String fullRelative;
        private String relative;
        //原文件名
        private String fileName;
        //文件大小  k
        private long   fileSize;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public long getFileSize() {
            return fileSize;
        }

        public void setFileSize(long fileSize) {
            this.fileSize = fileSize;
        }

        public String getServerFileFullPath() {
            return serverFileFullPath;
        }

        public void setServerFileFullPath(String serverFileFullPath) {
            this.serverFileFullPath = serverFileFullPath;
        }

        public String getFullRelative() {
            return fullRelative;
        }

        public void setFullRelative(String fullRelative) {
            this.fullRelative = fullRelative;
        }

        public String getRelative() {
            return relative;
        }

        public void setRelative(String relative) {
            this.relative = relative;
        }

    }
    
}
