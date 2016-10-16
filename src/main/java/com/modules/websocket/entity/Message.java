package com.modules.websocket.entity;

import java.io.Serializable;
import java.util.Set;

public class Message implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	//消息提示方式：0表示系统提示；1表示用户消息提示
	private String alertType;
	
	//提示方式为0时，将打印系统的消息
	private String alert;
	
	//在线用户
	private Set<String> names;
	
	//提示方式为1时，将打印用户发送的消息
	private String sentMsg;
	
	//发送者
	private String from;
	
	//接收人
	private String accept;
	
	//发送时间
	private String date;
	
	//昵称
	private String nickName;
	
	//是否群发(0:不是；1:是)
	private boolean all;

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public Set<String> getNames() {
		return names;
	}

	public void setNames(Set<String> names) {
		this.names = names;
	}

	public String getSentMsg() {
		return sentMsg;
	}

	public void setSentMsg(String sentMsg) {
		this.sentMsg = sentMsg;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

	public Message() {
		super();
	}
	
}
