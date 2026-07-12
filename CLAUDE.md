# CLAUDE.md

## Project Overview

Java library providing connectivity to the Binance Futures API and in less amount Spot API. Fork maintained by ma-traders. Supports USD-M Futures (`/fapi/*`), COIN-M Futures (`/dapi/*`), WebSocket streams, user data streams, portfolio margin, subaccount transfers, and wallet/capital APIs.

## Build & Test

```bash
mvn clean test          # Run unit tests (includes checkstyle)
mvn checkstyle:check    # Run checkstyle only
mvn install -Dgpg.skip=true -Dmaven.javadoc.skip=true  # Full build
```

- **Java 8** source/target (CI tests on 8, 11, 17)
- **Maven** build system
- No special environment variables needed for tests (uses MockWebServer)

## Project Structure

```
src/main/java/com/binance/connector/futures/client/
├── FuturesClient.java, SpotClient.java, WebsocketClient.java  # Interfaces
├── impl/
│   ├── UMFuturesClientImpl.java    # USD-M Futures client
│   ├── CMFuturesClientImpl.java    # COIN-M Futures client
│   ├── SpotClientImpl.java         # Spot client
│   ├── FuturesClientImpl.java      # Abstract base class
│   ├── UM/CMWebsocketClientImpl.java  # WebSocket clients
│   ├── um_futures/                 # UMMarket, UMAccount, UMUserData, UMPortfolioMargin
│   ├── cm_futures/                 # CMMarket, CMAccount, CMUserData, CMPortfolioMargin
│   ├── spot/                       # SpotUserData
│   ├── subaccount/                 # SubAccount
│   └── wallet/                     # Asset, Capital
├── enums/       # DefaultUrls, HttpMethod, RequestType
├── exceptions/  # BinanceConnectorException, BinanceClientException, BinanceServerException
└── utils/       # HttpClientSingleton, RequestHandler, UrlBuilder, WebSocketConnection, etc.

src/test/java/
├── unit/        # Unit tests with MockWebServer (organized by: um_futures/, cm_futures/, wallet/, subaccount/)
└── examples/    # Usage examples (not actual tests), PrivateConfig.java for API keys
```

## Key Dependencies

- **OkHttp 4.9.2** — HTTP client and WebSocket
- **org.json** — JSON parsing
- **commons-codec** — HMAC-SHA256 signatures
- **SLF4J + Logback** — Logging
- **JUnit 4.13.2 + MockWebServer** — Testing

## Code Conventions

### Enforced by Checkstyle (`src/main/resources/checkstyle.xml`)
- No star imports — use explicit imports
- No unused imports or local variables
- No magic numbers
- Braces required on all blocks
- Method length and parameter count limits enforced
- Modifier ordering enforced
- No TODO comments (flagged by checkstyle)

### Naming
- Classes: `PascalCase` (e.g., `UMFuturesClientImpl`, `TestUMPing`)
- Methods: `camelCase` (e.g., `exchangeInfo()`, `testNewOrder()`)
- Constants: `UPPER_SNAKE_CASE` (e.g., `API_KEY`, `UM_BASE_URL`)
- Packages: lowercase with underscores (e.g., `um_futures`, `cm_futures`)

### Patterns
- **Parameters**: Always use `LinkedHashMap<String, Object>` for API parameters
- **Client constructors**: Overloaded — no-arg, baseUrl, apiKey+secretKey, apiKey+secretKey+baseUrl
- **API access**: `client.market()`, `client.account()`, `client.userData()`, `client.portfolioMargin()`
- **Exceptions**: Three types — `BinanceConnectorException` (validation), `BinanceClientException` (4XX), `BinanceServerException` (5XX)

## Testing

Unit tests use **MockWebServer** to mock Binance API responses:
- `MockData` class provides constants (`PREFIX`, `MOCK_RESPONSE`, `HTTP_STATUS_OK`, etc.)
- `MockWebServerDispatcher` creates dispatchers for specific paths
- Each test class has `@Before init()` setting up MockWebServer and base URL
- Test classes named `Test<API><Endpoint>` (e.g., `TestUMPing`, `TestCMExchangeInfo`)

## Adding New Endpoints

1. Add method to the appropriate category class (e.g., `UMMarket`, `CMAccount`)
2. Mirror implementation in both UM and CM variants if applicable
3. Use `RequestHandler` for HTTP requests with proper `HttpMethod` and `RequestType`
4. Add unit test with MockWebServer in the corresponding `unit/` subdirectory
5. Run `mvn checkstyle:check` before committing

## CI/CD

GitHub Actions on PRs to `main`/`master`/`rc-**`:
1. Checkstyle validation
2. Unit tests
3. Maven build

Tested across Java 8, 11, and 17.

## Git Workflow

- Main branch: `main`
- Feature branches: `feature/*`
- Packages published to GitHub Packages (`ma-traders/packages-repo`)
