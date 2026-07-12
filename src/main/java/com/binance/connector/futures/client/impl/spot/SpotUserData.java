package com.binance.connector.futures.client.impl.spot;


import com.binance.connector.futures.client.impl.futures.UserData;
import com.binance.connector.futures.client.utils.ProxyAuth;

public class SpotUserData extends UserData {
    public SpotUserData(String productUrl, String apiKey, boolean showLimitUsage, ProxyAuth proxy) {
        super(productUrl, apiKey, showLimitUsage, proxy, "/v3/userDataStream");
    }
}
