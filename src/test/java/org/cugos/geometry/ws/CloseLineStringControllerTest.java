package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class CloseLineStringControllerTest extends AbstractControllerTest  {

    @Test
    public void get() throws Exception {
        String inputGeometry = "LINESTRING (0 0, 0 4, 4 4, 4 0)";
        String resultGeometry = "LINEARRING (0 0, 0 4, 4 4, 4 0, 0 0)";
        HttpRequest request = HttpRequest.GET("/closeLineString/wkt/wkt?unique=true&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(resultGeometry, geometry);
    }
    
    @Test
    public void post() throws Exception {
        String inputGeometry = "LINESTRING (0 0, 0 4, 4 4, 4 0)";
        String resultGeometry = "LINEARRING (0 0, 0 4, 4 4, 4 0, 0 0)";
        HttpRequest request = HttpRequest.POST("/closeLineString/wkt/wkt?unique=false", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(resultGeometry, geometry);
    }

}
