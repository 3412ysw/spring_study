package com.gn.mvc.websocket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class BasicWebScocketHandler extends TextWebSocketHandler{

	private static final List<WebSocketSession> sessionList
		= new ArrayList<WebSocketSession>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 새로운 웹소캣이 연결(open)된 순간 동작
		// System.out.println("서버 : 연결");
		sessionList.add(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// 클라이언트가 서버한테 메세지를 send한 순간
		String payload = message.getPayload();
		System.out.println("서버 : 메시지 받음" + payload);
		
		for(WebSocketSession temp : sessionList) {
			temp.sendMessage(new TextMessage(payload));
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// 웹소캣 연결이 끊겼을 때 
		// System.out.println("연결 끊김");
		sessionList.remove(session);
	}

	
	
}
