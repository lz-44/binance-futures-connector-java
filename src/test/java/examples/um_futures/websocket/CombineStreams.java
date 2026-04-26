package examples.um_futures.websocket;

import com.binance.connector.futures.client.enums.StreamCategory;
import com.binance.connector.futures.client.impl.UMWebsocketClientImpl;
import java.util.ArrayList;

public final class CombineStreams {
    private CombineStreams() {
    }

    public static void main(String[] args) {
        UMWebsocketClientImpl client = new UMWebsocketClientImpl();
        ArrayList<String> streams = new ArrayList<>();
        streams.add("btcusdt@aggTrade");
        streams.add("bnbusdt@aggTrade");

        client.combineStreams(StreamCategory.MARKET, streams, ((event) -> {
            System.out.println(event);
        }));

        ArrayList<String> bookStreams = new ArrayList<>();
        bookStreams.add("btcusdt@bookTicker");
        bookStreams.add("bnbusdt@bookTicker");

        client.combineStreams(StreamCategory.PUBLIC, bookStreams, ((event) -> {
            System.out.println(event);
        }));

        client.closeAllConnections();
    }
}
