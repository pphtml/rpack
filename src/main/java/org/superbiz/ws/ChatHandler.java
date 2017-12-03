package org.superbiz.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.websocket.WebSocket;
import ratpack.websocket.WebSocketClose;
import ratpack.websocket.WebSocketHandler;
import ratpack.websocket.WebSocketMessage;
import ratpack.websocket.WebSockets;
import rx.Subscription;
import rx.subjects.PublishSubject;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ChatHandler implements Handler {
    private static final Logger logger = Logger.getLogger(ChatHandler.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();

    // List of all currently connected clients
    private final Set<String> clients = new HashSet<>();

    // Subject that all clients subscribe to for events
    private final PublishSubject<String> events = PublishSubject.create();

    // Mapping of client to subscription to the events subject
    private final Map<String, Subscription> subscriptions = new HashMap<>();

    @Override
    public void handle(Context ctx) {
        WebSockets.websocket(ctx, new WebSocketHandler<String>() {

            @Override
            public String onOpen(WebSocket webSocket) throws Exception {
                String client = ctx.getRequest().getQueryParams().get("client");

                logger.info(String.format("Websocket opened for client: %s", client));

                if (client == null || client.isEmpty()) {
                    webSocket.close(500, "Client id is required");
                } else if (clients.contains(client)) {
                    webSocket.close(500, "Client is already connected");
                } else {
                    Map<String, Object> initEvent = new HashMap<>();
                    initEvent.put("type", "init");
                    initEvent.put("client", client);
                    initEvent.put("success", true);
                    initEvent.put("connectedClients", Collections.unmodifiableSet(clients));

                    webSocket.send(mapper.writer().writeValueAsString(initEvent));

                    clients.add(client);

                    Map<String, Object> clientConnectEvent = new HashMap<>();
                    clientConnectEvent.put("type", "clientconnect");
                    clientConnectEvent.put("client", client);

                    events.onNext(mapper.writer().writeValueAsString(clientConnectEvent));

                    subscriptions.put(client, events.subscribe(webSocket::send));

                    logger.info(String.format("Client %s subscribed to event stream", client));
                }

                return null;
            }

            @Override
            public void onClose(WebSocketClose<String> close) throws Exception {
                String client = ctx.getRequest().getQueryParams().get("client");

                clients.remove(client);

                Map<String, Object> event = new HashMap<>();
                event.put("type", "clientdisconnect");
                event.put("client", client);

                events.onNext(mapper.writer().writeValueAsString(event));

                subscriptions.remove(client);

                logger.info(String.format("Websocket closed for client: %s", client));
            }

            @Override
            public void onMessage(WebSocketMessage<String> frame) {
                logger.info(String.format("Message: %s", frame.getText()));
                events.onNext(frame.getText());
            }
        });
    }
}
