package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class DistanceControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "POINT (1 1)";
    private final String otherGeometry = "POINT (20 23)";

    private String geometryCollection(String firstGeometry, String secondGeometry) {
        return String.format("GEOMETRYCOLLECTION (%s, %s)", firstGeometry, secondGeometry);
    }

    @Test
    public void getDistance() throws Exception {
        HttpRequest request = HttpRequest.GET("/distance/wkt" +
            "?geom=" + URLEncoder.encode(geometryCollection(inputGeometry, otherGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("29.068883707497267", value);
    }

    @Test
    public void postDistance() throws Exception {
        HttpRequest request = HttpRequest.POST("/distance/wkt", geometryCollection(inputGeometry, otherGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("29.068883707497267", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/distance/wkt?geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
