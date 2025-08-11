package com.binance.connector.futures.client.impl.cm_futures;

import com.binance.connector.futures.client.impl.futures.SubAccount;
import com.binance.connector.futures.client.utils.ProxyAuth;

/**
 * <h2>Coin-Margined Sub-Account Endpoints</h2>
 * All endpoints under the
 * <a href="https://developers.binance.com/docs/sub_account/asset-management/Universal-Transfer">Sub-Account Endpoint</a>
 * section of the API documentation will be implemented in this class.
 * <br>
 * Response will be returned in <i>String format</i>.
 */
public class CMSubAccount extends SubAccount {
    public CMSubAccount(String baseUrl, String apiKey, String secretKey, boolean showLimitUsage, ProxyAuth proxy) {
        super(baseUrl, apiKey, secretKey, showLimitUsage, proxy);
    }
}