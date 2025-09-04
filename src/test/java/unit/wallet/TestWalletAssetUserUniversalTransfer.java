package unit.wallet;

import com.binance.connector.futures.client.enums.HttpMethod;
import com.binance.connector.futures.client.exceptions.BinanceConnectorException;
import com.binance.connector.futures.client.impl.WalletClientImpl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;
import unit.MockData;
import unit.MockWebServerDispatcher;
import java.util.LinkedHashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TestWalletAssetUserUniversalTransfer {
    private MockWebServer mockWebServer;
    private String baseUrl;

    @Before
    public void init() {
        this.mockWebServer = new MockWebServer();
        this.baseUrl = mockWebServer.url(MockData.PREFIX).toString();
    }

    @Test
    public void testUserUniversalTransfer() {
        String path = "sapi/v1/asset/transfer";
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(MockData.PREFIX, path, MockData.MOCK_RESPONSE, HttpMethod.POST, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);
        
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);

        final double randomAmount = 100d;
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("type", "MAIN_UMFUTURE");
        parameters.put("asset", "USDT");
        parameters.put("amount", randomAmount);
        
        String result = client.asset().userUniversalTransfer(parameters);
        assertEquals(MockData.MOCK_RESPONSE, result);
    }

    @Test
    public void testUserUniversalTransferWithOptionalParams() {
        String path = "sapi/v1/asset/transfer";
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(MockData.PREFIX, path, MockData.MOCK_RESPONSE, HttpMethod.POST, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);
        
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);

        final double randomAmount = 50.5d;
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("type", "MAIN_MARGIN");
        parameters.put("asset", "BTC");
        parameters.put("amount", randomAmount);
        parameters.put("fromSymbol", "BTCUSDT");
        parameters.put("toSymbol", "BTCBUSD");
        
        String result = client.asset().userUniversalTransfer(parameters);
        assertEquals(MockData.MOCK_RESPONSE, result);
    }

    @Test
    public void testUserUniversalTransferMissingMandatoryParams() {
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);
        
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("type", "MAIN_UMFUTURE");
        // Missing asset and amount
        
        assertThrows(BinanceConnectorException.class, () -> client.asset().userUniversalTransfer(parameters));
    }

    @Test
    public void testUserUniversalTransferMissingType() {
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);
        
        final double testAmount = 100d;
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("asset", "USDT");
        parameters.put("amount", testAmount);
        // Missing type
        
        assertThrows(BinanceConnectorException.class, () -> client.asset().userUniversalTransfer(parameters));
    }

    @Test
    public void testUserUniversalTransferMissingAsset() {
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);
        
        final double testAmount = 100d;
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("type", "MAIN_UMFUTURE");
        parameters.put("amount", testAmount);
        // Missing asset
        
        assertThrows(BinanceConnectorException.class, () -> client.asset().userUniversalTransfer(parameters));
    }

    @Test
    public void testUserUniversalTransferMissingAmount() {
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);
        
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("type", "MAIN_UMFUTURE");
        parameters.put("asset", "USDT");
        // Missing amount
        
        assertThrows(BinanceConnectorException.class, () -> client.asset().userUniversalTransfer(parameters));
    }

    @Test
    public void testUserUniversalTransferFromUmFutureToMain() {
        String path = "sapi/v1/asset/transfer";
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(MockData.PREFIX, path, MockData.MOCK_RESPONSE, HttpMethod.POST, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);
        
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);

        final double randomAmount = 25.75d;
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("type", "UMFUTURE_MAIN");
        parameters.put("asset", "BTC");
        parameters.put("amount", randomAmount);
        
        String result = client.asset().userUniversalTransfer(parameters);
        assertEquals(MockData.MOCK_RESPONSE, result);
    }

    @Test
    public void testUserUniversalTransferFromMainToCmFuture() {
        String path = "sapi/v1/asset/transfer";
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(MockData.PREFIX, path, MockData.MOCK_RESPONSE, HttpMethod.POST, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);
        
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);

        final double randomAmount = 1000d;
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("type", "MAIN_CMFUTURE");
        parameters.put("asset", "BTC");
        parameters.put("amount", randomAmount);
        
        String result = client.asset().userUniversalTransfer(parameters);
        assertEquals(MockData.MOCK_RESPONSE, result);
    }
}