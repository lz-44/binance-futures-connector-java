package com.binance.connector.futures.client.impl.wallet;

import com.binance.connector.futures.client.enums.HttpMethod;
import com.binance.connector.futures.client.utils.ParameterChecker;
import com.binance.connector.futures.client.utils.ProxyAuth;
import com.binance.connector.futures.client.utils.RequestHandler;
import java.util.LinkedHashMap;

/**
 * <h2>Asset Endpoints</h2>
 * Response will be returned in <i>String format</i>.
 */
public class Asset {
    private String productUrl;
    private RequestHandler requestHandler;
    private boolean showLimitUsage;

    public Asset(String productUrl, String apiKey, String secretKey, boolean showLimitUsage, ProxyAuth proxy) {
        this.productUrl = productUrl;
        this.requestHandler = new RequestHandler(apiKey, secretKey, proxy);
        this.showLimitUsage = showLimitUsage;
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

    public void setRequestHandler(String apiKey, String secretKey, ProxyAuth proxy) {
        this.requestHandler = new RequestHandler(apiKey, secretKey, proxy);
    }

    public void setShowLimitUsage(boolean showLimitUsage) {
        this.showLimitUsage = showLimitUsage;
    }

    private final String USER_UNIVERSAL_TRANSFER = "/v1/asset/transfer";
    /**
     * User Universal Transfer
     * <br><br>
     * POST /v1/asset/transfer
     * <br>
     * @param
     * parameters LinkedHashedMap of String,Object pair
     *            where String is the name of the parameter and Object is the value of the parameter
     * <br><br>
     * type -- mandatory/string -- Transfer type: MAIN_UMFUTURE, MAIN_CMFUTURE, MAIN_MARGIN, MAIN_FUNDING, MAIN_OPTION, UMFUTURE_MAIN, UMFUTURE_MARGIN, CMFUTURE_MAIN, CMFUTURE_MARGIN, MARGIN_MAIN, MARGIN_UMFUTURE, MARGIN_CMFUTURE, MARGIN_FUNDING, FUNDING_MAIN, FUNDING_UMFUTURE, FUNDING_CMFUTURE, FUNDING_MARGIN, OPTION_MAIN <br>
     * asset -- mandatory/string -- Asset to transfer <br>
     * amount -- mandatory/decimal -- Transfer amount <br>
     * fromSymbol -- optional/string -- Trading symbol on source account <br>
     * toSymbol -- optional/string -- Trading symbol on destination account <br>
     * recvWindow -- optional/long -- The value cannot be greater than 60000 <br>
     * @return String
     * @see <a href="https://developers.binance.com/docs/wallet/asset/user-universal-transfer">
     *     https://developers.binance.com/docs/wallet/asset/user-universal-transfer</a>
     */
    public String userUniversalTransfer(LinkedHashMap<String, Object> parameters) {
        ParameterChecker.checkParameter(parameters, "type", String.class);
        ParameterChecker.checkParameter(parameters, "asset", String.class);
        ParameterChecker.checkParameter(parameters, "amount", Double.class);
        return requestHandler.sendSignedRequest(productUrl, USER_UNIVERSAL_TRANSFER, parameters, HttpMethod.POST, showLimitUsage);
    }
}