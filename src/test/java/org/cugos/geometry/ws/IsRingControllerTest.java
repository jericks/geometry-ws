package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IsRingControllerTest extends AbstractControllerTest  {

    private final String isRingGeometry = "LINESTRING (1 1, 1 5, 5 5, 5 1, 1 1)";

    private final String isNotRingGeometry = "LINESTRING (1 1, 5 5, 10 10)";

    @Test
    public void getIsNotRing() throws Exception {
        HttpRequest request = HttpRequest.GET("/isRing/wkt?geom=" + URLEncoder.encode(isNotRingGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void getIsRing() throws Exception {
        HttpRequest request = HttpRequest.GET("/isRing/wkt?geom=" + URLEncoder.encode(isRingGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }
    
    @Test
    public void postIsNotRing() throws Exception {
        HttpRequest request = HttpRequest.POST("/isRing/wkt", isNotRingGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void postIsRing() throws Exception {
        HttpRequest request = HttpRequest.POST("/isRing/wkt", isRingGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

}
