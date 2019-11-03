package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class RotateControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";

    @Test
    public void getTheta() throws Exception {
        HttpRequest request = HttpRequest.GET("/rotate/wkt/wkt" +
                "?theta=" + Math.toRadians(45) +
                "&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((0 0, -7.071067811865475 7.0710678118654755, 0.0000000000000009 " +
                "14.142135623730951, 7.0710678118654755 7.071067811865475, 0 0))", geometry);
    }

    @Test
    public void getSinCosXY() throws Exception {
        HttpRequest request = HttpRequest.GET("/rotate/wkt/wkt" +
                "?sinTheta=4" +
                "&cosTheta=3" +
                "&x=4" +
                "&y=3" +
                "&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((4 -22, -36 8, -6 48, 34 18, 4 -22))", geometry);
    }

    @Test
    public void getSinCos() throws Exception {
        HttpRequest request = HttpRequest.GET("/rotate/wkt/wkt" +
                "?sinTheta=4" +
                "&cosTheta=3" +
                "&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((0 0, -40 30, -10 70, 30 40, 0 0))", geometry);
    }

    @Test
    public void postThetaXY() throws Exception {
        HttpRequest request = HttpRequest.POST("/rotate/wkt/wkt" +
                "?theta=" + Math.toRadians(45) +
                "&x=1" +
                "&y=2", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((1.7071067811865475 -0.1213203435596426, -5.363961030678928 6.949747468305833, " +
                "1.7071067811865483 14.020815280171309, 8.778174593052023 6.949747468305832, " +
                "1.7071067811865475 -0.1213203435596426))", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/rotate/wkt/wkt" +
                "?x=2" +
                "&y=1" +
                "&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
