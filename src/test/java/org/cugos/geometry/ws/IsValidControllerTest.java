package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class IsValidControllerTest extends AbstractControllerTest  {

    private final String isValidGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";

    private final String isNotValidGeometry = "POLYGON ((17.06298828125 49.7998046875, "
        + "14.25048828125 44.04296875, 18.24951171875 44.04296875, "
        + "13.45947265625 48.1298828125, 17.06298828125 "
        + "49.7998046875))";

    @Test
    public void getIsNotValid() throws Exception {
        HttpRequest request = HttpRequest.GET("/isValid/wkt?geom=" + URLEncoder.encode(isNotValidGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void getIsValid() throws Exception {
        HttpRequest request = HttpRequest.GET("/isValid/wkt?geom=" + URLEncoder.encode(isValidGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }
    
    @Test
    public void postIsNotValid() throws Exception {
        HttpRequest request = HttpRequest.POST("/isValid/wkt", isNotValidGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void postIsValid() throws Exception {
        HttpRequest request = HttpRequest.POST("/isValid/wkt", isValidGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

}
