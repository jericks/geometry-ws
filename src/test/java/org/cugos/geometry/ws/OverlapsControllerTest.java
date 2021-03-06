package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OverlapsControllerTest extends AbstractControllerTest  {

    private final String firstGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
    private final String secondGeometry = "POLYGON ((2 2, 2 14, 14 14, 14 2, 2 2))";
    private final String thirdGeometry = "POINT (15 15)";

    @Test
    public void getOverlaps() throws Exception {
        HttpRequest request = HttpRequest.GET("/overlaps/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(firstGeometry, secondGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("true", value);
    }

    @Test
    public void getDoesNotOverlap() throws Exception {
        HttpRequest request = HttpRequest.GET("/overlaps/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(firstGeometry, thirdGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("false", value);
    }
    
    @Test
    public void postOverlaps() throws Exception {
        HttpRequest request = HttpRequest.POST("/overlaps/wkt", Geometries.geometryCollection(firstGeometry, secondGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

    @Test
    public void postDoesNotOverlap() throws Exception {
        HttpRequest request = HttpRequest.POST("/overlaps/wkt", Geometries.geometryCollection(firstGeometry, thirdGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void badRequest() throws Exception {
        Assertions.assertThrows(HttpClientException.class, () -> {
            HttpRequest request = HttpRequest.GET("/overlaps/wkt?geom=" + URLEncoder.encode(firstGeometry, "UTF-8"));
            client.toBlocking().retrieve(request);
        });
    }

}
