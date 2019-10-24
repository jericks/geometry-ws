package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class PointAtAngleControllerTest extends AbstractControllerTest  {

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/pointAtAngle/wkt/wkt" +
                "?angle=90" +
                "&distance=10" +
                "&geom=" + URLEncoder.encode("POINT (10 10)", "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (10 20)", geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/pointAtAngle/wkt/wkt?angle=45&distance=5", "POINT (10 10)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (13.535533905932738 13.535533905932738)", geometry);
    }

}
