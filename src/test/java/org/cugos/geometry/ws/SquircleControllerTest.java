package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SquircleControllerTest extends AbstractControllerTest  {

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/squircle/wkt/wkt" +
                "?geom=" + URLEncoder.encode("POINT (100 100)", "UTF-8") +
                "&width=50" +
                "&height=40" +
                "&numberOfPoints=10" +
                "&rotation=0" +
                "&center=false"
        );
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((125 140, 141.8179283050743 136.8179283050743, "
                + "145 120, 141.8179283050743 103.18207169492571, "
                + "125 100, 108.18207169492571 103.18207169492571, 105 120, "
                + "108.18207169492571 136.8179283050743, 125 140))", geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/squircle/wkt/wkt" +
                "?width=50" +
                "&height=40" +
                "&numberOfPoints=10" +
                "&rotation=0" +
                "&center=false"
                , "POINT (100 100)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((125 140, 141.8179283050743 136.8179283050743, "
                + "145 120, 141.8179283050743 103.18207169492571, "
                + "125 100, 108.18207169492571 103.18207169492571, 105 120, "
                + "108.18207169492571 136.8179283050743, 125 140))", geometry);
    }

}
