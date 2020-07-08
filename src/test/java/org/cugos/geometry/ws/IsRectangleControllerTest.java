package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IsRectangleControllerTest extends AbstractControllerTest  {

    private final String rectangularGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";

    private final String nonRectangularGeometry = "POLYGON ((0 0, 0 10, 10 0, 0 0))";

    @Test
    public void getNotRectangle() throws Exception {
        HttpRequest request = HttpRequest.GET("/isRectangle/wkt?geom=" + URLEncoder.encode(nonRectangularGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void getIsRectangle() throws Exception {
        HttpRequest request = HttpRequest.GET("/isRectangle/wkt?geom=" + URLEncoder.encode(rectangularGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }
    
    @Test
    public void postNotRectangle() throws Exception {
        HttpRequest request = HttpRequest.POST("/isRectangle/wkt", nonRectangularGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void postRectangle() throws Exception {
        HttpRequest request = HttpRequest.POST("/isRectangle/wkt", rectangularGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

}
