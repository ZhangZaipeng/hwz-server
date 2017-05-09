package com.hwz.platform.fastdfs;

import org.csource.fastdfs.StorageClient1;

public class MyRunnable implements Runnable {
	private String name; 
	public MyRunnable(){};
	
    public MyRunnable(String name){  
        this.name = name;
    }  
      
    @Override  
    public void run() {
    	
    	StorageClient1 client1 = null;
    	try {
    		
    		client1 = ConnectionPool.getPoolInstance().checkout(1);
    		long start = System.currentTimeMillis();
    		System.out.println("Runnable"+name+"-->client1+start" + start);
    		Thread.sleep(5000l);
    		long end = System.currentTimeMillis();
    		System.out.println("Runnable"+name+"-->client1+end" + end);
    		ConnectionPool.getPoolInstance().checkin(client1);
		} catch (Exception e) {
			ConnectionPool.getPoolInstance().drop(client1);
			e.printStackTrace();
		}finally {
		}
    	
    } 
}
