package com.binance.connector.futures.client;

import com.binance.connector.futures.client.impl.wallet.Wallet;

public interface WalletClient {
    Wallet wallet();
}