package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountPointsControllerTest extends AbstractControllerTest  {

    private final String polygon = "POLYGON ((-126.91406249999999 44.08758502824516, -116.3671875 44.08758502824516, -116.3671875 50.736455137010665, -126.91406249999999 50.736455137010665, -126.91406249999999 44.08758502824516))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/countPoints/wkt?geom=" + URLEncoder.encode(polygon, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("5.0", geometry);
    }
    
    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/countPoints/wkt", polygon).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("5.0", geometry);
    }

}
