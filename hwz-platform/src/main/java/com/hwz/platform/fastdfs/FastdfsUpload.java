package com.hwz.platform.fastdfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import com.hwz.platform.StringUtils;


public class FastdfsUpload {
	
	private static final Logger logger = Logger.getLogger(FastdfsUpload.class);

	public static void main(String[] args){
		try {
			File file = new File("F:\\ccc.txt");
			InputStream inputStream = new FileInputStream(file);
			//String path = uploadAppenderFile("txt", inputStream);
			int path = AppenderFile("group1/M00/00/00/CmiQl1jQ7RyEF1c2AAAAAO49PBk621.txt",inputStream);
			System.out.println(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String uploadAppenderFile(String uploadFileName, InputStream inputStream){	
		// 返回字符文件ID
		String upPath = null;
		StorageClient1 client1 = null;
		try {
			byte[] file_buff = null;
			int len = 0;
			if (inputStream != null) {
				len = inputStream.available();
				file_buff = new byte[len];
				inputStream.read(file_buff);
			}
			
			// 获取文件后缀
			String fileExtName = getExtName(uploadFileName);
			
			// 获取连接
			client1 = ConnectionPool.getPoolInstance().checkout(10);
	
			//设置文件元信息  
			NameValuePair[] metaList = new NameValuePair[3];  
			metaList[0] = new NameValuePair("fileName", uploadFileName);  
			metaList[1] = new NameValuePair("fileExtName", fileExtName);  
			metaList[2] = new NameValuePair("fileLength", String.valueOf(len));
			
			upPath = client1.upload_appender_file1(file_buff, fileExtName, metaList);
			
			// 回收连接
			ConnectionPool.getPoolInstance().checkin(client1);
			
		} catch (InterruptedException e){
			e.printStackTrace();
		} catch (Exception e) {
			ConnectionPool.getPoolInstance().drop(client1);
			e.printStackTrace();
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
			}
		}
		return upPath;  
	}


	/**
	* 文件追加操作
	* @param fileId
	* @param inputStream
	* @return
	*/
	public static int AppenderFile(String fileId, InputStream inputStream){	
		int result = -1;  
		try {
			StorageClient1 client = null;
			client = ConnectionPool.getPoolInstance().checkout(10);
			byte[] buff = new byte[1024]; 
			int readcount;
			while ((readcount = inputStream.read( buff )) != -1) {  
				byte[] newbuffer;
				if (readcount < buff.length) {
					newbuffer = new byte[readcount];
					System.arraycopy(buff, 0, newbuffer, 0, readcount);
					result = client.append_file1(fileId, newbuffer);
				} else {
					result = client.append_file1(fileId, buff);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		} 
		return result;
	}

	public static String upoladFile(String uploadFileName, InputStream inputStream){
		String upPath = null;
		StorageClient1 client1 = null;
		try {
			byte[] file_buff = null;
			int len = 0;
			if (inputStream != null) {
				len = inputStream.available();
				file_buff = new byte[len];
				inputStream.read(file_buff);
			}
			
			// 获取文件后缀
			String fileExtName = getExtName(uploadFileName);
			
			// 获取连接
			client1 = ConnectionPool.getPoolInstance().checkout(10);
			
			//设置文件元信息  
			NameValuePair[] metaList = new NameValuePair[3];  
			metaList[0] = new NameValuePair("fileName", uploadFileName);  
			metaList[1] = new NameValuePair("fileExtName", fileExtName);  
			metaList[2] = new NameValuePair("fileLength", String.valueOf(len));
			
			// 上传文件
			upPath = client1.upload_file1(file_buff, fileExtName, null);
			
			// 回收连接
			ConnectionPool.getPoolInstance().checkin(client1);
		
		} catch (InterruptedException e){
			e.printStackTrace();
		} catch (Exception e){
			ConnectionPool.getPoolInstance().drop(client1);
			e.printStackTrace();
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return upPath;
	}
	
    // 获取文件后缀名
    private static String getExtName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return null;
    }
	
}