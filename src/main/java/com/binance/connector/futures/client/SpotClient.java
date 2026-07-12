package com.binance.connector.futures.client;

import com.binance.connector.futures.client.impl.spot.SpotUserData;

public interface SpotClient {
    SpotUserData userData();
}
