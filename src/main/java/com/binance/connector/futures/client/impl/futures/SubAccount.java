package com.binance.connector.futures.client.impl.futures;

import com.binance.connector.futures.client.enums.HttpMethod;
import com.binance.connector.futures.client.utils.ParameterChecker;
import com.binance.connector.futures.client.utils.ProxyAuth;
import com.binance.connector.futures.client.utils.RequestHandler;
import java.util.LinkedHashMap;

/**
 * <h2>Sub-Account Endpoints</h2>
 * Response will be returned in <i>String format</i>.
 */
public abstract class SubAccount {
    private String baseUrl;
    private RequestHandler requestHandler;
    private boolean showLimitUsage;

    public SubAccount(String baseUrl, String apiKey, String secretKey, boolean showLimitUsage, ProxyAuth proxy) {
        this.baseUrl = baseUrl;
        this.requestHandler = new RequestHandler(apiKey, secretKey, proxy);
        this.showLimitUsage = showLimitUsage;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public RequestHandler getRequestHandler() {
        return this.requestHandler;
    }

    public boolean getShowLimitUsage() {
        return this.showLimitUsage;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setRequestHandler(String apiKey, String secretKey, ProxyAuth proxy) {
        this.requestHandler = new RequestHandler(apiKey, secretKey, proxy);
    }

    public void setShowLimitUsage(boolean showLimitUsage) {
        this.showLimitUsage = showLimitUsage;
    }

    private final String UNIVERSAL_TRANSFER = "/sapi/v1/sub-account/universalTransfer";
    
    /**
     * Universal Transfer (For Master Account)
     * <br><br>
     * POST /sapi/v1/sub-account/universalTransfer
     * <br>
     * @param
     * parameters LinkedHashedMap of String,Object pair
     *            where String is the name of the parameter and Object is the value of the parameter
     * <br><br>
     * fromAccountType -- mandatory/string -- Sending account type: SPOT, USDT_FUTURE, COIN_FUTURE <br>
     * toAccountType -- mandatory/string -- Receiving account type: SPOT, USDT_FUTURE, COIN_FUTURE <br>
     * asset -- mandatory/string <br>
     * amount -- mandatory/decimal <br>
     * fromEmail -- optional/string <br>
     * toEmail -- optional/string <br>
     * clientTranId -- optional/string -- Must be unique <br>
     * symbol -- optional/string -- Only supported under PORTFOLIO_MARGIN account <br>
     * recvWindow -- optional/long <br>
     * @return String
     * @see <a href="https://developers.binance.com/docs/sub_account/asset-management/Universal-Transfer">
     *     https://developers.binance.com/docs/sub_account/asset-management/Universal-Transfer</a>
     */
    public String universalTransfer(LinkedHashMap<String, Object> parameters) {
        ParameterChecker.checkParameter(parameters, "fromAccountType", String.class);
        ParameterChecker.checkParameter(parameters, "toAccountType", String.class);
        ParameterChecker.checkParameter(parameters, "asset", String.class);
        ParameterChecker.checkParameter(parameters, "amount", String.class);
        return requestHandler.sendSignedRequest(baseUrl, UNIVERSAL_TRANSFER, parameters, HttpMethod.POST, showLimitUsage);
    }

    private final String UNIVERSAL_TRANSFER_HISTORY = "/sapi/v1/sub-account/universalTransfer";
    
    /**
     * Universal Transfer History (For Master Account)
     * <br><br>
     * GET /sapi/v1/sub-account/universalTransfer
     * <br>
     * @param
     * parameters LinkedHashedMap of String,Object pair
     *            where String is the name of the parameter and Object is the value of the parameter
     * <br><br>
     * fromEmail -- optional/string <br>
     * toEmail -- optional/string <br>
     * clientTranId -- optional/string <br>
     * startTime -- optional/long <br>
     * endTime -- optional/long <br>
     * page -- optional/int -- Default 1 <br>
     * limit -- optional/int -- Default 500, Max 500 <br>
     * recvWindow -- optional/long <br>
     * @return String
     * @see <a href="https://developers.binance.com/docs/sub_account/asset-management/Universal-Transfer">
     *     https://developers.binance.com/docs/sub_account/asset-management/Universal-Transfer</a>
     */
    public String universalTransferHistory(LinkedHashMap<String, Object> parameters) {
        return requestHandler.sendSignedRequest(baseUrl, UNIVERSAL_TRANSFER_HISTORY, parameters, HttpMethod.GET, showLimitUsage);
    }
}