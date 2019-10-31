package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class SimplifyControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "LINESTRING (1 1, 2.3333333333333335 2.3333333333333335, "
            + "3.666666666666667 3.666666666666667, 5 5, 6.4 6.4, "
            + "7.800000000000001 7.800000000000001, 9.200000000000001 "
            + "9.200000000000001, 10.600000000000001 10.600000000000001, "
            + "12 12)";

    @Test
    public void getDP() throws Exception {
        HttpRequest request = HttpRequest.GET("/simplify/wkt/wkt" +
                "?algorithm=dp" +
                "&tolerance=2.0" +
                "&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("LINESTRING (1 1, 12 12)", geometry);
    }

    @Test
    public void getTP() throws Exception {
        HttpRequest request = HttpRequest.GET("/simplify/wkt/wkt" +
                "?algorithm=tp" +
                "&tolerance=2.0" +
                "&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("LINESTRING (1 1, 12 12)", geometry);
    }

    @Test
    public void postVS() throws Exception {
        HttpRequest request = HttpRequest.POST("/simplify/wkt/wkt" +
                "?algorithm=vw" +
                "&tolerance=2.0", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("LINESTRING (1 1, 12 12)", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/simplify/wkt/wkt" +
                "?algorithm=bad_algorithm" +
                "&tolerance=2.0" +
                "&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
