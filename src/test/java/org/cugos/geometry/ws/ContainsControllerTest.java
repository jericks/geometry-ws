package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContainsControllerTest extends AbstractControllerTest  {

    private final String polygonGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
    private final String pointGeometry = "POINT (5 5)";

    @Test
    public void getContains() throws Exception {
        HttpRequest request = HttpRequest.GET("/contains/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(polygonGeometry, pointGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("true", value);
    }

    @Test
    public void getDoesNotContain() throws Exception {
        HttpRequest request = HttpRequest.GET("/contains/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(pointGeometry, polygonGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("false", value);
    }
    
    @Test
    public void postContains() throws Exception {
        HttpRequest request = HttpRequest.POST("/contains/wkt", Geometries.geometryCollection(polygonGeometry, pointGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

    @Test
    public void postDoesNotContains() throws Exception {
        HttpRequest request = HttpRequest.POST("/contains/wkt", Geometries.geometryCollection(pointGeometry, polygonGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void badRequest() throws Exception {
        Assertions.assertThrows(HttpClientException.class, () -> {
            HttpRequest request = HttpRequest.GET("/contains/wkt?geom=" + URLEncoder.encode(pointGeometry, "UTF-8"));
            client.toBlocking().retrieve(request);
        });
    }

}
