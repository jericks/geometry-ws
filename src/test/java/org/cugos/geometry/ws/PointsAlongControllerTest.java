package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PointsAlongControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "LINESTRING (0 0, 10 10)";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/pointsAlong/wkt/wkt?distance=1&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("MULTIPOINT ((0 0), (0.7071067811865475 0.7071067811865475), (1.414213562373095 1.414213562373095), " +
                "(2.1213203435596424 2.1213203435596424), (2.82842712474619 2.82842712474619), (3.5355339059327373 3.5355339059327373), " +
                "(4.242640687119285 4.242640687119285), (4.949747468305833 4.949747468305833), (5.65685424949238 5.65685424949238), " +
                "(6.363961030678928 6.363961030678928), (7.071067811865475 7.071067811865475), (7.778174593052023 7.778174593052023), " +
                "(8.48528137423857 8.48528137423857), (9.192388155425117 9.192388155425117), (9.899494936611665 9.899494936611665))", geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/pointsAlong/wkt/wkt?distance=2", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("MULTIPOINT ((0 0), (1.414213562373095 1.414213562373095), " +
                "(2.82842712474619 2.82842712474619), (4.242640687119285 4.242640687119285), " +
                "(5.65685424949238 5.65685424949238), (7.071067811865475 7.071067811865475), " +
                "(8.48528137423857 8.48528137423857), (9.899494936611665 9.899494936611665))", geometry);
    }

}
