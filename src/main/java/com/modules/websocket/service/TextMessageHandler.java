package com.modules.websocket.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.modules.websocket.consts.AlertType;
import com.modules.websocket.consts.Constants;
import com.modules.websocket.entity.Message;

public class TextMessageHandler extends TextWebSocketHandler {

	private static final Map<String, WebSocketSession> users;

	private static Set<String> names;

	private Gson gson = new Gson();

	static {
		users = new HashMap<String, WebSocketSession>();
		names = new HashSet<String>();
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		/*
		 * 链接成功后会触发此方法，可在此处对离线消息什么的进行处理
		 */
		Message msg = new Message();
		String username = (String) session.getAttributes().get(
				Constants.DEFAULT_WEBSOCKET_USERNAME);
		users.put(username, session);
		
		msg.setAlertType(AlertType.topic.value);
		msg.setAlert("【"+username+"】已经上线！");
		names.add(username);
		msg.setNames(names);
		TextMessage message = new TextMessage(gson.toJson(msg));
		sendMessageToUsers(message);
	}

	@Override
	public void handleMessage(WebSocketSession session,
			WebSocketMessage<?> message) throws Exception {
		super.handleMessage(session, message);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session,
			TextMessage message) throws Exception {
		/*
		 * 前端 websocket.send() 会触发此方法
		 */
		System.out.println("message -> " + message.getPayload());
		super.handleTextMessage(session, message);
	}

	@Override
	public void handleTransportError(WebSocketSession session,
			Throwable exception) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		System.err.println(exception.getMessage());
		System.out.println("websocket connection closed......");
		users.remove(session.getId());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus status) throws Exception {
		/*
		 * 链接断开后会触发此方法，可在此处对离线消息什么的进行处理
		 */
		Message msg = new Message();
		String username = (String) session.getAttributes().get(
				Constants.DEFAULT_WEBSOCKET_USERNAME);
		names.remove(username);
		users.remove(username);
		
		msg.setAlertType(AlertType.topic.value);
		msg.setAlert("【"+username+"】已下线！");
		msg.setNames(names);
		TextMessage message = new TextMessage(gson.toJson(msg));
		sendMessageToUsers(message);
		
	}

	/**
	 * 一对一发送消息
	 * 
	 * @param username
	 * @param message
	 */
	public void sendMessageToUser(String username, TextMessage message) {
		Iterator<Map.Entry<String, WebSocketSession>> it = userIterator();
		while (it.hasNext()) {
			WebSocketSession session = it.next().getValue();
			if (username.equals(session.getAttributes().get(
					Constants.DEFAULT_WEBSOCKET_USERNAME))) {
				try {
					if (session.isOpen())
						session.sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 群发消息
	 * 
	 * @param message
	 */
	public void sendMessageToUsers(TextMessage message) {
		Iterator<Map.Entry<String, WebSocketSession>> it = userIterator();
		while (it.hasNext()) {
			WebSocketSession session = it.next().getValue();
			try {
				if (session.isOpen())
					session.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Iterator<Map.Entry<String, WebSocketSession>> userIterator() {
		Set<Map.Entry<String, WebSocketSession>> entrys = users.entrySet();
		return entrys.iterator();
	}
}