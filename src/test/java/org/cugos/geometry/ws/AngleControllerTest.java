package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class AngleControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "POINT (1 1)";
    private final String otherGeometry = "POINT (10 10)";

    private String geometryCollection(String firstGeometry, String secondGeometry) {
        return String.format("GEOMETRYCOLLECTION (%s, %s)", firstGeometry, secondGeometry);
    }

    @Test
    public void getAngle() throws Exception {
        HttpRequest request = HttpRequest.GET("/angle/wkt?type=radians" +
            "&geom=" + URLEncoder.encode(geometryCollection(inputGeometry, otherGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("0.7853981633974483", value);
    }

    @Test
    public void postAngle() throws Exception {
        HttpRequest request = HttpRequest.POST("/angle/wkt?type=degrees", geometryCollection(inputGeometry, otherGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("45.0", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/angle/wkt?type=degrees&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
