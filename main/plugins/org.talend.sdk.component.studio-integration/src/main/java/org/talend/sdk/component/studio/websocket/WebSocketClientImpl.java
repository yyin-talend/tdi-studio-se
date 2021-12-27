package org.talend.sdk.component.studio.websocket;

import static java.util.Locale.ENGLISH;
import static java.util.concurrent.TimeUnit.MINUTES;

import javax.json.bind.Jsonb;
import javax.json.bind.spi.JsonbProvider;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import org.apache.tomcat.websocket.Constants;

public class WebSocketClientImpl implements ServicesClient.WebSocketClient {

    private static final Jsonb jsonb = JsonbProvider.provider("org.apache.johnzon.jsonb.JohnzonProvider").create().build();

    private final WebSocketContainer container;

    private final String protocol;

    private String serverHost;

    private final String port;

    private final String basePath;

    private final long timeout;

    private final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    private Runnable synch;

    public WebSocketClientImpl(String protocol, String port, String basePath, long timeout) {
        this.container = ContainerProvider.getWebSocketContainer();
        this.protocol = protocol;
        this.port = port;
        this.basePath = basePath;
        this.timeout = timeout;
    }

    @Override
    public <T> T sendAndWait(final String id, final String uri, final Object payload, final Class<T> expectedResponse, final boolean doCheck) {
        final Session session = getOrCreateSession(id, doCheck);
        final PayloadHandler handler = new PayloadHandler();
        session.getUserProperties().put("handler", handler);
        final String buildRequest = buildRequest(id, uri, payload);
        try {
            try {
                session.getBasicRemote().sendBinary(ByteBuffer.wrap(buildRequest.getBytes(StandardCharsets.UTF_8)));
            } catch (final IOException e) {
                throw new IllegalStateException(e);
            }
            try {
                handler.latch.await(1, MINUTES); // todo: make it configurable? 1mn is already a lot
            } catch (final InterruptedException e) {
                Thread.interrupted();
                throw new IllegalStateException(e);
            }
        } finally {
            doRelease(session);
        }
        return handler.parseResponse(expectedResponse);
    }

    private void doRelease(final Session session) {
        sessions.add(session);
    }

    private String buildRequest(final String id, final String uri, final Object payload) {
        final String method = id.substring("/v1/".length(), id.indexOf('/', "/v1/".length() + 1));
        return "SEND\r\ndestination:" + uri + "\r\ndestinationMethod:" + method.toUpperCase(ENGLISH) + "\r\nAccept: application/json\r\nContent-Type: application/json\r\n\r\n" + (payload == null ? "" : jsonb.toJson(payload)) + "^@";
    }

    private Session getOrCreateSession(final String id, final boolean doCheck) {
        if (doCheck && synch != null) {
            synchronized (this) {
                if (synch != null) {
                    synch.run();
                    synch = null;
                }
            }
        }
        Session poll = sessions.poll();
        if (poll != null && !poll.isOpen()) {
            try {
                poll.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "Session is no more opened"));
            } catch (final Exception e) {
            }
            // just to go through close cycle but should fail since it is not opened, we
            // just ignore any error
            poll = null;
        }
        if (poll == null) {
            poll = doConnect();
        }
        return poll;
    }

    private Session doConnect() {
        final URI connectUri = URI.create(getBase() + "/bus");
        final ClientEndpointConfig endpointConfig = ClientEndpointConfig.Builder.create().build();
        endpointConfig.getUserProperties().put(Constants.IO_TIMEOUT_MS_PROPERTY, Long.toString(timeout));
        try {
            return container.connectToServer(new Endpoint() {
                @Override
                public void onOpen(final Session session, final EndpointConfig endpointConfig) {
                    session.addMessageHandler(ByteBuffer.class, new MessageHandler.Partial<ByteBuffer>() {
                        @Override
                        public synchronized void onMessage(final ByteBuffer part, final boolean last) {
                            final Consumer<ByteBuffer> callback = Consumer.class.cast(session.getUserProperties().get("handler"));
                            callback.accept(part);
                        }
                    });
                }

                @Override
                public void onError(final Session session, final Throwable throwable) {
                    final PayloadHandler handler = PayloadHandler.class.cast(session.getUserProperties().get("handler"));
                    if (handler != null) {
                        handler.latch.countDown();
                    }
                    throw new IllegalStateException(throwable);
                }
            }, endpointConfig, connectUri);
        } catch (final DeploymentException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String getBase() {
        return protocol + serverHost + ":" + port + basePath;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public void setSynch(Runnable synch) {
        this.synch = synch;
    }

    @Override
    public synchronized void close() {
        sessions.forEach(s -> {
            try {
                s.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "Shutting down the studio"));
            } catch (final IOException e) {
            }
        });
        // no-op: todo: define if we want to log it, we will not do anything anyway at
        // that time
        sessions.clear();
    }
}
