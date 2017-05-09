package com.hwz.platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuzhuang on 2016/5/11.
 */
public class FileUtils {
    private static String fileNameExtractorRegex = "filename=\".+\"";

    public static String getFileName(String contentDesc) {
        //获取header信息中的content-disposition，如果为文件，则可以从其中提取出文件名
        //String contentDesc = part.getHeader("content-disposition");
        String fileName = null;
        Pattern pattern = Pattern.compile(fileNameExtractorRegex);
        Matcher matcher = pattern.matcher(contentDesc);
        if (matcher.find()) {
            fileName = matcher.group();
            fileName = fileName.substring(10, fileName.length() - 1);
        }
        return fileName;
    }
    
    // 获取文件后缀名
    public static String getExtName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }
    
    // 创建文件
    public static File createFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {//文件是否存在
            makeDir(file.getParentFile());
        }
        return file;
    }
    
    //创建文件目录
    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {//父节点目录是否存在
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }
    
    //删除文件
    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }
    
    // 文件 MD5 可以 校验文件是否相同
    public static String getFileMD5(File file) throws IOException {
        // 缓冲区大小（这个可以抽出一个参数）
        int bufferSize = 256 * 1024;
        FileInputStream fileInputStream = null;
        DigestInputStream digestInputStream = null;
        try {
            // 拿到一个MD5转换器（同样，这里可以换成SHA1）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 使用DigestInputStream
            fileInputStream = new FileInputStream(file);
            digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
            // read的过程中进行MD5处理，直到读完文件
            byte[] buffer = new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0)
                ;
            // 获取最终的MessageDigest
            messageDigest = digestInputStream.getMessageDigest();
            // 拿到结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 同样，把字节数组转换成字符串
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
        	return null;
        } finally {
            try {
                digestInputStream.close();
            } catch (Exception e) {
            }
            try {
                fileInputStream.close();
            } catch (Exception e) {
            }

        }

    }

    // 这个函数用于将字节数组换成成16进制的字符串
    public static String byteArrayToHex(byte[] byteArray) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < byteArray.length; n++) {
            stmp = (Integer.toHexString(byteArray[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < byteArray.length - 1) {
                hs = hs + "";
            }
        }
        return hs;
    }

}
