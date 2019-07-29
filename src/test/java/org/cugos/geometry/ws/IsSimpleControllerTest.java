package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class IsSimpleControllerTest extends AbstractControllerTest  {

    private final String isSimpleGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";

    private final String isNotSimpleGeometry = "LINESTRING (8.14208984375 48.0419921875, "
        + "10.60302734375 51.6015625, 11.56982421875 47.91015625, "
        + "8.36181640625 50.72265625)";

    @Test
    public void getIsNotSimple() throws Exception {
        HttpRequest request = HttpRequest.GET("/isSimple/wkt?geom=" + URLEncoder.encode(isNotSimpleGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void getIsSimple() throws Exception {
        HttpRequest request = HttpRequest.GET("/isSimple/wkt?geom=" + URLEncoder.encode(isSimpleGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }
    
    @Test
    public void postIsNotSimple() throws Exception {
        HttpRequest request = HttpRequest.POST("/isSimple/wkt", isNotSimpleGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void postIsSimple() throws Exception {
        HttpRequest request = HttpRequest.POST("/isSimple/wkt", isSimpleGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

}
