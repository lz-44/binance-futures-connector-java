package examples.um_futures.subaccount;

import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import examples.PrivateConfig;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UniversalTransferHistory {
    private UniversalTransferHistory() {
    }

    private static final Logger logger = LoggerFactory.getLogger(UniversalTransferHistory.class);

    public static void main(String[] args) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        // Optional parameters for filtering:
        // parameters.put("fromEmail", "from@example.com");
        // parameters.put("toEmail", "to@example.com");
        // parameters.put("startTime", 1625097600000L);
        // parameters.put("endTime", 1625184000000L);
        // parameters.put("page", 1);
        // parameters.put("limit", 100);

        UMFuturesClientImpl client = new UMFuturesClientImpl(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, PrivateConfig.UM_BASE_URL);
        String result = client.subAccount().universalTransferHistory(parameters);
        logger.info(result);
    }
}