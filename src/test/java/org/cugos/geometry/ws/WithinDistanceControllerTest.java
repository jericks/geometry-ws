package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class WithinDistanceControllerTest extends AbstractControllerTest  {

    private String geometryCollection(String firstGeometry, String secondGeometry) {
        return String.format("GEOMETRYCOLLECTION (%s, %s)", firstGeometry, secondGeometry);
    }

    @Test
    public void getWithinDistance() throws Exception {
        HttpRequest request = HttpRequest.GET("/withinDistance/wkt" +
            "?geom=" + URLEncoder.encode(geometryCollection("POINT (1 1)", "POINT (20 23)"), "UTF-8") +
            "&distance=30");
        String value = client.toBlocking().retrieve(request);
        assertEquals("true", value);
    }

    @Test
    public void getIsNotWithinDistance() throws Exception {
        HttpRequest request = HttpRequest.GET("/withinDistance/wkt" +
            "?geom=" + URLEncoder.encode(geometryCollection("POINT (1 1)", "POINT (20 23)"), "UTF-8") +
            "&distance=10");
        String value = client.toBlocking().retrieve(request);
        assertEquals("false", value);
    }
    
    @Test
    public void postWithinDistance() throws Exception {
        HttpRequest request = HttpRequest.POST("/withinDistance/wkt?distance=30", geometryCollection("POINT (1 1)", "POINT (20 23)"))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

    @Test
    public void postIsNotWithinDistance() throws Exception {
        HttpRequest request = HttpRequest.POST("/withinDistance/wkt?distance=10", geometryCollection("POINT (1 1)", "POINT (20 23)"))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/withinDistance/wkt?distance=10&geom=" + URLEncoder.encode("POINT (1 1)", "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
