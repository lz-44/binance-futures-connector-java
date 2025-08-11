package unit.um_futures.subaccount;

import com.binance.connector.futures.client.enums.HttpMethod;
import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;
import unit.MockData;
import unit.MockWebServerDispatcher;
import java.util.LinkedHashMap;
import static org.junit.Assert.assertEquals;

public class TestUMUniversalTransfer {
    private MockWebServer mockWebServer;
    private String baseUrl;
    
    private static final long START_TIME = 1625097600000L;
    private static final long END_TIME = 1625184000000L;
    private static final int PAGE_NUMBER = 1;
    private static final int LIMIT_SIZE = 100;

    @Before
    public void init() {
        this.mockWebServer = new MockWebServer();
        this.baseUrl = mockWebServer.url(MockData.PREFIX).toString();
    }

    @Test
    public void testUniversalTransfer() {
        String path = "sapi/v1/sub-account/universalTransfer";
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("fromAccountType", "SPOT");
        parameters.put("toAccountType", "USDT_FUTURE");
        parameters.put("asset", "USDT");
        parameters.put("amount", "100.0");
        
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(MockData.PREFIX, path, MockData.MOCK_RESPONSE, HttpMethod.POST, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);

        UMFuturesClientImpl client = new UMFuturesClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);
        String result = client.subAccount().universalTransfer(parameters);
        assertEquals(MockData.MOCK_RESPONSE, result);
    }

    @Test
    public void testUniversalTransferWithOptionalParams() {
        String path = "sapi/v1/sub-account/universalTransfer";
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("fromAccountType", "USDT_FUTURE");
        parameters.put("toAccountType", "SPOT");
        parameters.put("asset", "BTC");
        parameters.put("amount", "0.1");
        parameters.put("fromEmail", "from@example.com");
        parameters.put("toEmail", "to@example.com");
        parameters.put("clientTranId", "test-transfer-123");
        
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(MockData.PREFIX, path, MockData.MOCK_RESPONSE, HttpMethod.POST, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);

        UMFuturesClientImpl client = new UMFuturesClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);
        String result = client.subAccount().universalTransfer(parameters);
        assertEquals(MockData.MOCK_RESPONSE, result);
    }

    @Test
    public void testUniversalTransferHistory() {
        String path = "sapi/v1/sub-account/universalTransfer";
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(MockData.PREFIX, path, MockData.MOCK_RESPONSE, HttpMethod.GET, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);

        UMFuturesClientImpl client = new UMFuturesClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);
        String result = client.subAccount().universalTransferHistory(parameters);
        assertEquals(MockData.MOCK_RESPONSE, result);
    }

    @Test
    public void testUniversalTransferHistoryWithParams() {
        String path = "sapi/v1/sub-account/universalTransfer";
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("fromEmail", "from@example.com");
        parameters.put("toEmail", "to@example.com");
        parameters.put("startTime", START_TIME);
        parameters.put("endTime", END_TIME);
        parameters.put("page", PAGE_NUMBER);
        parameters.put("limit", LIMIT_SIZE);
        
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(MockData.PREFIX, path, MockData.MOCK_RESPONSE, HttpMethod.GET, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);

        UMFuturesClientImpl client = new UMFuturesClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);
        String result = client.subAccount().universalTransferHistory(parameters);
        assertEquals(MockData.MOCK_RESPONSE, result);
    }
}