package unit.subaccount;

import com.binance.connector.futures.client.enums.HttpMethod;
import com.binance.connector.futures.client.exceptions.BinanceConnectorException;
import com.binance.connector.futures.client.impl.SubAccountClientImpl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;
import unit.MockData;
import unit.MockWebServerDispatcher;
import java.util.LinkedHashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TestSubAccountUniversalTransfer {
    private MockWebServer mockWebServer;
    private String baseUrl;

    @Before
    public void init() {
        this.mockWebServer = new MockWebServer();
        this.baseUrl = mockWebServer.url(MockData.PREFIX).toString();
    }

    @Test
    public void testUniversalTransfer() {
        String path = "sapi/v1/sub-account/universalTransfer";
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(MockData.PREFIX, path, MockData.MOCK_RESPONSE, HttpMethod.POST, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);
        
        SubAccountClientImpl client = new SubAccountClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);

        final double randomAmount = 100d;
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("fromAccountType", "SPOT");
        parameters.put("toAccountType", "USDT_FUTURE");
        parameters.put("asset", "USDT");
        parameters.put("amount", randomAmount);
        
        String result = client.subAccount().universalTransfer(parameters);
        assertEquals(MockData.MOCK_RESPONSE, result);
    }

    @Test
    public void testUniversalTransferWithOptionalParams() {
        String path = "sapi/v1/sub-account/universalTransfer";
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(MockData.PREFIX, path, MockData.MOCK_RESPONSE, HttpMethod.POST, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);
        
        SubAccountClientImpl client = new SubAccountClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);

        final double randomAmount = 0.1d;
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("fromEmail", "from@example.com");
        parameters.put("toEmail", "to@example.com");
        parameters.put("fromAccountType", "SPOT");
        parameters.put("toAccountType", "COIN_FUTURE");
        parameters.put("asset", "BTC");
        parameters.put("amount", randomAmount);
        parameters.put("clientTranId", "transfer123");
        
        String result = client.subAccount().universalTransfer(parameters);
        assertEquals(MockData.MOCK_RESPONSE, result);
    }

    @Test
    public void testUniversalTransferMissingMandatoryParams() {
        SubAccountClientImpl client = new SubAccountClientImpl(MockData.API_KEY, MockData.SECRET_KEY, baseUrl);
        
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("fromAccountType", "SPOT");
        // Missing toAccountType, asset, amount
        
        assertThrows(BinanceConnectorException.class, () -> client.subAccount().universalTransfer(parameters));
    }
}