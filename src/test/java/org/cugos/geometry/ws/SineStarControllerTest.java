package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class SineStarControllerTest extends AbstractControllerTest  {

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/sineStar/wkt/wkt" +
                "?geom=" + URLEncoder.encode("POINT (10 10)", "UTF-8") +
                "&width=50" +
                "&height=40" +
                "&numberOfPoints=10" +
                "&rotation=0" +
                "&center=false" +
                "&numberOfArms=5" +
                "&armLengthRatio=5"
        );
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((35 50, 51.81792830507429 46.81792830507429, " +
                "55 30, 51.81792830507429 13.182071694925707, 35 10, 18.182071694925707 13.18207169492571, " +
                "15 30, 18.18207169492571 46.81792830507429, 35 50))", geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/sineStar/wkt/wkt" +
                "?width=50" +
                "&height=40" +
                "&numberOfPoints=10" +
                "&rotation=0" +
                "&center=false" +
                "&numberOfArms=5" +
                "&armLengthRatio=5"
                , "POINT (10 10)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((35 50, 51.81792830507429 46.81792830507429, " +
                "55 30, 51.81792830507429 13.182071694925707, 35 10, 18.182071694925707 13.18207169492571, " +
                "15 30, 18.18207169492571 46.81792830507429, 35 50))", geometry);
    }

}
