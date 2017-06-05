package com.hwz.tools;

import org.aspectj.lang.ProceedingJoinPoint;

public class LoggerServiceAop {
	private static LogTool log = LogTool.getInstance(new Object[]{LoggerDaoAop.class});
    public static final String LOG_SERVICE = "【SERVICE层】>>>";

    public LoggerServiceAop() {
    }

    public Object record(ProceedingJoinPoint pjp) throws Exception {
        Object[] params = pjp.getArgs();
        String mname = "【SERVICE层】>>>" + pjp.getTarget().getClass().getName() + ":" + pjp.getSignature().getName() + "(" + this.objectToStr(params) + ")";
        if(log.isDebugEnabled()) {
            log.debug(mname + "调用【开始】了！");
        }

        Object var11;
        try {
            Object ret = pjp.proceed();
            var11 = ret;
        } catch (Throwable var9) {
            log.error(mname + "调用【报错】了！", new Object[]{var9});
            if(var9 instanceof Exception) {
                Exception se = (Exception)var9;
                throw se;
            }

            throw new Exception(mname + "调用【报错】了！", var9);
        } finally {
            if(log.isDebugEnabled()) {
                log.debug(mname + "调用【结束】了！");
            }

        }

        return var11;
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
