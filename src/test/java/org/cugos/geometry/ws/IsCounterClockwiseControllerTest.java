package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class IsCounterClockwiseControllerTest extends AbstractControllerTest  {

    private final String isCounterClockwiseGeometry = "LINEARRING (15 20, 10 20, 10 10, 15 10, 15 20)";

    private final String isNotCounterClockwiseGeometry = "LINEARRING (15 20, 15 10, 10 10, 10 20, 15 20)";

    @Test
    public void getIsNotCounterClockwise() throws Exception {
        HttpRequest request = HttpRequest.GET("/isCounterClockwise/wkt?geom=" + URLEncoder.encode(isNotCounterClockwiseGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void getIsCounterClockwise() throws Exception {
        HttpRequest request = HttpRequest.GET("/isCounterClockwise/wkt?geom=" + URLEncoder.encode(isCounterClockwiseGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }
    
    @Test
    public void postIsNotCounterClockwise() throws Exception {
        HttpRequest request = HttpRequest.POST("/isCounterClockwise/wkt", isNotCounterClockwiseGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void postIsCounterClockwise() throws Exception {
        HttpRequest request = HttpRequest.POST("/isCounterClockwise/wkt", isCounterClockwiseGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

}
