package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class NarrowControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "GEOMETRYCOLLECTION (POINT (1 1), POINT(10 10))";

    private final String expectedGeometry = "MULTIPOINT ((1 1), (10 10))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/narrow/wkt/wkt?geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(expectedGeometry, geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/narrow/wkt/wkt", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(expectedGeometry, geometry);
    }

}
