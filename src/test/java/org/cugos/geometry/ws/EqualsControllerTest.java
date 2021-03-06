package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EqualsControllerTest extends AbstractControllerTest  {

    @Test
    public void getEquals() throws Exception {
        String inputGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
        String otherGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
        HttpRequest request = HttpRequest.GET("/equals/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(inputGeometry, otherGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("true", value);
    }

    @Test
    public void getNotEquals() throws Exception {
        String inputGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
        String otherGeometry = "POLYGON ((1 1, 1 10, 10 10, 10 1, 1 1))";
        HttpRequest request = HttpRequest.GET("/equals/wkt" +
                "?geom=" + URLEncoder.encode(Geometries.geometryCollection(inputGeometry, otherGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("false", value);
    }

    @Test
    public void getEqualsExact() throws Exception {
        String inputGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
        String otherGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
        HttpRequest request = HttpRequest.GET("/equals/wkt" +
                "?type=exact&tolerance=1.1&geom=" + URLEncoder.encode(Geometries.geometryCollection(inputGeometry, otherGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("true", value);
    }

    @Test
    public void getEqualsTopo() throws Exception {
        String inputGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
        String otherGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
        HttpRequest request = HttpRequest.GET("/equals/wkt" +
                "?type=topo&geom=" + URLEncoder.encode(Geometries.geometryCollection(inputGeometry, otherGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("true", value);
    }

    @Test
    public void getEqualsNorm() throws Exception {
        String inputGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
        String otherGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
        HttpRequest request = HttpRequest.GET("/equals/wkt" +
                "?type=norm&geom=" + URLEncoder.encode(Geometries.geometryCollection(inputGeometry, otherGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("true", value);
    }

    @Test
    public void postEquals() throws Exception {
        String inputGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
        String otherGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
        HttpRequest request = HttpRequest.POST("/equals/wkt", Geometries.geometryCollection(inputGeometry, otherGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

    @Test
    public void badRequest() throws Exception {
        Assertions.assertThrows(HttpClientException.class, () -> {
            String inputGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
            HttpRequest request = HttpRequest.GET("/equals/wkt?geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
            client.toBlocking().retrieve(request);
        });
    }

}
