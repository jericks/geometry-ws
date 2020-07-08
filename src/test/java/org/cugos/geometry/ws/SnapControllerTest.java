package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SnapControllerTest extends AbstractControllerTest  {

    private final String polygonGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
    private final String anotherPolygonGeometry = "POLYGON ((11 11, 11 20, 20 20, 20 11, 11 11))";
    private final String snappedGeometry = "GEOMETRYCOLLECTION ("
            + "POLYGON ((0 0, 0 10, 11 11, 10 0, 0 0)), "
            + "POLYGON ((11 11, 11 20, 20 20, 20 11, 11 11)))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/snap/wkt/wkt" +
            "?distance=1.5&geom=" + URLEncoder.encode(Geometries.geometryCollection(polygonGeometry, anotherPolygonGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals(snappedGeometry, value);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/snap/wkt/wkt?distance=1.5", Geometries.geometryCollection(polygonGeometry, anotherPolygonGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(snappedGeometry, geometry);
    }

    @Test
    public void badRequest() throws Exception {
        Assertions.assertThrows(HttpClientException.class, () -> {
            HttpRequest request = HttpRequest.GET("/snap/wkt/wkt?distance=10&geom=" + URLEncoder.encode(polygonGeometry, "UTF-8"));
            client.toBlocking().retrieve(request);
        });
    }

}
