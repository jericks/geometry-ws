package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NodeControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "LINESTRING (5.19775390625 51.07421875, 7.52685546875 53.7548828125, " +
            "11.65771484375 49.931640625, 7.52685546875 47.20703125, 9.50439453125 54.501953125, " +
            "7.35107421875 52.4365234375, 4.53857421875 52.65625, 6.38427734375 50.634765625)";

    private final String resultGeometry = "MULTILINESTRING ((5.2 51, 5.6 51.6), (5.6 51.6, 6.4 52.6), " +
            "(6.4 52.6, 7.6 53.8, 8.2 53.2), (8.2 53.2, 9 52.4), (9 52.4, 11.6 50, 7.6 47.2, 9 52.4), " +
            "(9 52.4, 9.6 54.6, 8.2 53.2), (8.2 53.2, 7.4 52.4, 6.4 52.6), (6.4 52.6, 4.6 52.6, 5.6 51.6), " +
            "(5.6 51.6, 6.4 50.6))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/node/wkt/wkt?number=5&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(resultGeometry, geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/node/wkt/wkt?number=5", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(resultGeometry, geometry);
    }

}
