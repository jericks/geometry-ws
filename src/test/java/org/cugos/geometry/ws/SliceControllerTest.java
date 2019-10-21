package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class SliceControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "MULTIPOINT ((1 1), (2 2), (3 3), (4 4), (5 5))";

    @Test
    public void getOneToThree() throws Exception {
        HttpRequest request = HttpRequest.GET("/slice/wkt/wkt?start=1&end=3&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("MULTIPOINT ((2 2), (3 3))", geometry);
    }

    @Test
    public void getTwo() throws Exception {
        HttpRequest request = HttpRequest.GET("/slice/wkt/wkt?start=2&end=5&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("MULTIPOINT ((3 3), (4 4), (5 5))", geometry);
    }

    @Test
    public void getNegativeThree() throws Exception {
        HttpRequest request = HttpRequest.GET("/slice/wkt/wkt?start=-3&end=5&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("MULTIPOINT ((3 3), (4 4), (5 5))", geometry);
    }

    @Test
    public void postNegativeFourToTwo() throws Exception {
        HttpRequest request = HttpRequest.POST("/slice/wkt/wkt?start=-4&end=-2", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("MULTIPOINT ((2 2), (3 3))", geometry);
    }

    @Test
    public void postZeroToNegativeTwo() throws Exception {
        HttpRequest request = HttpRequest.POST("/slice/wkt/wkt?start=0&end=-2", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("MULTIPOINT ((1 1), (2 2), (3 3))", geometry);
    }

}
