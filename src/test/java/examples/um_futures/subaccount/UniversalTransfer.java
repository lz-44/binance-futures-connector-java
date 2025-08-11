package examples.um_futures.subaccount;

import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import examples.PrivateConfig;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UniversalTransfer {
    private UniversalTransfer() {
    }

    private static final Logger logger = LoggerFactory.getLogger(UniversalTransfer.class);

    public static void main(String[] args) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("fromAccountType", "SPOT");
        parameters.put("toAccountType", "USDT_FUTURE");
        parameters.put("asset", "USDT");
        parameters.put("amount", "100.0");
        // Optional parameters:
        // parameters.put("fromEmail", "from@example.com");
        // parameters.put("toEmail", "to@example.com");
        // parameters.put("clientTranId", "unique-transfer-id-123");

        UMFuturesClientImpl client = new UMFuturesClientImpl(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, PrivateConfig.UM_BASE_URL);
        String result = client.subAccount().universalTransfer(parameters);
        logger.info(result);
    }
}