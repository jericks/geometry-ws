package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LineMergeControllerTest extends AbstractControllerTest  {

    private static final String inputGeometry = "MULTILINESTRING((-29 -27,-30 -29.7,-36 -31,-45 -33),(-45 -33,-46 -32))";

    private static final String expectedOutputGeometry = "MULTILINESTRING ((-29 -27, -30 -29.7, -36 -31, -45 -33, -46 -32))";
    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/lineMerge/wkt/wkt?geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(expectedOutputGeometry, geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/lineMerge/wkt/wkt", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(expectedOutputGeometry, geometry);
    }

}
