package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnvelopeControllerTest extends AbstractControllerTest  {

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/envelope/wkt/wkt?geom=" + URLEncoder.encode("LINESTRING (-122.39168643951416 47.581331342700175, -122.38645076751709 47.5812734461813, -122.38683700561523 47.57562822813626)", "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((-122.39168643951416 47.57562822813626, -122.39168643951416 47.581331342700175, -122.38645076751709 47.581331342700175, -122.38645076751709 47.57562822813626, -122.39168643951416 47.57562822813626))", geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/envelope/wkt/wkt", "LINESTRING (-122.39168643951416 47.581331342700175, -122.38645076751709 47.5812734461813, -122.38683700561523 47.57562822813626)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((-122.39168643951416 47.57562822813626, -122.39168643951416 47.581331342700175, -122.38645076751709 47.581331342700175, -122.38645076751709 47.57562822813626, -122.39168643951416 47.57562822813626))", geometry);
    }

}
