# GitHub Copilot Instructions for Binance Futures Connector Java

## Project Overview

This repository contains a **deprecated** Java library that provides connectivity to Binance Futures API. It is a lightweight connector that supports:

- USD-M Futures (`/fapi/*` endpoints)
- COIN-M Futures (`/dapi/*` endpoints) 
- USD-M and COIN-M Futures WebSocket Market Streams
- USD-M and COIN-M Futures User Data Streams
- Portfolio Margin endpoints

**Important**: This repository is deprecated. New development should use [binance-connector-java](https://github.com/binance/binance-connector-java).

## Technical Stack

- **Language**: Java 8
- **Build Tool**: Maven
- **HTTP Client**: OkHttp 4.9.2
- **WebSocket**: OkHttp WebSocket implementation
- **Logging**: SLF4J with Logback
- **Testing**: JUnit 4.13.2 with MockWebServer
- **Code Style**: Checkstyle with custom configuration
- **JSON**: org.json library
- **Cryptography**: Apache Commons Codec for HMAC signatures

## Project Structure

```
src/main/java/com/binance/connector/futures/
├── client/                          # Main client interfaces
│   ├── FuturesClient.java          # Base futures client interface
│   ├── SpotClient.java             # Spot client interface
│   ├── WebsocketClient.java        # WebSocket client interface
│   ├── impl/                       # Client implementations
│   │   ├── UMFuturesClientImpl.java    # USD-M Futures client
│   │   ├── CMFuturesClientImpl.java    # COIN-M Futures client
│   │   ├── SpotClientImpl.java         # Spot client implementation
│   │   ├── UMWebsocketClientImpl.java  # USD-M WebSocket client
│   │   ├── CMWebsocketClientImpl.java  # COIN-M WebSocket client
│   │   ├── SpotWebsocketClientImpl.java # Spot WebSocket client
│   │   ├── um_futures/             # USD-M specific implementations
│   │   ├── cm_futures/             # COIN-M specific implementations
│   │   └── spot/                   # Spot specific implementations
│   ├── enums/                      # Enumerations and constants
│   ├── exceptions/                 # Custom exception classes
│   └── utils/                      # Utility classes

src/test/java/
├── examples/                       # Usage examples (not actual tests)
│   ├── um_futures/                 # USD-M examples
│   ├── cm_futures/                 # COIN-M examples
│   └── PrivateConfig.java          # Configuration for examples
└── unit/                          # Actual unit tests with MockWebServer
```

## Coding Standards and Patterns

### Naming Conventions

- **Classes**: PascalCase (e.g., `UMFuturesClientImpl`, `TestCMPing`)
- **Methods**: camelCase (e.g., `exchangeInfo()`, `testNewOrder()`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `API_KEY`, `SECRET_KEY`, `UM_BASE_URL`)
- **Packages**: lowercase with underscores for multi-word (e.g., `um_futures`, `cm_futures`)

### Client Implementation Pattern

All client implementations follow this pattern:

```java
public class UMFuturesClientImpl extends FuturesClientImpl {
    private static String defaultBaseUrl = DefaultUrls.USDM_PROD_URL;
    private static String umProduct = "/fapi";

    // Multiple constructors for flexibility
    public UMFuturesClientImpl() {
        super(defaultBaseUrl, umProduct);
    }

    public UMFuturesClientImpl(String apiKey, String secretKey) {
        super(apiKey, secretKey, defaultBaseUrl, umProduct);
    }

    // Override interface methods to return specific implementations
    @Override
    public UMMarket market() {
        return new UMMarket(getProductUrl(), getBaseUrl(), getApiKey(), getShowLimitUsage(), getProxy());
    }
}
```

### Parameter Handling Pattern

API parameters are consistently handled using `LinkedHashMap<String, Object>`:

```java
LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
parameters.put("symbol", "BTCUSDT");
parameters.put("side", "SELL");
parameters.put("type", "LIMIT");
parameters.put("timeInForce", "GTC");
parameters.put("quantity", 0.01);
parameters.put("price", 9500);
```

### Exception Handling Pattern

The library uses three types of exceptions:

```java
try {
    String result = client.trade().newOrder(parameters);
    logger.info(result);
} catch (BinanceConnectorException e) {
    // Parameter validation errors (before API call)
    logger.error("fullErrMessage: {}", e.getMessage(), e);
} catch (BinanceClientException e) {
    // 4XX HTTP errors (client-side issues)
    logger.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
        e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
} catch (BinanceServerException e) {
    // 5XX HTTP errors (server-side issues)
    logger.error("Server error: {}", e.getMessage(), e);
}
```

## Testing Patterns

### Unit Test Structure

All unit tests follow this pattern using MockWebServer:

```java
public class TestUMPing {
    private MockWebServer mockWebServer;
    private String baseUrl;

    @Before
    public void init() {
        this.mockWebServer = new MockWebServer();
        this.baseUrl = mockWebServer.url(MockData.PREFIX).toString();
    }

    @Test
    public void testPing() {
        String path = "fapi/v1/ping";
        Dispatcher dispatcher = MockWebServerDispatcher.getDispatcher(
            MockData.PREFIX, path, MockData.MOCK_RESPONSE, 
            HttpMethod.GET, MockData.HTTP_STATUS_OK);
        mockWebServer.setDispatcher(dispatcher);
        
        UMFuturesClientImpl client = new UMFuturesClientImpl(baseUrl);
        String result = client.market().ping();
        assertEquals(MockData.MOCK_RESPONSE, result);
    }
}
```

### Example Structure

Examples are provided in `src/test/java/examples/` but are not actual tests. They demonstrate real API usage:

```java
public final class Ping {
    private static final Logger logger = LoggerFactory.getLogger(Ping.class);
    
    public static void main(String[] args) {
        UMFuturesClientImpl client = new UMFuturesClientImpl();
        
        try {
            String result = client.market().ping();
            logger.info(result);
        } catch (BinanceConnectorException | BinanceClientException e) {
            logger.error("Error: {}", e.getMessage(), e);
        }
    }
}
```

## API Organization

The API is organized into functional categories:

### Market Data (Public endpoints)
- `client.market().ping()` - Test connectivity
- `client.market().exchangeInfo()` - Exchange information
- `client.market().depth()` - Order book
- `client.market().klines()` - Candlestick data

### Account/Trading (Private endpoints)
- `client.account().accountInfo()` - Account information
- `client.account().newOrder()` - Place new order
- `client.account().cancelOrder()` - Cancel order
- `client.account().positionInfo()` - Position information

### User Data Streams
- `client.userData().createListenKey()` - Create listen key
- `client.userData().extendListenKey()` - Extend listen key  
- `client.userData().closeListenKey()` - Close listen key

### WebSocket Streams
```java
UMWebsocketClientImpl client = new UMWebsocketClientImpl();
int streamID = client.aggTradeStream("btcusdt", (event) -> {
    System.out.println(event);
});
```

## Key Differences: USD-M vs COIN-M Futures vs Spot

- **USD-M Futures** (`UMFuturesClientImpl`): Uses `/fapi` endpoints, settles in USDT/BUSD
- **COIN-M Futures** (`CMFuturesClientImpl`): Uses `/dapi` endpoints, settles in cryptocurrency
- **Spot** (`SpotClientImpl`): Uses `/api` endpoints for spot trading user data streams

All follow identical patterns but with different base URLs and endpoint prefixes. The Spot client primarily provides user data stream functionality for spot trading integration.

## Configuration and Environment

### Base URLs
- **Production USD-M**: `https://fapi.binance.com`
- **Production COIN-M**: `https://dapi.binance.com`
- **Production Spot**: `https://api.binance.com`  
- **Testnet**: `https://testnet.binancefuture.com`

### Proxy Support
```java
Proxy proxyConn = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
ProxyAuth proxy = new ProxyAuth(proxyConn, null);
client.setProxy(proxy);
```

### Rate Limit Information
Enable rate limit usage information:
```java
client.setShowLimitUsage(true);
// Returns: {"data":"...","x-mbx-used-weight":"1","x-mbx-used-weight-1m":"1"}
```

## Code Quality and Standards

- **Checkstyle**: Enforced via Maven plugin with custom rules in `src/main/resources/checkstyle.xml`
- **No star imports**: Always use explicit imports
- **Consistent formatting**: 4-space indentation, specific whitespace rules
- **Method length limits**: Enforced by checkstyle
- **Unused imports/variables**: Not allowed

## Common Implementation Tips

1. **Always use LinkedHashMap for parameters** to maintain insertion order
2. **Handle all three exception types** in client code
3. **Use static final fields for URLs and constants**
4. **Follow the constructor overloading pattern** for client flexibility
5. **Use MockWebServer for unit testing** API endpoints
6. **Implement proper logging** using SLF4J
7. **Use builder pattern for complex parameter objects** when appropriate
8. **WebSocket connections should be properly closed** using `closeConnection()` or `closeAllConnections()`

## Common Patterns for New Code

When adding new endpoints or functionality:

1. Add the method signature to the appropriate interface (`Market`, `Account`, `UserData`)
2. Implement the method in both UM and CM specific classes
3. Follow the existing parameter validation patterns
4. Add corresponding unit tests with MockWebServer
5. Add example usage in the examples package
6. Ensure proper exception handling and logging