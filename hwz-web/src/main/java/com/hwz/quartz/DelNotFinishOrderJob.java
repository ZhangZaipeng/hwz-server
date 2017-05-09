package com.hwz.quartz;

import org.springframework.stereotype.Service;

//注解方式  扫描Bean
//定义目标Bean和Bean中的方法
//@Service("delNotFinishOrderJob")
public class DelNotFinishOrderJob {

    private static boolean        isRunning = false;


    public void execute() {
        if (isRunning) return;
        isRunning = true;
        System.out.println("DelNotFinish===========");
        isRunning = false;
    }

}
