package com.modules.websocket.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import com.google.gson.Gson;
import com.modules.sys.dto.Result;
import com.modules.sys.entity.User;
import com.modules.websocket.consts.AlertType;
import com.modules.websocket.consts.Constants;
import com.modules.websocket.entity.Message;
import com.modules.websocket.service.TextMessageHandler;

@Controller
@RequestMapping("/chat")
public class MessageCTRL {
	
	private Gson gson = new Gson();
	
	/**
	 * 跳转到聊天页面
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String gotoChat(User user,HttpServletRequest request){
		HttpSession session = request.getSession();
        session.setAttribute(Constants.DEFAULT_SESSION_USERNAME, user.getUsername());
		return "/chat/chatIndex";
	}
	
	/**
	 * 跳转到聊天
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/chat",method=RequestMethod.GET)
	public String Chat(User user,HttpServletRequest request){
		return "/chat/chat";
	}
	
	@Bean
    public TextMessageHandler textMessageHandler() {
        return new TextMessageHandler();
    }

    @RequestMapping
    public String view() {
        return "message";
    }

    @RequestMapping(value = "/send",method = RequestMethod.POST)
    public @ResponseBody Result send(Message msg,User user) {
    	try{
    		if("ALL".equals(msg.getAccept())){
    			msg.setAll(true);
    			msg.setFrom(user.getUsername());
    			msg.setAlertType(AlertType.queue.value);
                msg.setDate(new Date().toLocaleString());
                textMessageHandler().sendMessageToUsers(new TextMessage(gson.toJson(msg)));
                return Result.ok();
    		}
    		msg.setFrom(user.getUsername());
    		msg.setAll(false);
            msg.setAlertType(AlertType.queue.value);
            msg.setDate(new Date().toLocaleString());
            textMessageHandler().sendMessageToUser(msg.getAccept(), new TextMessage(gson.toJson(msg)));
            return Result.ok();
    	}catch(Exception e){
    		e.printStackTrace();
    		return Result.error("消息发送报错！");
    	}
    	
    }

}
