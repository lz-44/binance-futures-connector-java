package com.binance.connector.futures.client.impl;

import com.binance.connector.futures.client.SpotClient;
import com.binance.connector.futures.client.impl.spot.SpotUserData;

public class SpotClientImpl implements SpotClient {
    private final String prodUrl;
    private final String apiKey;
    private final String secretKey;
    private final boolean showLimitUsage = false;

    public SpotClientImpl(String apiKey, String secretKey, String url) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.prodUrl = url + "/api";
    }

    @Override
    public SpotUserData userData() {
        return new SpotUserData(prodUrl, apiKey, showLimitUsage, null);
    }
}

