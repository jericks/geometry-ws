package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class DistanceLineControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "POINT (1 1)";

    private final String otherGeometry = "POINT (20 23)";

    private final String lineStringGeometry = "LINESTRING (1 1, 20 23)";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/distanceLine/wkt/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(inputGeometry, otherGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals(lineStringGeometry, value);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/distanceLine/wkt/wkt", Geometries.geometryCollection(inputGeometry, otherGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(lineStringGeometry, geometry);
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/distanceLine/wkt/wkt?geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
