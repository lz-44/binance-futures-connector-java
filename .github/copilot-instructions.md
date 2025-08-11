# Binance Futures Connector Java

Binance Futures Connector Java is a deprecated Java library (use binance-connector-java instead) that provides a connector to the Binance Futures API. This is a Maven-based Java 8 project with comprehensive examples and unit tests.

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Working Effectively

### Bootstrap and Build
- **NEVER CANCEL builds or tests**: Build times are measured and documented below
- **Java requirement**: Java 8+ is required, but Java 17 works fine (backward compatible)
- **Maven requirement**: Maven 3.6+ is required
- Run these commands in sequence:
  - `mvn clean` -- takes 10 seconds
  - `mvn compile` -- takes 2-3 minutes on first run (due to dependency downloads), then 8 seconds. NEVER CANCEL. Set timeout to 5+ minutes for first build.
  - `mvn checkstyle:check` -- takes 4 seconds. Code style validation using Checkstyle.
  - `mvn test-compile` -- compiles test sources, takes 8 seconds after dependencies downloaded
  - `mvn test` -- runs 299 unit tests, takes 33 seconds. NEVER CANCEL. Set timeout to 2+ minutes.
  - `mvn install -Dgpg.skip=true -Dmaven.javadoc.skip=true` -- complete build and install, takes 70 seconds. NEVER CANCEL. Set timeout to 3+ minutes.

### Full Clean Build Process
```bash
mvn clean compile test-compile  # Takes 8 seconds after initial dependency download
mvn test                        # Takes 33 seconds, runs 299 tests. NEVER CANCEL.
mvn install -Dgpg.skip=true -Dmaven.javadoc.skip=true  # Takes 70 seconds. NEVER CANCEL.
```

### Quick Development Cycle
```bash
mvn clean compile              # 8 seconds - compile main sources
mvn checkstyle:check          # 4 seconds - validate code style
mvn test-compile              # additional 0-1 seconds - compile test sources  
mvn test                      # 33 seconds - run all tests. NEVER CANCEL.
```

## Validation

### Manual Testing Scenarios
- **ALWAYS test example functionality** after making changes to core connector code
- Run example code to validate API connectivity:
  ```bash
  mvn test-compile
  java -cp target/classes:target/test-classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q) examples.um_futures.market.Time
  ```
- Example will fail with network error if no internet access - this is EXPECTED behavior in sandboxed environments
- **WebSocket examples**: Can be compiled and started but require real network connectivity to function
- **Account examples**: Use testnet configuration by default (examples use PrivateConfig.TESTNET_* constants)

### CI/CD Validation Commands
Run these commands before submitting changes (matches .github/workflows/java.yml):
- `mvn checkstyle:check` -- REQUIRED: Style validation, takes 4 seconds
- `mvn clean test` -- REQUIRED: Clean build and full test suite, takes 41 seconds total. NEVER CANCEL.
- `mvn install -Dgpg.skip=true -Dmaven.javadoc.skip=true` -- REQUIRED: Full build, takes 70 seconds. NEVER CANCEL.

## Common Tasks

### Running Examples
- Examples are located in `src/test/java/examples/`
- Two main categories: `um_futures/` (USD-M Futures) and `cm_futures/` (COIN-M Futures)  
- API Key configuration in `src/test/java/examples/PrivateConfig.java` (empty by default)
- Market data examples work without API keys: `examples.um_futures.market.*`
- Account/trading examples require API keys: `examples.um_futures.account.*`
- WebSocket examples: `examples.um_futures.websocket.*`

### Project Structure Navigation
```
src/main/java/com/binance/connector/futures/  # Main library code
├── client/impl/                              # Client implementations
├── client/exceptions/                        # Custom exceptions  
└── client/utils/                            # Utilities (RequestHandler, etc.)

src/test/java/examples/                       # Example code
├── um_futures/                              # USD-M Futures examples
│   ├── market/                              # Market data (no auth required)
│   ├── account/                             # Account/trading (auth required)
│   ├── websocket/                           # WebSocket streams
│   └── userdata/                            # User data streams
└── cm_futures/                              # COIN-M Futures examples (similar structure)

src/test/java/unit/                          # Unit tests (299 tests)
```

### Key Files to Know
- `pom.xml` -- Maven configuration, Java 8 target, dependencies
- `src/main/resources/checkstyle.xml` -- Code style rules
- `src/main/resources/logback.xml` -- Logging configuration  
- `src/test/java/examples/PrivateConfig.java` -- API key configuration for examples
- `.github/workflows/java.yml` -- CI/CD pipeline definition
- `CHANGELOG.md` -- Version history and breaking changes

### Dependencies and Versions
- **Java**: Targets Java 8, compatible with Java 11, 17
- **OkHttp**: 4.9.2 (HTTP client)
- **JUnit**: 4.13.2 (testing)
- **Logback**: 1.2.13 (logging)
- **Checkstyle**: Uses maven-checkstyle-plugin 3.1.2 with checkstyle 9.3

### Build Artifacts
After `mvn install`, artifacts are created in:
- `target/binance-futures-connector-java-3.0.5.jar` -- Main library JAR
- `target/binance-futures-connector-java-3.0.5-sources.jar` -- Sources JAR
- `~/.m2/repository/io/github/binance/binance-futures-connector-java/3.0.5/` -- Local Maven repository

### Troubleshooting
- **Bootstrap class path warnings**: Expected with Java 17 targeting Java 8, does not affect functionality
- **Encoding warnings**: Expected, uses UTF-8 platform encoding
- **Network errors in examples**: Expected in sandboxed environments, indicates code is working correctly
- **Checkstyle violations**: Fix with `mvn checkstyle:check` to see specific issues
- **Test failures**: All 299 tests should pass; failures indicate code issues

### Repository Status
- **DEPRECATED**: This repository is deprecated in favor of binance-connector-java
- **Maintenance mode**: Security updates only, no new features
- **API coverage**: USD-M Futures (`/fapi/*`), COIN-M Futures (`/dapi/*`), WebSocket streams
- **Version**: Current version 3.0.5 as of repository state

## Timing Summary
- Clean: 10 seconds
- Compile (first time): 2-3 minutes (dependency downloads)  
- Compile (subsequent): 8 seconds
- Checkstyle: 4 seconds
- Test suite: 33 seconds (299 tests) - NEVER CANCEL
- Full install: 70 seconds - NEVER CANCEL
- Complete clean build cycle: ~45 seconds (after dependencies cached)