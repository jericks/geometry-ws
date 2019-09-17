package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class GetControllerTest extends AbstractControllerTest  {

    private final String geometry = "MULTIPOINT (0 0, 0 10, 10 10, 10 0, 0 0)";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/get/wkt/geojson" +
            "?index=0&geom=" + URLEncoder.encode(geometry, "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("{\"type\":\"Point\",\"coordinates\":[0.0,0.0],\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:0\"}}}", value);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/get/wkt/kml?index=1", geometry)
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("<Point>\n" +
                "  <coordinates>0.0,10.0</coordinates>\n" +
                "</Point>\n", geometry);
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/get/wkt/wkt?index=7&geom=" + URLEncoder.encode(geometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
