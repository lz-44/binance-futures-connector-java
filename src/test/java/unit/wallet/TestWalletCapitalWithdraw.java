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

public class TestWalletCapitalWithdraw {
    private MockWebServer mockWebServer;
    private String baseUrl;

    @Before
    public void init() {
        this.mockWebServer = new MockWebServer();
        this.baseUrl = mockWebServer.url(MockData.PREFIX).toString();
    }

    @Test
    public void testCapitalWithdraw() {
        String path = "sapi/v1/capital/withdraw/apply";
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(MockData.PREFIX, path, MockData.MOCK_RESPONSE, HttpMethod.POST, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);
        
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);

        final double randomAmount = 0.01d;
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("coin", "BTC");
        parameters.put("amount", randomAmount);
        parameters.put("address", "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2");
        
        String result = client.capital().capitalWithdraw(parameters);
        assertEquals(MockData.MOCK_RESPONSE, result);
    }

    @Test
    public void testCapitalWithdrawWithOptionalParams() {
        String path = "sapi/v1/capital/withdraw/apply";
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(MockData.PREFIX, path, MockData.MOCK_RESPONSE, HttpMethod.POST, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);
        
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);

        final double randomAmount = 1.5d;
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("coin", "USDT");
        parameters.put("amount", randomAmount);
        parameters.put("address", "0x742d35Cc6634C0532925a3b8D5c5f3");
        parameters.put("network", "ETH");
        parameters.put("name", "Test withdrawal");
        parameters.put("withdrawOrderId", "withdraw123");
        
        String result = client.capital().capitalWithdraw(parameters);
        assertEquals(MockData.MOCK_RESPONSE, result);
    }

    @Test
    public void testCapitalWithdrawMissingMandatoryParams() {
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);
        
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("coin", "BTC");
        // Missing amount and address
        
        assertThrows(BinanceConnectorException.class, () -> client.capital().capitalWithdraw(parameters));
    }

    @Test
    public void testCapitalWithdrawMissingCoin() {
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);
        
        final double testAmount = 0.01d;
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("amount", testAmount);
        parameters.put("address", "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2");
        // Missing coin
        
        assertThrows(BinanceConnectorException.class, () -> client.capital().capitalWithdraw(parameters));
    }

    @Test
    public void testCapitalWithdrawMissingAmount() {
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);
        
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("coin", "BTC");
        parameters.put("address", "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2");
        // Missing amount
        
        assertThrows(BinanceConnectorException.class, () -> client.capital().capitalWithdraw(parameters));
    }

    @Test
    public void testCapitalWithdrawMissingAddress() {
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);
        
        final double testAmount = 0.01d;
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("coin", "BTC");
        parameters.put("amount", testAmount);
        // Missing address
        
        assertThrows(BinanceConnectorException.class, () -> client.capital().capitalWithdraw(parameters));
    }

    @Test
    public void testGetAllCoinsInfo() {
        String path = "sapi/v1/capital/config/getall";
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(MockData.PREFIX, path, MockData.MOCK_RESPONSE, HttpMethod.GET, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);
        
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);

        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        
        String result = client.capital().getAllCoinsInfo(parameters);
        assertEquals(MockData.MOCK_RESPONSE, result);
    }

    @Test
    public void testGetAllCoinsInfoWithRecvWindow() {
        String path = "sapi/v1/capital/config/getall";
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(MockData.PREFIX, path, MockData.MOCK_RESPONSE, HttpMethod.GET, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);
        
        WalletClientImpl client = new WalletClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);

        final long recvWindow = 5000L;
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("recvWindow", recvWindow);
        
        String result = client.capital().getAllCoinsInfo(parameters);
        assertEquals(MockData.MOCK_RESPONSE, result);
    }
}