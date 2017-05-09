package com.hwz.platform.fastdfs;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;

import com.hwz.platform.Constants;


public class ConnectionPool{
	
	private int size = 50; //最大连接数,可以写配置文件

	private ConcurrentHashMap<StorageClient1, Object> busyConnectionPool = null; //被使用的连接

	private ArrayBlockingQueue<StorageClient1> idleConnectionPool = null; //空闲的连接

	private Object obj = new Object();

	private static ConnectionPool instance = new ConnectionPool();
	
	//单例
	private ConnectionPool(){
		this.busyConnectionPool = new ConcurrentHashMap<StorageClient1, Object>();
		this.idleConnectionPool = new ArrayBlockingQueue<StorageClient1>(this.size);
		init(this.size);
	}

	public static ConnectionPool getPoolInstance(){
		return instance;
	}
  
	// 初始化连接池
	private void init(int size){
		initClientGlobal();
		TrackerServer trackerServer = null;
		try {
			TrackerClient trackerClient = new TrackerClient();
			  
			//只需要一个tracker server连接
			trackerServer = trackerClient.getConnection();
			for (int i = 0; i < size; i++) {
				StorageServer storageServer = null;
				StorageClient1 client1 = new StorageClient1(trackerServer, storageServer);
				
				this.idleConnectionPool.add(client1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (trackerServer != null) {
				try {
					trackerServer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// 取出连接
	public StorageClient1 checkout(int waitTimes) throws InterruptedException{
		
		StorageClient1 storageClient1 = null;
		
		storageClient1 = idleConnectionPool.poll(waitTimes, TimeUnit.SECONDS);
		
		if (null != storageClient1) {          
			busyConnectionPool.put(storageClient1, obj);
			return storageClient1;
		}
		return null;
	}

	// 回收连接
	public void checkin(StorageClient1 client1){
		
		if (this.busyConnectionPool.remove(client1) != null){
			this.idleConnectionPool.add(client1);
		}
			
	}

	// 如果连接无效则抛弃，新建连接来补充到池里
	public void drop(StorageClient1 client1){
		if (this.busyConnectionPool.remove(client1) != null) {
			TrackerServer trackerServer = null;
			try {
				TrackerClient trackerClient = new TrackerClient();
	
				trackerServer = trackerClient.getConnection();
				
				StorageServer storageServer = null;
				
				StorageClient1 newClient1 = new StorageClient1(trackerServer, storageServer);
				
				this.idleConnectionPool.add(newClient1);
			
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (trackerServer != null){
					try {
						trackerServer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	// 初始化
	private void initClientGlobal(){
		InetSocketAddress[] trackerServers = new InetSocketAddress[1];
		
		//trackerServers[0] = new InetSocketAddress("119.29.178.133", 22122);
		trackerServers[0] = new InetSocketAddress(Constants.FASTDFS_ADDRESS, Constants.FASTDFS_PORT);
		//trackerServers[1] = new InetSocketAddress(Constants.FASTDFS_ADDRESS, Constants.FASTDFS_PORT);
		
		//tracker server 集群
		ClientGlobal.setG_tracker_group(new TrackerGroup(trackerServers));
		
		//连接超时时间
		ClientGlobal.setG_connect_timeout(2000);
		//网络超时时间
		ClientGlobal.setG_network_timeout(30000);
		ClientGlobal.setG_anti_steal_token(false);
		// 字符集
		ClientGlobal.setG_charset("UTF-8");
		ClientGlobal.setG_secret_key(null);
	}
}