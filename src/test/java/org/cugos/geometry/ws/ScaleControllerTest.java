package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class ScaleControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/scale/wkt/wkt" +
                "?xScale=2" +
                "&yScale=5" +
                "&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((0 0, 0 50, 20 50, 20 0, 0 0))", geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/scale/wkt/wkt" +
                "?xScale=2" +
                "&yScale=5" +
                "&x=35" +
                "&y=30", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((-35 -120, -35 -70, -15 -70, -15 -120, -35 -120))", geometry);
    }

}
