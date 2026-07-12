package com.binance.connector.futures.client.impl;

import com.binance.connector.futures.client.WalletClient;
import com.binance.connector.futures.client.impl.wallet.Asset;
import com.binance.connector.futures.client.impl.wallet.Capital;

public class WalletClientImpl implements WalletClient {
    private final String prodUrl;
    private final String apiKey;
    private final String secretKey;
    private final boolean showLimitUsage = false;

    public WalletClientImpl(String apiKey, String secretKey, String url) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.prodUrl = url + "/sapi";
    }

    @Override
    public Capital capital() {
        return new Capital(prodUrl, apiKey, secretKey, showLimitUsage, null);
    }

    @Override
    public Asset asset() {
        return new Asset(prodUrl, apiKey, secretKey, showLimitUsage, null);
    }
}