package order;

import com.hwz.order.service.OrderService;

public class OrderRunnable implements Runnable {
	
	private String name;
	private OrderService orderService;
	public OrderRunnable(){};
	
    public OrderRunnable(String name,OrderService orderService){  
        this.name = name;
        this.orderService = orderService;
    }  
      
    @Override  
    public void run() {
    	try {
    		System.out.println("=======start====="+name+"=======");
    		String a = orderService.getOrderId(OrderService.ModuleType.getValue("OIL"), System.currentTimeMillis());
        	System.out.println(name + "========" + a);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
		}
    	
    } 
}
