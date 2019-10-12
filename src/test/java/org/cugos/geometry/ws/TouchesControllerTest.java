package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class TouchesControllerTest extends AbstractControllerTest  {

    private final String polygonGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
    private final String anotherPolygonGeometry = "POLYGON ((10 10, 10 14, 14 14, 14 10, 10 10))";
    private final String pointGeometry = "POINT (15 15)";

    @Test
    public void getTouches() throws Exception {
        HttpRequest request = HttpRequest.GET("/touches/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(polygonGeometry, anotherPolygonGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("true", value);
    }

    @Test
    public void getDoesNotTouch() throws Exception {
        HttpRequest request = HttpRequest.GET("/touches/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(pointGeometry, polygonGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("false", value);
    }
    
    @Test
    public void postTouches() throws Exception {
        HttpRequest request = HttpRequest.POST("/touches/wkt", Geometries.geometryCollection(polygonGeometry, anotherPolygonGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

    @Test
    public void postDoesNotTouches() throws Exception {
        HttpRequest request = HttpRequest.POST("/touches/wkt", Geometries.geometryCollection(pointGeometry, polygonGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/touches/wkt?geom=" + URLEncoder.encode(pointGeometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
