package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class IsClosedControllerTest extends AbstractControllerTest  {

    private final String isClosedGeometry = "LINESTRING (1 1, 1 5, 5 5, 5 1, 1 1)";

    private final String isNotClosedGeometry = "LINESTRING (1 1, 5 5, 10 10)";

    @Test
    public void getIsNotClosed() throws Exception {
        HttpRequest request = HttpRequest.GET("/isClosed/wkt?geom=" + URLEncoder.encode(isNotClosedGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void getIsClosed() throws Exception {
        HttpRequest request = HttpRequest.GET("/isClosed/wkt?geom=" + URLEncoder.encode(isClosedGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }
    
    @Test
    public void postIsNotClosed() throws Exception {
        HttpRequest request = HttpRequest.POST("/isClosed/wkt", isNotClosedGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void postIsClosed() throws Exception {
        HttpRequest request = HttpRequest.POST("/isClosed/wkt", isClosedGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

}
