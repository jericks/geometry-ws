package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class LocatePointControllerTest extends AbstractControllerTest  {

    private final String geometry = "LINESTRING (0 0, 5 5, 10 10)";
    private final String anotherGeometry = "POINT (2.5 2.5)";
    private final String polygonGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/locatePoint/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(geometry, anotherGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("0.25", value);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/locatePoint/wkt", Geometries.geometryCollection(anotherGeometry, geometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("0.25", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void onlyOneGeomtry() throws Exception {
        HttpRequest request = HttpRequest.GET("/locatePoint/wkt?geom=" + URLEncoder.encode(geometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

    @Test(expected = HttpClientException.class)
    public void missingPoint() throws Exception {
        HttpRequest request = HttpRequest.GET("/locatePoint/wkt?geom=" + URLEncoder.encode(Geometries.geometryCollection(geometry, polygonGeometry), "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
