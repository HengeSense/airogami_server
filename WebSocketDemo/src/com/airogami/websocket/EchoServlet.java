package com.airogami.websocket;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@WebServlet(name = "MyEcho WebSocket Servlet", urlPatterns = { "/echo" })
public class EchoServlet extends WebSocketServlet {
 
	private static final long serialVersionUID = 1L;

	@Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(10000);
        //factory.register(EchoSocket.class);
        factory.setCreator(new MyWebSocketCreator());
    }
}
