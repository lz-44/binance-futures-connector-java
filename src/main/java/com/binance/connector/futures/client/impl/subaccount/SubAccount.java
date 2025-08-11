package com.binance.connector.futures.client.impl.subaccount;

import com.binance.connector.futures.client.enums.HttpMethod;
import com.binance.connector.futures.client.utils.ParameterChecker;
import com.binance.connector.futures.client.utils.ProxyAuth;
import com.binance.connector.futures.client.utils.RequestHandler;
import java.util.LinkedHashMap;

/**
 * <h2>Sub-account Endpoints</h2>
 * Response will be returned in <i>String format</i>.
 */
public class SubAccount {
    private String productUrl;
    private RequestHandler requestHandler;
    private boolean showLimitUsage;

    public SubAccount(String productUrl, String apiKey, String secretKey, boolean showLimitUsage, ProxyAuth proxy) {
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

    private final String UNIVERSAL_TRANSFER = "/v1/sub-account/universalTransfer";
    /**
     * Universal Transfer for Sub-account
     * <br><br>
     * POST /v1/sub-account/universalTransfer
     * <br>
     * @param
     * parameters LinkedHashedMap of String,Object pair
     *            where String is the name of the parameter and Object is the value of the parameter
     * <br><br>
     * fromEmail -- optional/string -- Transfer from email. If not sent, transfer from main account by default. <br>
     * toEmail -- optional/string -- Transfer to email. If not sent, transfer to main account by default. <br>
     * fromAccountType -- mandatory/string -- "SPOT","USDT_FUTURE","COIN_FUTURE","MARGIN"(Cross),"ISOLATED_MARGIN","LEVERAGED","TRX","BTC_MINING","ETH_MINING","LTC_MINING","CASH","POOL","MINING","BSWAP","FIAT","FUNDING" <br>
     * toAccountType -- mandatory/string -- "SPOT","USDT_FUTURE","COIN_FUTURE","MARGIN"(Cross),"ISOLATED_MARGIN","LEVERAGED","TRX","BTC_MINING","ETH_MINING","LTC_MINING","CASH","POOL","MINING","BSWAP","FIAT","FUNDING" <br>
     * asset -- mandatory/string <br>
     * amount -- mandatory/decimal <br>
     * clientTranId -- optional/string -- Must be unique. <br>
     * symbol -- optional/string -- Only supported under ISOLATED_MARGIN type <br>
     * recvWindow -- optional/long <br>
     * @return String
     */
    public String universalTransfer(LinkedHashMap<String, Object> parameters) {
        ParameterChecker.checkParameter(parameters, "fromAccountType", String.class);
        ParameterChecker.checkParameter(parameters, "toAccountType", String.class);
        ParameterChecker.checkParameter(parameters, "asset", String.class);
        ParameterChecker.checkParameter(parameters, "amount", String.class);
        return requestHandler.sendSignedRequest(productUrl, UNIVERSAL_TRANSFER, parameters, HttpMethod.POST, showLimitUsage);
    }
}