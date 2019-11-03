package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class ProjectControllerTest extends AbstractControllerTest  {

    @Test
    public void getEPSG() throws Exception {
        HttpRequest request = HttpRequest.GET("/project/wkt/wkt?source=EPSG:2927&target=EPSG:4326&geom=" + URLEncoder.encode("POINT (1186683.01 641934.58)", "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (-122.32131937934592 47.07927009358412)", geometry);
    }

    @Test
    public void getName() throws Exception {
        HttpRequest request = HttpRequest.GET("/project/wkt/wkt" +
                "?source=EPSG:2927" +
                "&target=" + URLEncoder.encode("+title=long/lat:WGS84 +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees", "UTF-8") +
                "&geom=" + URLEncoder.encode("POINT (1186683.01 641934.58)", "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (-122.32131937934592 47.07927009358412)", geometry);
    }

    @Test
    public void postEPSD() throws Exception {
        HttpRequest request = HttpRequest.POST("/project/wkt/wkt?source=EPSG:2927&target=EPSG:4326", "POINT (1186683.01 641934.58)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (-122.32131937934592 47.07927009358412)", geometry);
    }

}
