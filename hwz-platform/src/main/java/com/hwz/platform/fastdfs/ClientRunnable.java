package com.hwz.platform.fastdfs;

public class ClientRunnable {
	
    public static void main(String[] args) throws InterruptedException{
    	long start = System.currentTimeMillis();
    	for (int i = 0; i < 100; i++) {
    		MyRunnable myRunnable=new MyRunnable(i+"");  
            Thread thread=new Thread(myRunnable);
            
            
            thread.start();
            
            thread.sleep(10l);
            
		}
    	System.out.println("end=" + (System.currentTimeMillis() - start));
    }
    
}
