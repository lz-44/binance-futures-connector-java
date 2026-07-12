package com.binance.connector.futures.client.impl;

import com.binance.connector.futures.client.enums.DefaultUrls;
import com.binance.connector.futures.client.enums.StreamCategory;
import com.binance.connector.futures.client.utils.RequestBuilder;
import com.binance.connector.futures.client.utils.WebSocketCallback;
import com.binance.connector.futures.client.utils.ParameterChecker;
import okhttp3.Request;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * <h2>USDⓈ-M  Websocket Streams</h2>
 * All stream endpoints under the
 * <a href="https://developers.binance.com/docs/derivatives/usds-margined-futures/websocket-market-streams/Connect"> Websocket Market Streams</a> and
 * <a href="https://developers.binance.com/docs/derivatives/usds-margined-futures/user-data-streams/Connect"> User Data Streams</a>
 * section of the API documentation will be implemented in this class.
 * <br>
 * Response will be returned as callback.
 */
public class UMWebsocketClientImpl extends WebsocketClientImpl {

    private static final List<String> DEFAULT_USER_DATA_EVENTS =
            Arrays.asList("ORDER_TRADE_UPDATE", "ACCOUNT_UPDATE");

    public UMWebsocketClientImpl() {
        super(DefaultUrls.USDM_WS_URL, Duration.ZERO);
    }

    public UMWebsocketClientImpl(String baseUrl) {
        super(baseUrl, Duration.ZERO);
    }

    public UMWebsocketClientImpl(String baseUrl, Duration pingInterval) {
        super(baseUrl, pingInterval);
    }

    /**
     * Routes USDⓈ-M Futures streams through the per-category prefix
     * ({@code /public}, {@code /market}, {@code /private}) introduced by Binance
     * on 2026-04-23. The legacy unified path no longer receives data for
     * {@code /market} and {@code /private} channels.
     *
     * @see <a href="https://developers.binance.com/docs/derivatives/usds-margined-futures/websocket-market-streams/Important-WebSocket-Change-Notice">
     * Important WebSocket Change Notice</a>
     */
    @Override
    protected String streamBaseUrl(StreamCategory category) {
        return getBaseUrl() + "/" + category.getPath();
    }

    /**
     * Mark price and funding rate for all symbols pushed every 3 seconds or every second.
     * <br><br>
     * &lt;symbol&gt;@markPrice or &lt;symbol&gt;@markPrice@1s
     * <br><br>
     * Update Speed: 3000ms or 1000ms
     *
     * @param speed speed in seconds, can be 1 or 3
     * @param onMessageCallback onMessageCallback
     * @return int - Connection ID
     * @see <a href="https://developers.binance.com/docs/derivatives/usds-margined-futures/websocket-market-streams/Mark-Price-Stream">
     * https://developers.binance.com/docs/derivatives/usds-margined-futures/websocket-market-streams/Mark-Price-Stream</a>
     */
    public int allMarkPriceStream(int speed, WebSocketCallback onMessageCallback) {
        return allMarkPriceStream(speed, getNoopCallback(), onMessageCallback, getNoopCallback(), getNoopCallback());
    }

    /**
     * Same as {@link #allMarkPriceStream(int, WebSocketCallback)} plus accepts callbacks for all major websocket connection events.
     *
     * @param speed speed in seconds, can be 1 or 3
     * @param onOpenCallback onOpenCallback
     * @param onMessageCallback onMessageCallback
     * @param onClosingCallback onClosingCallback
     * @param onFailureCallback onFailureCallback
     * @return int - Connection ID
     */
    public int allMarkPriceStream(int speed, WebSocketCallback onOpenCallback, WebSocketCallback onMessageCallback, WebSocketCallback onClosingCallback, WebSocketCallback onFailureCallback) {
        Request request = null;
        final int defaultSpeed = 3;
        String url = streamBaseUrl(StreamCategory.MARKET);
        if (speed == defaultSpeed) {
            request = RequestBuilder.buildWebsocketRequest(String.format("%s/ws/!markPrice@arr", url));
        } else {
            request = RequestBuilder.buildWebsocketRequest(String.format("%s/ws/!markPrice@arr@%ss", url, speed));
        }
        return super.createConnection(onOpenCallback, onMessageCallback, onClosingCallback, onFailureCallback, request);
    }

    /**
     * Composite index information for index symbols pushed every second.
     * <br><br>
     * &lt;symbol&gt;@compositeIndex
     * <br><br>
     * Update Speed: 1000ms
     *
     * @param symbol trading symbol
     * @param onMessageCallback onMessageCallback
     * @return int - Connection ID
     * @see <a href="https://developers.binance.com/docs/derivatives/usds-margined-futures/websocket-market-streams/Composite-Index-Symbol-Information-Streams">
     * https://developers.binance.com/docs/derivatives/usds-margined-futures/websocket-market-streams/Composite-Index-Symbol-Information-Streams</a>
     */
    public int compositeIndexSymbolInfo(String symbol, WebSocketCallback onMessageCallback) {
        ParameterChecker.checkParameterType(symbol, String.class, "symbol");
        return compositeIndexSymbolInfo(symbol, getNoopCallback(), onMessageCallback, getNoopCallback(), getNoopCallback());
    }

    /**
     * Same as {@link #compositeIndexSymbolInfo(String, WebSocketCallback)} plus accepts callbacks for all major websocket connection events.
     *
     * @param symbol trading symbol
     * @param onOpenCallback onOpenCallback
     * @param onMessageCallback onMessageCallback
     * @param onClosingCallback onClosingCallback
     * @param onFailureCallback onFailureCallback
     * @return int - Connection ID
     */
    public int compositeIndexSymbolInfo(String symbol, WebSocketCallback onOpenCallback, WebSocketCallback onMessageCallback, WebSocketCallback onClosingCallback, WebSocketCallback onFailureCallback) {
        ParameterChecker.checkParameterType(symbol, String.class, "symbol");
        Request request = RequestBuilder.buildWebsocketRequest(String.format("%s/ws/%s@compositeIndex", streamBaseUrl(StreamCategory.MARKET), symbol.toLowerCase()));
        return createConnection(onOpenCallback, onMessageCallback, onClosingCallback, onFailureCallback, request);
    }

    /**
     * USDⓈ-M user data stream after the 2026-04-23 migration. The listenKey and the
     * subscribed event types are passed as query parameters under {@code /private/ws}:
     * <br><br>
     * {@code wss://fstream.binance.com/private/ws?listenKey=<listenKey>&events=ORDER_TRADE_UPDATE/ACCOUNT_UPDATE}
     * <br><br>
     * Defaults to subscribing both {@code ORDER_TRADE_UPDATE} and {@code ACCOUNT_UPDATE};
     * use {@link #listenUserStream(String, List, WebSocketCallback)} to pick a subset.
     *
     * @see <a href="https://developers.binance.com/docs/derivatives/usds-margined-futures/websocket-market-streams/Important-WebSocket-Change-Notice">
     * Important WebSocket Change Notice</a>
     */
    @Override
    public int listenUserStream(String listenKey, WebSocketCallback onMessageCallback) {
        return listenUserStream(listenKey, DEFAULT_USER_DATA_EVENTS, getNoopCallback(), onMessageCallback, getNoopCallback(), getNoopCallback());
    }

    @Override
    public int listenUserStream(String listenKey, WebSocketCallback onOpenCallback, WebSocketCallback onMessageCallback, WebSocketCallback onClosingCallback, WebSocketCallback onFailureCallback) {
        return listenUserStream(listenKey, DEFAULT_USER_DATA_EVENTS, onOpenCallback, onMessageCallback, onClosingCallback, onFailureCallback);
    }

    /**
     * Subscribes to a specific subset of user data events on the {@code /private/ws} endpoint.
     *
     * @param listenKey listen key returned by the REST {@code /fapi/v1/listenKey} endpoint
     * @param events event types to subscribe to (e.g. {@code ORDER_TRADE_UPDATE}, {@code ACCOUNT_UPDATE})
     * @param onMessageCallback onMessageCallback
     * @return int - Connection ID
     */
    public int listenUserStream(String listenKey, List<String> events, WebSocketCallback onMessageCallback) {
        return listenUserStream(listenKey, events, getNoopCallback(), onMessageCallback, getNoopCallback(), getNoopCallback());
    }

    /**
     * Same as {@link #listenUserStream(String, List, WebSocketCallback)} plus accepts callbacks for all major websocket connection events.
     */
    public int listenUserStream(String listenKey, List<String> events, WebSocketCallback onOpenCallback, WebSocketCallback onMessageCallback, WebSocketCallback onClosingCallback, WebSocketCallback onFailureCallback) {
        ParameterChecker.checkParameterType(listenKey, String.class, "listenKey");
        if (events == null || events.isEmpty()) {
            throw new IllegalArgumentException("events must not be empty");
        }
        String url = String.format("%s/ws?listenKey=%s&events=%s",
                streamBaseUrl(StreamCategory.PRIVATE),
                listenKey,
                String.join("/", events));
        Request request = RequestBuilder.buildWebsocketRequest(url);
        return createConnection(onOpenCallback, onMessageCallback, onClosingCallback, onFailureCallback, request);
    }

}
