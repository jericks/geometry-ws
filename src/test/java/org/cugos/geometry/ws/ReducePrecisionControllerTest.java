package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class ReducePrecisionControllerTest extends AbstractControllerTest  {

    private final String inputGeometry =  "POINT (5.19775390625 51.07421875)";

    @Test
    public void getFloating() throws Exception {
        HttpRequest request = HttpRequest.GET("/reducePrecision/wkt/wkt" +
                "?geom=" + URLEncoder.encode(inputGeometry, "UTF-8") +
                "&type=floating" +
                "&scale=0.0" +
                "&pointwise=false" +
                "&removeCollapsed=false"
        );
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (5.19775390625 51.07421875)", geometry);
    }

    @Test
    public void postFloating() throws Exception {
        HttpRequest request = HttpRequest.POST("/reducePrecision/wkt/wkt" +
            "?type=floating" +
            "&scale=0.0" +
            "&pointwise=false" +
            "&removeCollapsed=false", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (5.19775390625 51.07421875)", geometry);
    }

    @Test
    public void getFixed() throws Exception {
        HttpRequest request = HttpRequest.GET("/reducePrecision/wkt/wkt" +
            "?geom=" + URLEncoder.encode(inputGeometry, "UTF-8") +
            "&type=fixed" +
            "&scale=10" +
            "&pointwise=false" +
            "&removeCollapsed=false"
        );
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (5.2 51.1)", geometry);
    }

    @Test
    public void postFixed() throws Exception {
        HttpRequest request = HttpRequest.POST("/reducePrecision/wkt/wkt" +
            "?type=fixed" +
            "&scale=10" +
            "&pointwise=false" +
            "&removeCollapsed=false", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (5.2 51.1)", geometry);
    }

    @Test
    public void getFloatingSingle() throws Exception {
        HttpRequest request = HttpRequest.GET("/reducePrecision/wkt/wkt" +
            "?geom=" + URLEncoder.encode(inputGeometry, "UTF-8") +
            "&type=floating_single" +
            "&scale=0" +
            "&pointwise=false" +
            "&removeCollapsed=false"
        );
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (5.19775390625 51.07421875)", geometry);
    }

    @Test
    public void postFloatingSingle() throws Exception {
        HttpRequest request = HttpRequest.POST("/reducePrecision/wkt/wkt" +
            "?type=floating_single" +
            "&scale=0" +
            "&pointwise=false" +
            "&removeCollapsed=false", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (5.19775390625 51.07421875)", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/reducePrecision/wkt/wkt" +
            "?geom=" + URLEncoder.encode(inputGeometry, "UTF-8") +
            "&type=BAD_TYPE" +
            "&scale=0.0" +
            "&pointwise=false" +
            "&removeCollapsed=false"
        );
        client.toBlocking().retrieve(request);
    }

}
