package com.freedomsoft.securemessenger.api;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class StompApi {
    private static final StompApi STOMP_API = new StompApi();

    private StompApi() {
    }

    public static StompApi getInstance() {
        return STOMP_API;
    }

    private StompClient stompClient;

    public static void init() {
        STOMP_API.initApi();
    }

    private void initApi() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://winnersonx.ddns.net/ws/websocket");
    }

    public StompClient getStompClient() {
        return stompClient;
    }
}
