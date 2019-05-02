package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class IsEmptyControllerTest extends AbstractControllerTest  {

    private final String notEmptyGeometry = "POINT (-126.91406249999999 44.08758502824516)";

    private final String emptyGeometry = "POINT EMPTY";

    @Test
    public void getNotEmpty() throws Exception {
        HttpRequest request = HttpRequest.GET("/isEmpty/wkt?geom=" + URLEncoder.encode(notEmptyGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void getIsEmpty() throws Exception {
        HttpRequest request = HttpRequest.GET("/isEmpty/wkt?geom=" + URLEncoder.encode(emptyGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }
    
    @Test
    public void postNotEmpty() throws Exception {
        HttpRequest request = HttpRequest.POST("/isEmpty/wkt", notEmptyGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("false", geometry);
    }

    @Test
    public void postEmpty() throws Exception {
        HttpRequest request = HttpRequest.POST("/isEmpty/wkt", emptyGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("true", geometry);
    }

}
