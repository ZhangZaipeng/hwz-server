package com.hwz.tools;

import org.aspectj.lang.ProceedingJoinPoint;

public class LoggerDaoAop {
	private static LogTool log = LogTool.getInstance(new Object[]{LoggerDaoAop.class});
    public static final String LOG_DAO = "【DAO层】>>>";

    public LoggerDaoAop() {
    }

    public Object record(ProceedingJoinPoint pjp) throws Exception {
        Object[] params = pjp.getArgs();
        String mname = "【DAO层】>>>" + pjp.getTarget().getClass().getName() + ":" + pjp.getSignature().getName() + "(" + this.objectToStr(params) + ")";
        if(log.isDebugEnabled()) {
            log.debug(mname + "调用【开始】了！");
        }

        Object var5;
        try {
            Object ret = pjp.proceed();
            var5 = ret;
        } catch (Throwable var9) {
            log.error(mname + "调用【报错】了！", new Object[]{var9});
            throw new Exception(mname + "调用【报错】了！", var9);
        } finally {
            if(log.isDebugEnabled()) {
                log.debug(mname + "调用【结束】了！");
            }

        }

        return var5;
    }

    public String objectToStr(Object[] params) {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        Object[] arr$ = params;
        int len$ = params.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Object object = arr$[i$];
            if(null != object) {
                if(i > 0) {
                    sb.append(",");
                }

                sb.append(object.toString());
                ++i;
            }
        }

        return sb.toString();
    }
}
