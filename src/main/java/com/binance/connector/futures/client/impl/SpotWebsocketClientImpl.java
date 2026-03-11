package com.binance.connector.futures.client.impl;

import com.binance.connector.futures.client.enums.DefaultUrls;
import com.binance.connector.futures.client.utils.ParameterChecker;
import com.binance.connector.futures.client.utils.RequestBuilder;
import com.binance.connector.futures.client.utils.SignatureGenerator;
import com.binance.connector.futures.client.utils.WebSocketCallback;
import com.binance.connector.futures.client.utils.WebSocketConnection;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.Request;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spot Websocket Client implementation.
 * <br>
 * Extends {@link WebsocketClientImpl} with Spot-specific functionality including
 * authenticated User Data Stream subscription via WebSocket API
 * ({@code userDataStream.subscribe.signature}).
 *
 * @see <a href="https://developers.binance.com/docs/binance-spot-api-docs/websocket-api/user-data-stream-requests">
 * Spot User Data Stream Requests</a>
 */
public class SpotWebsocketClientImpl extends WebsocketClientImpl {
    private static final int HTTP_STATUS_OK = 200;
    private static final String SUBSCRIBE_METHOD = "userDataStream.subscribe.signature";
    private static final Logger logger = LoggerFactory.getLogger(SpotWebsocketClientImpl.class);

    private final String wsApiBaseUrl;

    public SpotWebsocketClientImpl(String baseUrl) {
        this(baseUrl, DefaultUrls.SPOT_WS_API_URL, Duration.ZERO);
    }

    public SpotWebsocketClientImpl(String baseUrl, Duration pingInterval) {
        this(baseUrl, DefaultUrls.SPOT_WS_API_URL, pingInterval);
    }

    public SpotWebsocketClientImpl(String baseUrl, String wsApiBaseUrl, Duration pingInterval) {
        super(baseUrl, pingInterval);
        this.wsApiBaseUrl = wsApiBaseUrl;
    }

    /**
     * Subscribe to Spot User Data Stream using {@code userDataStream.subscribe.signature}.
     * <br>
     * Connects to the Binance WebSocket API and authenticates with HMAC-SHA256 signature.
     * Unlike the legacy listenKey approach, no separate REST call is needed.
     *
     * @param apiKey    Binance API key
     * @param secretKey Binance secret key for HMAC-SHA256 signing
     * @param onMessageCallback callback for user data events
     * @return int - Connection ID
     */
    public int subscribeUserDataStream(String apiKey, String secretKey,
            WebSocketCallback onMessageCallback) {
        return subscribeUserDataStream(apiKey, secretKey,
                getNoopCallback(), onMessageCallback,
                getNoopCallback(), getNoopCallback());
    }

    /**
     * Subscribe to Spot User Data Stream using {@code userDataStream.subscribe.signature},
     * with callbacks for all major websocket connection events.
     * <br>
     * The {@code onOpenCallback} is invoked after the subscription is confirmed by the server
     * (not immediately on WebSocket connection open).
     *
     * @param apiKey            Binance API key
     * @param secretKey         Binance secret key for HMAC-SHA256 signing
     * @param onOpenCallback    called when subscription is confirmed
     * @param onMessageCallback called for each user data event
     * @param onClosingCallback called when connection is closing
     * @param onFailureCallback called on connection failure or subscription error
     * @return int - Connection ID
     */
    public int subscribeUserDataStream(
            String apiKey, String secretKey,
            WebSocketCallback onOpenCallback,
            WebSocketCallback onMessageCallback,
            WebSocketCallback onClosingCallback,
            WebSocketCallback onFailureCallback
    ) {
        ParameterChecker.checkParameterType(apiKey, String.class, "apiKey");
        ParameterChecker.checkParameterType(secretKey, String.class, "secretKey");

        Request request = RequestBuilder.buildWebsocketRequest(wsApiBaseUrl);
        String requestId = UUID.randomUUID().toString();
        AtomicReference<WebSocketConnection> connRef = new AtomicReference<>();

        WebSocketCallback wrappedOnOpen = buildSubscribeOnOpen(
                apiKey, secretKey, requestId, connRef);
        WebSocketCallback wrappedOnMessage = buildSubscribeOnMessage(
                requestId, onOpenCallback, onMessageCallback, onFailureCallback);

        WebSocketConnection connection = new WebSocketConnection(
                wrappedOnOpen, wrappedOnMessage, onClosingCallback, onFailureCallback,
                request, getPingInterval()
        );
        connRef.set(connection);
        connection.connect();
        int connectionId = connection.getConnectionId();
        registerConnection(connectionId, connection);
        return connectionId;
    }

    private WebSocketCallback buildSubscribeOnOpen(
            String apiKey, String secretKey, String requestId,
            AtomicReference<WebSocketConnection> connRef
    ) {
        return msg -> {
            long timestamp = System.currentTimeMillis();
            String payload = "apiKey=" + apiKey + "&timestamp=" + timestamp;
            String signature = SignatureGenerator.getSignature(payload, secretKey);

            JSONObject params = new JSONObject();
            params.put("apiKey", apiKey);
            params.put("timestamp", timestamp);
            params.put("signature", signature);

            JSONObject subscribeRequest = new JSONObject();
            subscribeRequest.put("id", requestId);
            subscribeRequest.put("method", SUBSCRIBE_METHOD);
            subscribeRequest.put("params", params);

            logger.info("Sending {} request", SUBSCRIBE_METHOD);
            connRef.get().send(subscribeRequest.toString());
        };
    }

    private WebSocketCallback buildSubscribeOnMessage(
            String requestId,
            WebSocketCallback onOpenCallback,
            WebSocketCallback onMessageCallback,
            WebSocketCallback onFailureCallback
    ) {
        return msg -> {
            if (isSubscriptionResponse(msg, requestId)) {
                handleSubscriptionResponse(
                        msg, onOpenCallback, onFailureCallback);
                return;
            }
            onMessageCallback.onReceive(msg);
        };
    }

    private boolean isSubscriptionResponse(String msg, String requestId) {
        try {
            JSONObject json = new JSONObject(msg);
            return json.has("id")
                    && requestId.equals(json.get("id").toString());
        } catch (JSONException e) {
            return false;
        }
    }

    private void handleSubscriptionResponse(
            String msg,
            WebSocketCallback onOpenCallback,
            WebSocketCallback onFailureCallback
    ) {
        JSONObject json = new JSONObject(msg);
        int status = json.optInt("status");
        if (status == HTTP_STATUS_OK) {
            logger.info("User data stream subscription confirmed");
            onOpenCallback.onReceive(null);
        } else {
            logger.error("User data stream subscription failed: {}", msg);
            onFailureCallback.onReceive("Subscription failed: " + msg);
        }
    }
}
