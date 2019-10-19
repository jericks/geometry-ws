package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class InterpolatePointControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "LINESTRING (0 0, 5 5, 10 10)";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/interpolatePoint/wkt/wkt?position=0.25&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (2.5 2.5)", geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/interpolatePoint/wkt/wkt?position=0.25", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (2.5 2.5)", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void wrongGeometryType() throws Exception {
        HttpRequest request = HttpRequest.GET("/overlaps/wkt?position=0.25&geom=" + URLEncoder.encode("POINT (1 1)", "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
