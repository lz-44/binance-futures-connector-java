package unit.spot.userdata;

import com.binance.connector.futures.client.impl.SpotWebsocketClientImpl;
import com.binance.connector.futures.client.utils.SignatureGenerator;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unit.MockData;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestSpotSubscribeUserDataStream {
    private static final int LATCH_COUNT = 1;
    private static final long LATCH_TIMEOUT = 5L;
    private static final int ERROR_CODE = -1;
    private static final Logger logger =
            LoggerFactory.getLogger(TestSpotSubscribeUserDataStream.class);
    private static final String USER_DATA_EVENT =
            "{\"e\":\"executionReport\",\"E\":123456789}";
    private MockWebServer mockWebServer;
    private SpotWebsocketClientImpl client;
    private int connectionId;

    @Before
    public void init() {
        this.mockWebServer = new MockWebServer();
    }

    @After
    public void tearDown() {
        if (client != null) {
            client.closeConnection(connectionId);
        }
        try {
            this.mockWebServer.close();
        } catch (IOException e) {
            logger.warn("MockWebServer close timed out", e);
        }
    }

    @Test
    public void testSubscribeUserDataStream() throws Exception {
        CountDownLatch openLatch = new CountDownLatch(LATCH_COUNT);
        CountDownLatch messageLatch = new CountDownLatch(LATCH_COUNT);
        AtomicBoolean openCalled = new AtomicBoolean(false);
        AtomicReference<String> receivedMessage = new AtomicReference<>();
        AtomicReference<String> receivedSubscribeReq = new AtomicReference<>();

        MockResponse wsUpgrade = new MockResponse().withWebSocketUpgrade(
                new WebSocketListener() {
                    @Override
                    public void onMessage(WebSocket ws, String text) {
                        receivedSubscribeReq.set(text);
                        JSONObject req = new JSONObject(text);
                        JSONObject resp = new JSONObject();
                        resp.put("id", req.getString("id"));
                        resp.put("status", MockData.HTTP_STATUS_OK);
                        resp.put("result", new JSONObject());
                        ws.send(resp.toString());
                        ws.send(USER_DATA_EVENT);
                    }
                });
        mockWebServer.enqueue(wsUpgrade);

        String wsApiUrl = mockWebServer.url("/ws-api/v3").toString()
                .replace("http://", "ws://");

        client = new SpotWebsocketClientImpl(
                "ws://unused", wsApiUrl, Duration.ZERO);

        connectionId = client.subscribeUserDataStream(
                MockData.API_KEY,
                MockData.SECRET_KEY,
                msg -> {
                    openCalled.set(true);
                    openLatch.countDown();
                },
                msg -> {
                    receivedMessage.set(msg);
                    messageLatch.countDown();
                },
                msg -> { },
                msg -> { }
        );

        assertTrue("onOpen should be called",
                openLatch.await(LATCH_TIMEOUT, TimeUnit.SECONDS));
        assertTrue(openCalled.get());

        assertTrue("onMessage should receive user data event",
                messageLatch.await(LATCH_TIMEOUT, TimeUnit.SECONDS));
        assertEquals(USER_DATA_EVENT, receivedMessage.get());

        JSONObject subscribeReq = new JSONObject(receivedSubscribeReq.get());
        assertEquals("userDataStream.subscribe.signature",
                subscribeReq.getString("method"));

        JSONObject params = subscribeReq.getJSONObject("params");
        assertEquals(MockData.API_KEY, params.getString("apiKey"));
        assertTrue(params.has("timestamp"));
        assertTrue(params.has("signature"));

        long timestamp = params.getLong("timestamp");
        String expectedPayload = "apiKey=" + MockData.API_KEY
                + "&timestamp=" + timestamp;
        String expectedSig = SignatureGenerator.getSignature(
                expectedPayload, MockData.SECRET_KEY);
        assertEquals(expectedSig, params.getString("signature"));
    }

    @Test
    public void testSubscribeUserDataStreamFailure() throws Exception {
        CountDownLatch failureLatch = new CountDownLatch(LATCH_COUNT);
        AtomicReference<String> failureMsg = new AtomicReference<>();
        final int errorStatus = 400;

        MockResponse wsUpgrade = new MockResponse().withWebSocketUpgrade(
                new WebSocketListener() {
                    @Override
                    public void onMessage(WebSocket ws, String text) {
                        JSONObject req = new JSONObject(text);
                        JSONObject resp = new JSONObject();
                        resp.put("id", req.getString("id"));
                        resp.put("status", errorStatus);
                        resp.put("error", new JSONObject()
                                .put("code", ERROR_CODE));
                        ws.send(resp.toString());
                    }
                });
        mockWebServer.enqueue(wsUpgrade);

        String wsApiUrl = mockWebServer.url("/ws-api/v3").toString()
                .replace("http://", "ws://");

        client = new SpotWebsocketClientImpl(
                "ws://unused", wsApiUrl, Duration.ZERO);

        connectionId = client.subscribeUserDataStream(
                MockData.API_KEY,
                MockData.SECRET_KEY,
                msg -> { },
                msg -> { },
                msg -> { },
                msg -> {
                    failureMsg.set(msg);
                    failureLatch.countDown();
                }
        );

        assertTrue("onFailure should be called on subscription error",
                failureLatch.await(LATCH_TIMEOUT, TimeUnit.SECONDS));
        assertNotNull(failureMsg.get());
        assertTrue(failureMsg.get().contains("Subscription failed"));
    }

    @Test
    public void testSubscribeSimpleCallback() throws Exception {
        CountDownLatch messageLatch = new CountDownLatch(LATCH_COUNT);
        AtomicReference<String> receivedMessage = new AtomicReference<>();

        MockResponse wsUpgrade = new MockResponse().withWebSocketUpgrade(
                new WebSocketListener() {
                    @Override
                    public void onMessage(WebSocket ws, String text) {
                        JSONObject req = new JSONObject(text);
                        JSONObject resp = new JSONObject();
                        resp.put("id", req.getString("id"));
                        resp.put("status", MockData.HTTP_STATUS_OK);
                        resp.put("result", new JSONObject());
                        ws.send(resp.toString());
                        ws.send(USER_DATA_EVENT);
                    }
                });
        mockWebServer.enqueue(wsUpgrade);

        String wsApiUrl = mockWebServer.url("/ws-api/v3").toString()
                .replace("http://", "ws://");

        client = new SpotWebsocketClientImpl(
                "ws://unused", wsApiUrl, Duration.ZERO);

        connectionId = client.subscribeUserDataStream(
                MockData.API_KEY,
                MockData.SECRET_KEY,
                msg -> {
                    receivedMessage.set(msg);
                    messageLatch.countDown();
                }
        );

        assertTrue("onMessage should receive user data event",
                messageLatch.await(LATCH_TIMEOUT, TimeUnit.SECONDS));
        assertEquals(USER_DATA_EVENT, receivedMessage.get());
    }
}
