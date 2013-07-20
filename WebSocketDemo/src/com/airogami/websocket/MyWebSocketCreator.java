package com.airogami.websocket;

import javax.servlet.http.HttpSession;

import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
 
public class MyWebSocketCreator implements WebSocketCreator {
 
    private EchoSocket binaryEcho;
 
    private EchoSocket textEcho;
 
    public MyWebSocketCreator() {
        this.binaryEcho = new EchoSocket();
        this.textEcho = new EchoSocket();
    }
 
    @Override
    public Object createWebSocket(UpgradeRequest req, UpgradeResponse resp) {
    	
    	HttpSession session = (HttpSession)req.getSession();
    	System.out.println(session.getId());
       /* for (String subprotocol : req.getSubProtocols()) {
            if ("binary".equals(subprotocol)) {
                resp.setAcceptedSubProtocol(subprotocol);
                return binaryEcho;
            }
            if ("text".equals(subprotocol)) {
                resp.setAcceptedSubProtocol(subprotocol);
                return textEcho;
            }
        }*/
        return textEcho;
    }
}