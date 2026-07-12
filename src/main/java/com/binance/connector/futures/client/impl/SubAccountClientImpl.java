package com.binance.connector.futures.client.impl;

import com.binance.connector.futures.client.SubAccountClient;
import com.binance.connector.futures.client.impl.subaccount.SubAccount;

public class SubAccountClientImpl implements SubAccountClient {
    private final String prodUrl;
    private final String apiKey;
    private final String secretKey;
    private final boolean showLimitUsage = false;

    public SubAccountClientImpl(String apiKey, String secretKey, String url) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.prodUrl = url + "/sapi";
    }

    @Override
    public SubAccount subAccount() {
        return new SubAccount(prodUrl, apiKey, secretKey, showLimitUsage, null);
    }
}