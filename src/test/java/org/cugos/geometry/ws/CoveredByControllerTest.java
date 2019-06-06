package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class CoveredByControllerTest extends AbstractControllerTest  {

    private final String polygonGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
    private final String pointGeometry = "POINT (5 5)";

    private String geometryCollection(String firstGeometry, String secondGeometry) {
        return String.format("GEOMETRYCOLLECTION (%s, %s)", firstGeometry, secondGeometry);
    }

    @Test
    public void getCoveredBy() throws Exception {
        HttpRequest request = HttpRequest.GET("/coveredBy/wkt" +
            "?geom=" + URLEncoder.encode(geometryCollection(pointGeometry, polygonGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("true", value);
    }

    @Test
    public void getDoesNotCoveredBy() throws Exception {
        HttpRequest request = HttpRequest.GET("/coveredBy/wkt" +
            "?geom=" + URLEncoder.encode(geometryCollection(polygonGeometry, pointGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("false", value);
    }
    
    @Test
    public void postCoveredBy() throws Exception {
        HttpRequest request = HttpRequest.POST("/coveredBy/wkt", geometryCollection(pointGeometry, polygonGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

    @Test
    public void postDoesNotCoveredBy() throws Exception {
        HttpRequest request = HttpRequest.POST("/coveredBy/wkt", geometryCollection(polygonGeometry, pointGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/coveredBy/wkt?geom=" + URLEncoder.encode(pointGeometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
