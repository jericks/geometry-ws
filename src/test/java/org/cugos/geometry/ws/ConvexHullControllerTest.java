package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConvexHullControllerTest extends AbstractControllerTest {

    private final String polygon = "POLYGON ((9 52, 9 50, 7 50, 7 48, 10 47, 10 46, 13 46, 11 52, 10 52, 9 52))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/convexHull/wkt/wkt?geom=" + URLEncoder.encode(polygon, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((10 46, 7 48, 7 50, 9 52, 11 52, 13 46, 10 46))", geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/convexHull/wkt/wkt", polygon).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((10 46, 7 48, 7 50, 9 52, 11 52, 13 46, 10 46))", geometry);
    }

}
