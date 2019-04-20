package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class InteriorPointControllerTest extends AbstractControllerTest {

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/interiorPoint/wkt/wkt?geom=" + URLEncoder.encode("POLYGON ((-126.91406249999999 44.08758502824516, -116.3671875 44.08758502824516, -116.3671875 50.736455137010665, -126.91406249999999 50.736455137010665, -126.91406249999999 44.08758502824516))", "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (-121.640625 47.41202008262791)", geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/interiorPoint/wkt/wkt", "POLYGON ((-126.91406249999999 44.08758502824516, -116.3671875 44.08758502824516, -116.3671875 50.736455137010665, -126.91406249999999 50.736455137010665, -126.91406249999999 44.08758502824516))").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (-121.640625 47.41202008262791)", geometry);
    }

}
