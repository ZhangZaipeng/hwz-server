package order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hwz.order.service.OrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/spring-*.xml"})
public class GetOrderId extends AbstractJUnit4SpringContextTests{
	
    @Autowired
    private OrderService orderService;
    
	@Test
    public void testHello() throws InterruptedException{
		
		long start = System.currentTimeMillis();
    	for (int i = 0; i < 10; i++) {
    		OrderRunnable orderRunnable = new OrderRunnable(i+"",orderService);  
            Thread thread=new Thread(orderRunnable);
            
            thread.start();
            
            thread.sleep(10l);
            
		}
    	
    	System.out.println("end=" + (System.currentTimeMillis() - start));
    	
		
    }
}
