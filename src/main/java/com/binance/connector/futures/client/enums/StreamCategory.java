package com.binance.connector.futures.client.enums;

/**
 * Stream endpoint categories introduced by the Binance USDⓈ-M Futures
 * WebSocket migration (effective 2026-04-23). Streams must be routed
 * through the matching category prefix on {@code wss://fstream.binance.com}:
 * {@code /public}, {@code /market}, or {@code /private}. Categories are
 * ignored by markets that have not adopted this split (COIN-M, Spot).
 *
 * @see <a href="https://developers.binance.com/docs/derivatives/usds-margined-futures/websocket-market-streams/Important-WebSocket-Change-Notice">
 * Important WebSocket Change Notice</a>
 */
public enum StreamCategory {
    PUBLIC("public"),
    MARKET("market"),
    PRIVATE("private");

    private final String path;

    StreamCategory(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
