package com.modules.websocket.consts;

public enum AlertType {
	
	topic("系统提示","0"),queue("用户消息","1");
	
	public String type;
	
	public String value;

	private AlertType(String type, String value) {
		this.type = type;
		this.value = value;
	}

}
