package com.hwz.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogTool {
	private Logger logger = LoggerFactory.getLogger(LogTool.class);

    public LogTool() {
    }

    public static LogTool getInstance(Object... objects) {
        LogTool log = new LogTool();
        if(null != objects && objects.length > 0 && objects[0] instanceof Class) {
            log.logger = LoggerFactory.getLogger((Class)objects[0]);
        }

        return log;
    }

    public void debug(Object message) {
        this.logger.debug("☆☆☆【" + message + "】☆☆☆");
    }

    public void error(Object message, Object... objects) {
        if(null != objects && objects.length > 0 && objects[0] instanceof Throwable) {
            this.logger.error("◆◆◆【" + message + "】◆◆◆", (Throwable)objects[0]);
        } else {
            this.logger.error("◆◆◆【" + message + "】◆◆◆");
        }

    }

    public void error(Object message) {
        if(message instanceof Throwable) {
            this.logger.error("接口调用报错 ◆◆◆[" + message + "]◆◆◆", (Throwable)message);
        } else {
            this.logger.error("◆◆◆【" + message + "】◆◆◆");
        }

    }

    public void error(Exception message) {
        this.logger.error("◆◆◆【" + message.getMessage() + "】◆◆◆");
    }

    public void info(Object message) {
        this.logger.info("★★★【" + message + "】★★★");
    }

    public void warn(Object message) {
        this.logger.warn("※※※【" + message + "】※※※");
    }

    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }
}
