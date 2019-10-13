package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class RectangleControllerTest extends AbstractControllerTest  {

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/rectangle/wkt/wkt" +
                "?geom=" + URLEncoder.encode("POINT (100 100)", "UTF-8") +
                "&width=50" +
                "&height=40" +
                "&numberOfPoints=10" +
                "&rotation=0" +
                "&center=false"
        );
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((100 100, 125 100, 150 100, 150 120, 150 140, "
                + "125 140, 100 140, 100 120, 100 100))", geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/rectangle/wkt/wkt" +
                "?width=50" +
                "&height=40" +
                "&numberOfPoints=10" +
                "&rotation=0" +
                "&center=false"
                , "POINT (100 100)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((100 100, 125 100, 150 100, 150 120, 150 140, "
                + "125 140, 100 140, 100 120, 100 100))", geometry);
    }

}
