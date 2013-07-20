package com.airogami.websocket;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class EchoSocket extends WebSocketAdapter {
	 
    @Override
    public void onWebSocketText(String message) {
        if (isConnected()) {
            try {
                System.out.printf("Echoing back message [%s]%n", message);
                getRemote().sendString(message);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
