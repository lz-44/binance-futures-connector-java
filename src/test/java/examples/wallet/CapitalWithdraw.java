package examples.wallet;

import com.binance.connector.futures.client.enums.DefaultUrls;
import com.binance.connector.futures.client.exceptions.BinanceClientException;
import com.binance.connector.futures.client.exceptions.BinanceConnectorException;
import com.binance.connector.futures.client.impl.WalletClientImpl;
import examples.PrivateConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedHashMap;

public final class CapitalWithdraw {
    private CapitalWithdraw() {
    }

    private static final Logger logger = LoggerFactory.getLogger(CapitalWithdraw.class);
    public static void main(String[] args) {

        WalletClientImpl client = new WalletClientImpl(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, DefaultUrls.SPOT_PROD_URL);

        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("coin", "USDT");
        parameters.put("amount", "10.0");
        parameters.put("address", "0x742d35Cc6634C0532925a3b8D5c5f3");
        parameters.put("network", "ETH");
        parameters.put("name", "Withdraw to external wallet");

        try {
            String result = client.capital().capitalWithdraw(parameters);
            logger.info(result);
        } catch (BinanceConnectorException e) {
            logger.error("fullErrMessage: {}", e.getMessage(), e);
        } catch (BinanceClientException e) {
            logger.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                    e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
        }
    }
}