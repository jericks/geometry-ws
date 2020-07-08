package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlacePointControllerTest extends AbstractControllerTest  {

    private final String lineGeometry = "LINESTRING (0 0, 5 5, 10 10)";

    private final String pointGeometry = "POINT (3 4.5)";

    private final String snappedPoint = "POINT (3.75 3.75)";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/placePoint/wkt/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(lineGeometry, pointGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals(snappedPoint, value);
    }

    @Test
    public void getPointLine() throws Exception {
        HttpRequest request = HttpRequest.GET("/placePoint/wkt/wkt" +
                "?geom=" + URLEncoder.encode(Geometries.geometryCollection(pointGeometry, lineGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals(snappedPoint, value);
    }


    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/placePoint/wkt/wkt", Geometries.geometryCollection(lineGeometry, pointGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(snappedPoint, geometry);
    }


    @Test
    public void badRequest() throws Exception {
        Assertions.assertThrows(HttpClientException.class, () -> {
            HttpRequest request = HttpRequest.GET("/placePoint/wkt/wkt?geom=" + URLEncoder.encode(pointGeometry, "UTF-8"));
            client.toBlocking().retrieve(request);
        });
    }

    @Test
    public void notPointAndLine() throws Exception {
        Assertions.assertThrows(HttpClientException.class, () -> {
            HttpRequest request = HttpRequest.GET("/placePoint/wkt/wkt?geom=" + URLEncoder.encode(Geometries.geometryCollection(pointGeometry, pointGeometry), "UTF-8"));
            client.toBlocking().retrieve(request);
        });
    }

}
