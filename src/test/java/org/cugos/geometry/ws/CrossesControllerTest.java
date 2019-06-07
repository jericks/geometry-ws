package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class CrossesControllerTest extends AbstractControllerTest  {


    private final String lineGeometry1 = "LINESTRING (5 5, 5 15)";
    private final String polygonGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
    private final String lineGeometry2 = "LINESTRING (15 15, 20 20)";

    private String geometryCollection(String firstGeometry, String secondGeometry) {
        return String.format("GEOMETRYCOLLECTION (%s, %s)", firstGeometry, secondGeometry);
    }

    @Test
    public void getCrosses() throws Exception {
        HttpRequest request = HttpRequest.GET("/crosses/wkt" +
            "?geom=" + URLEncoder.encode(geometryCollection(lineGeometry1, polygonGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("true", value);
    }

    @Test
    public void getDoesNotCross() throws Exception {
        HttpRequest request = HttpRequest.GET("/crosses/wkt" +
            "?geom=" + URLEncoder.encode(geometryCollection(lineGeometry2, polygonGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("false", value);
    }
    
    @Test
    public void postCrosses() throws Exception {
        HttpRequest request = HttpRequest.POST("/crosses/wkt", geometryCollection(lineGeometry1, polygonGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

    @Test
    public void postDoesNotCross() throws Exception {
        HttpRequest request = HttpRequest.POST("/crosses/wkt", geometryCollection(lineGeometry2, polygonGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/crosses/wkt?geom=" + URLEncoder.encode(lineGeometry1, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
