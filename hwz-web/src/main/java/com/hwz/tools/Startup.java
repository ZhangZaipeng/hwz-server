package com.hwz.tools;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//import com.alibaba.dubbo.container.Main;
/**
 *  System.in.read(); 等待输入 
 * @author xunianchong
 *
 */
public class Startup {
	protected  static final Logger logger = Logger.getLogger(Startup.class);
/*	 private static volatile boolean running = true;*/	
	public static void main(String[] args) throws IOException {
		  ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"config/spring-*.xml","config/dubbo-provider.xml"});
	      context.start();
	      logger.info("application started!");
	      //ImMsgService dubbo =  (ImMsgService)context.getBean("imMsgServiceImpl");
	      //logger.info("application started!===="+JsonUtils.toJSONString(dubbo.selMaxMinMsgBy(1, "101_ys_380", "")));
	      long startTime = System.currentTimeMillis();   //获取开始时间
	      logger.info("start time" + startTime);
	      //logger.info("application started!===="+JsonUtils.toJSONString(dubbo.selDoctorIMMsg(1, "101_yh_1028", "")));
	      long endTime=System.currentTimeMillis(); //获取结束时间
	      logger.info("start time" + endTime);
	      logger.info("耗时 : " + (endTime - startTime) + "毫秒");
	      
	     /* Runtime.getRuntime().addShutdownHook(new Thread() {
              public void run() {
            	  running = false;
            	  logger.info("application  destroyed!");
              }
          });
	      synchronized (Startup.class) {
	            while (running) {
	                try {
	                	Startup.class.wait();
	                } catch (Throwable e) {
	                }
	            }
	       }*/
	       //System.exit(1); 
	       //Main.main(args);
	}
}
