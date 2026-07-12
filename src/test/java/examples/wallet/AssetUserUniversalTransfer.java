package examples.wallet;

import com.binance.connector.futures.client.enums.DefaultUrls;
import com.binance.connector.futures.client.exceptions.BinanceClientException;
import com.binance.connector.futures.client.exceptions.BinanceConnectorException;
import com.binance.connector.futures.client.impl.WalletClientImpl;
import examples.PrivateConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedHashMap;

public final class AssetUserUniversalTransfer {
    private AssetUserUniversalTransfer() {
    }

    private static final Logger logger = LoggerFactory.getLogger(AssetUserUniversalTransfer.class);
    public static void main(String[] args) {

        WalletClientImpl client = new WalletClientImpl(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, DefaultUrls.SPOT_PROD_URL);

        final double transferAmount = 100.0;
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("type", "MAIN_UMFUTURE");
        parameters.put("asset", "USDT");
        parameters.put("amount", transferAmount);

        try {
            String result = client.asset().userUniversalTransfer(parameters);
            logger.info(result);
        } catch (BinanceConnectorException e) {
            logger.error("fullErrMessage: {}", e.getMessage(), e);
        } catch (BinanceClientException e) {
            logger.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                    e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
        }
    }
}