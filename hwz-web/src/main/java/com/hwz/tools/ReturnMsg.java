package com.hwz.tools;

public class ReturnMsg {
	
	public static final int SUCCESS = 1;
	public static final int FAIL = 0;
	
	private int success;		// 成功失败标示  1 = SUCCESS , 0 = FAIL
	private String msgbox;	// 返回消息
	private Object data;	// 返回数据
	
	public ReturnMsg () {}
	
	public ReturnMsg (int success, String msgbox, Object data) {
		this.success = success;
		this.msgbox = msgbox;
		this.data = data; 
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getMsgbox() {
		return msgbox;
	}

	public void setMsgbox(String msgbox) {
		this.msgbox = msgbox;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}

