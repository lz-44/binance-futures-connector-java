package com.binance.connector.futures.client;

import com.binance.connector.futures.client.impl.wallet.Asset;
import com.binance.connector.futures.client.impl.wallet.Capital;

public interface WalletClient {
    Capital capital();
    Asset asset();
}