package com.binance.connector.futures.client.impl.futures;

import com.binance.connector.futures.client.enums.HttpMethod;
import com.binance.connector.futures.client.utils.ProxyAuth;
import com.binance.connector.futures.client.utils.RequestHandler;
import java.util.LinkedHashMap;

/**
 * <h2>User Data Streams Endpoints</h2>
 * Response will be returned in <i>String format</i>.
 */
public abstract class UserData {
    private final String listenKey;

    private String productUrl;
    private RequestHandler requestHandler;
    private boolean showLimitUsage;

    public UserData(String productUrl, String apiKey, boolean showLimitUsage, ProxyAuth proxy, String listenKey) {
        this.productUrl = productUrl;
        this.requestHandler = new RequestHandler(apiKey, proxy);
        this.showLimitUsage = showLimitUsage;
        this.listenKey = listenKey;
    }

    public String getProductUrl() {
        return this.productUrl;
    }

    public RequestHandler getRequestHandler() {
        return this.requestHandler;
    }

    public boolean getShowLimitUsage() {
        return this.showLimitUsage;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public void setRequestHandler(String apiKey, ProxyAuth proxy) {
        this.requestHandler = new RequestHandler(apiKey, proxy);
    }

    public void setShowLimitUsage(boolean showLimitUsage) {
        this.showLimitUsage = showLimitUsage;
    }
    /**
     * Start a new user data stream. The stream will close after 60 minutes unless a keepalive is sent.
     * If the account has an active listenKey, that listenKey will be returned and its validity will be extended for 60 minutes.
     * <br><br>
     * POST /v1/listenKey
     * <br>
     * @return String
     * @see <a href="https://developers.binance.com/docs/derivatives/usds-margined-futures/user-data-streams/Start-User-Data-Stream">
     *     https://developers.binance.com/docs/derivatives/usds-margined-futures/user-data-streams/Start-User-Data-Stream</a>
     */
    public String createListenKey() {
        return requestHandler.sendWithApiKeyRequest(productUrl, listenKey, null, HttpMethod.POST, showLimitUsage);
    }

    /**
     * Keepalive a user data stream to prevent a time out. User data streams will close after 60 minutes.
     * It's recommended to send a ping about every 60 minutes.
     * <br><br>
     * PUT /v1/listenKey
     * <br>
     * @return String
     * @see <a href="https://developers.binance.com/docs/derivatives/usds-margined-futures/user-data-streams/Keepalive-User-Data-Stream">
     *     https://developers.binance.com/docs/derivatives/usds-margined-futures/user-data-streams/Keepalive-User-Data-Stream</a>
     */
    public String extendListenKey() {
        return requestHandler.sendWithApiKeyRequest(productUrl, listenKey, null, HttpMethod.PUT, showLimitUsage);
    }

    // Suitable for /v3/userDataStream endpoint (Spot User Data Stream)
    public String extendListenKey(String listenKeyToExtend) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("listenKey", listenKeyToExtend);
        return requestHandler.sendWithApiKeyRequest(productUrl, listenKey, parameters, HttpMethod.PUT, showLimitUsage);
    }

    /**
     * Close out a user data stream.
     * <br><br>
     * DELETE /v1/listenKey
     * <br>
     * @return String
     * @see <a href="https://developers.binance.com/docs/derivatives/usds-margined-futures/user-data-streams/Close-User-Data-Stream">
     *     https://developers.binance.com/docs/derivatives/usds-margined-futures/user-data-streams/Close-User-Data-Stream</a>
     */
    public String closeListenKey() {
        return requestHandler.sendWithApiKeyRequest(productUrl, listenKey, null, HttpMethod.DELETE, showLimitUsage);
    }
}