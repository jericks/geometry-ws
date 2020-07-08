package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EndPointControllerTest extends AbstractControllerTest  {

    private final String lineString = "LINESTRING (1 1, 10 10)";

    private final String multiLineString = "MULTILINESTRING ((1 1, 10 10), (5 5, 20 20))";

    private final String point = "POINT (1 1)";

    @Test
    public void getLineString() throws Exception {
        HttpRequest request = HttpRequest.GET("/endPoint/wkt/wkt?geom=" + URLEncoder.encode(lineString, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (10 10)", geometry);
    }

    @Test
    public void getMultiLineString() throws Exception {
        HttpRequest request = HttpRequest.GET("/endPoint/wkt/wkt?geom=" + URLEncoder.encode(multiLineString, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (20 20)", geometry);
    }

    @Test
    public void postLineString() throws Exception {
        HttpRequest request = HttpRequest.POST("/endPoint/wkt/wkt", lineString).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (10 10)", geometry);
    }

    @Test
    public void postMultiLineString() throws Exception {
        HttpRequest request = HttpRequest.POST("/endPoint/wkt/wkt", multiLineString).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (20 20)", geometry);
    }

    @Test
    public void badRequest() throws Exception {
        Assertions.assertThrows(HttpClientException.class, () -> {
            HttpRequest request = HttpRequest.GET("/endPoint/wkt/wkt?geom=" + URLEncoder.encode(point, "UTF-8"));
            client.toBlocking().retrieve(request);
        });
    }
    
}
