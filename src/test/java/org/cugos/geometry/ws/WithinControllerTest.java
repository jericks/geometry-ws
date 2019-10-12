package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class WithinControllerTest extends AbstractControllerTest  {

    private final String polygonGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
    private final String pointGeometry = "POINT (5 5)";

    @Test
    public void getWithin() throws Exception {
        HttpRequest request = HttpRequest.GET("/within/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(pointGeometry, polygonGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("true", value);
    }

    @Test
    public void getIsNotWithin() throws Exception {
        HttpRequest request = HttpRequest.GET("/within/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(polygonGeometry, pointGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("false", value);
    }
    
    @Test
    public void postWithin() throws Exception {
        HttpRequest request = HttpRequest.POST("/within/wkt", Geometries.geometryCollection(pointGeometry, polygonGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

    @Test
    public void postDoesNotWithin() throws Exception {
        HttpRequest request = HttpRequest.POST("/within/wkt", Geometries.geometryCollection(polygonGeometry, pointGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/within/wkt?geom=" + URLEncoder.encode(pointGeometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
