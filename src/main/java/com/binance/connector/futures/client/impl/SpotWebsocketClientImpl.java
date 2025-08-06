package com.binance.connector.futures.client.impl;

import java.time.Duration;

public class SpotWebsocketClientImpl extends WebsocketClientImpl {

    public SpotWebsocketClientImpl(String baseUrl) {
        super(baseUrl, Duration.ZERO);
    }

    public SpotWebsocketClientImpl(String baseUrl, Duration pingInterval) {
        super(baseUrl, pingInterval);
    }
}
