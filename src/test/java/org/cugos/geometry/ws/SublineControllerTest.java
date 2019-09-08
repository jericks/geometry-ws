package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class SublineControllerTest extends AbstractControllerTest  {

    private final String lineGeometry = "LINESTRING (0 0, 10 10, 20 20)";

    private final String pointGeometry = "POINT (5 5)";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/subline/wkt/wkt" +
            "?geom=" + URLEncoder.encode(lineGeometry, "UTF-8") + "&start=0.25&end=0.75");
        String value = client.toBlocking().retrieve(request);
        assertEquals("LINESTRING (5 5, 10 10, 15 15)", value);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/subline/wkt/wkt?start=0.25&end=0.75", lineGeometry)
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("LINESTRING (5 5, 10 10, 15 15)", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void wrongGeometryType() throws Exception {
        HttpRequest request = HttpRequest.GET("/subline/wkt/wkt?start=0.25&end=0.75&geom=" + URLEncoder.encode(pointGeometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

    @Test(expected = HttpClientException.class)
    public void endBeforeStart() throws Exception {
        HttpRequest request = HttpRequest.GET("/subline/wkt/wkt?start=0.75&end=0.25&geom=" + URLEncoder.encode(lineGeometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
