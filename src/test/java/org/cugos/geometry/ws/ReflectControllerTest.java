package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class ReflectControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";

    @Test
    public void getXY() throws Exception {
        HttpRequest request = HttpRequest.GET("/reflect/wkt/wkt" +
                "?x0=5" +
                "&y0=2" +
                "&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((0 0, 6.8965517241379315 -7.241379310344829, "
                + "14.137931034482762 -0.3448275862068977, "
                + "7.241379310344829 6.8965517241379315, 0 0))", geometry);
    }

    @Test
    public void getXYXY() throws Exception {
        HttpRequest request = HttpRequest.GET("/reflect/wkt/wkt" +
                "?x0=5" +
                "&y0=2" +
                "&x1=2" +
                "&y1=1" +
                "&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((-0.1999999999999993 0.5999999999999996, 5.800000000000001 -7.3999999999999995, " +
                "13.8 -1.3999999999999995, 7.8 6.6, " +
                "-0.1999999999999993 0.5999999999999996))", geometry);
    }

    @Test
    public void postXY() throws Exception {
        HttpRequest request = HttpRequest.POST("/reflect/wkt/wkt" +
                "?x0=5" +
                "&y0=2", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((0 0, 6.8965517241379315 -7.241379310344829, "
                + "14.137931034482762 -0.3448275862068977, "
                + "7.241379310344829 6.8965517241379315, 0 0))", geometry);
    }

    @Test
    public void postXYXY() throws Exception {
        HttpRequest request = HttpRequest.POST("/reflect/wkt/wkt" +
                "?x0=5" +
                "&y0=2" +
                "&x1=2" +
                "&y1=1", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((-0.1999999999999993 0.5999999999999996, 5.800000000000001 -7.3999999999999995, " +
                "13.8 -1.3999999999999995, 7.8 6.6, " +
                "-0.1999999999999993 0.5999999999999996))", geometry);
    }

}
