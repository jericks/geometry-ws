package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class ReverseControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "LINESTRING (0 0, 5 5, 10 10)";

    private final String reversedGeometry = "LINESTRING (10 10, 5 5, 0 0)";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/reverse/wkt/wkt?geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(reversedGeometry, geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/reverse/wkt/wkt", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(reversedGeometry, geometry);
    }

}
