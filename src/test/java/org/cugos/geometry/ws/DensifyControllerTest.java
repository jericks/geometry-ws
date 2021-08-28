package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DensifyControllerTest extends AbstractControllerTest  {

    private final String polygon = "POLYGON ((-126.91406249999999 44.08758502824516, -116.3671875 44.08758502824516, " +
            "-116.3671875 50.736455137010665, -126.91406249999999 50.736455137010665, " +
            "-126.91406249999999 44.08758502824516))";

    private final String densifiedPolygon = "POLYGON ((-126.91406249999999 44.08758502824516, " +
            "-126.43465909090908 44.08758502824516, -125.95525568181817 44.08758502824516, " +
            "-125.47585227272727 44.08758502824516, -124.99644886363635 44.08758502824516, " +
            "-124.51704545454544 44.08758502824516, -124.03764204545453 44.08758502824516, " +
            "-123.55823863636363 44.08758502824516, -123.07883522727272 44.08758502824516, " +
            "-122.59943181818181 44.08758502824516, -122.1200284090909 44.08758502824516, " +
            "-121.640625 44.08758502824516, -121.16122159090908 44.08758502824516, " +
            "-120.68181818181817 44.08758502824516, -120.20241477272727 44.08758502824516, " +
            "-119.72301136363636 44.08758502824516, -119.24360795454545 44.08758502824516, " +
            "-118.76420454545455 44.08758502824516, -118.28480113636363 44.08758502824516, " +
            "-117.80539772727272 44.08758502824516, -117.32599431818181 44.08758502824516, " +
            "-116.8465909090909 44.08758502824516, -116.3671875 44.08758502824516, " +
            "-116.3671875 44.56250432172841, -116.3671875 45.03742361521166, -116.3671875 45.51234290869491, " +
            "-116.3671875 45.98726220217816, -116.3671875 46.46218149566141, -116.3671875 46.93710078914466, " +
            "-116.3671875 47.41202008262791, -116.3671875 47.886939376111165, -116.3671875 48.36185866959441, " +
            "-116.3671875 48.836777963077665, -116.3671875 49.31169725656091, -116.3671875 49.786616550044165, " +
            "-116.3671875 50.26153584352741, -116.3671875 50.736455137010665, -116.8465909090909 50.736455137010665, " +
            "-117.32599431818181 50.736455137010665, -117.80539772727272 50.736455137010665, " +
            "-118.28480113636364 50.736455137010665, -118.76420454545455 50.736455137010665, " +
            "-119.24360795454545 50.736455137010665, -119.72301136363636 50.736455137010665, " +
            "-120.20241477272727 50.736455137010665, -120.68181818181817 50.736455137010665, " +
            "-121.16122159090908 50.736455137010665, -121.640625 50.736455137010665, " +
            "-122.1200284090909 50.736455137010665, -122.59943181818181 50.736455137010665, " +
            "-123.07883522727272 50.736455137010665, -123.55823863636363 50.736455137010665, " +
            "-124.03764204545453 50.736455137010665, -124.51704545454544 50.736455137010665, " +
            "-124.99644886363635 50.736455137010665, -125.47585227272727 50.736455137010665, " +
            "-125.95525568181817 50.736455137010665, -126.43465909090908 50.736455137010665, " +
            "-126.91406249999999 50.736455137010665, -126.91406249999999 50.26153584352741, " +
            "-126.91406249999999 49.786616550044165, -126.91406249999999 49.31169725656091, " +
            "-126.91406249999999 48.836777963077665, -126.91406249999999 48.36185866959441, " +
            "-126.91406249999999 47.886939376111165, -126.91406249999999 47.41202008262791, " +
            "-126.91406249999999 46.93710078914466, -126.91406249999999 46.46218149566141, " +
            "-126.91406249999999 45.98726220217816, -126.91406249999999 45.51234290869491, " +
            "-126.91406249999999 45.03742361521166, -126.91406249999999 44.56250432172841, " +
            "-126.91406249999999 44.08758502824516))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/densify/wkt/wkt?distance=0.5&geom=" + URLEncoder.encode(polygon, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(densifiedPolygon, geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/densify/wkt/wkt?distance=0.5", polygon).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(densifiedPolygon, geometry);
    }

}
