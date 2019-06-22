package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class DifferenceControllerTest extends AbstractControllerTest  {

    private final String polygonGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
    private final String anotherPolygonGeometry = "POLYGON ((5 5, 5 20, 20 20, 20 5, 5 5))";
    private final String differenceGeometry = "POLYGON ((0 0, 0 10, 5 10, 5 5, 10 5, 10 0, 0 0))";
    private final String pointGeometry = "POINT (5 5)";

    private String geometryCollection(String firstGeometry, String secondGeometry) {
        return String.format("GEOMETRYCOLLECTION (%s, %s)", firstGeometry, secondGeometry);
    }

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/difference/wkt/wkt" +
            "?geom=" + URLEncoder.encode(geometryCollection(polygonGeometry, anotherPolygonGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals(differenceGeometry, value);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/difference/wkt/wkt", geometryCollection(polygonGeometry, anotherPolygonGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(differenceGeometry, geometry);
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/difference/wkt/wkt?geom=" + URLEncoder.encode(pointGeometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
