package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class DisjointControllerTest extends AbstractControllerTest  {

    private final String polygonGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
    private final String disjointGeometry = "LINESTRING (15 15, 20 20)";
    private final String notDisjointGeometry = "LINESTRING (5 5, 5 15)";

    private String geometryCollection(String firstGeometry, String secondGeometry) {
        return String.format("GEOMETRYCOLLECTION (%s, %s)", firstGeometry, secondGeometry);
    }

    @Test
    public void getDisjoint() throws Exception {
        HttpRequest request = HttpRequest.GET("/disjoint/wkt" +
            "?geom=" + URLEncoder.encode(geometryCollection(polygonGeometry, disjointGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("true", value);
    }

    @Test
    public void getIsNotDisjoint() throws Exception {
        HttpRequest request = HttpRequest.GET("/disjoint/wkt" +
            "?geom=" + URLEncoder.encode(geometryCollection(notDisjointGeometry, polygonGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("false", value);
    }
    
    @Test
    public void postDisjoint() throws Exception {
        HttpRequest request = HttpRequest.POST("/disjoint/wkt", geometryCollection(polygonGeometry, disjointGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

    @Test
    public void postIsNotDisjoint() throws Exception {
        HttpRequest request = HttpRequest.POST("/disjoint/wkt", geometryCollection(notDisjointGeometry, polygonGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/disjoint/wkt?geom=" + URLEncoder.encode(disjointGeometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
