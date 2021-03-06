package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntersectionControllerTest extends AbstractControllerTest  {

    private final String polygonGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
    private final String anotherPolygonGeometry = "POLYGON ((5 5, 5 20, 20 20, 20 5, 5 5))";
    private final String intersectionGeometry = "POLYGON ((5 10, 10 10, 10 5, 5 5, 5 10))";
    private final String pointGeometry = "POINT (5 5)";

    @Test
    public void getIntersection() throws Exception {
        HttpRequest request = HttpRequest.GET("/intersection/wkt/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(polygonGeometry, anotherPolygonGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals(intersectionGeometry, value);
    }

    @Test
    public void postIntersection() throws Exception {
        HttpRequest request = HttpRequest.POST("/intersection/wkt/wkt", Geometries.geometryCollection(polygonGeometry, anotherPolygonGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(intersectionGeometry, geometry);
    }

    @Test
    public void badRequest() throws Exception {
        Assertions.assertThrows(HttpClientException.class, () -> {
            HttpRequest request = HttpRequest.GET("/intersection/wkt/wkt?geom=" + URLEncoder.encode(pointGeometry, "UTF-8"));
            client.toBlocking().retrieve(request);
        });
    }

}
