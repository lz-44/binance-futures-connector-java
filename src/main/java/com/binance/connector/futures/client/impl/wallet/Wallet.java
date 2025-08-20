package com.binance.connector.futures.client.impl.wallet;

import com.binance.connector.futures.client.enums.HttpMethod;
import com.binance.connector.futures.client.utils.ParameterChecker;
import com.binance.connector.futures.client.utils.ProxyAuth;
import com.binance.connector.futures.client.utils.RequestHandler;
import java.util.LinkedHashMap;

/**
 * <h2>Wallet Endpoints</h2>
 * Response will be returned in <i>String format</i>.
 */
public class Wallet {
    private String productUrl;
    private RequestHandler requestHandler;
    private boolean showLimitUsage;

    public Wallet(String productUrl, String apiKey, String secretKey, boolean showLimitUsage, ProxyAuth proxy) {
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

    private final String CAPITAL_WITHDRAW = "/v1/capital/withdraw/apply";
    /**
     * Submit a withdraw request
     * <br><br>
     * POST /v1/capital/withdraw/apply
     * <br>
     * @param
     * parameters LinkedHashedMap of String,Object pair
     *            where String is the name of the parameter and Object is the value of the parameter
     * <br><br>
     * coin -- mandatory/string -- Coin name <br>
     * amount -- mandatory/decimal -- Withdraw amount <br>
     * address -- mandatory/string -- Withdraw address <br>
     * network -- optional/string -- Network name <br>
     * addressTag -- optional/string -- Address tag <br>
     * name -- optional/string -- Description of the address <br>
     * withdrawOrderId -- optional/string -- Client id for withdraw <br>
     * recvWindow -- optional/long <br>
     * @return String
     * @see <a href="https://developers.binance.com/docs/wallet/capital/withdraw">
     *     https://developers.binance.com/docs/wallet/capital/withdraw</a>
     */
    public String capitalWithdraw(LinkedHashMap<String, Object> parameters) {
        ParameterChecker.checkParameter(parameters, "coin", String.class);
        ParameterChecker.checkParameter(parameters, "amount", Double.class);
        ParameterChecker.checkParameter(parameters, "address", String.class);
        return requestHandler.sendSignedRequest(productUrl, CAPITAL_WITHDRAW, parameters, HttpMethod.POST, showLimitUsage);
    }
}