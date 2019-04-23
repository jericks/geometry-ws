package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class KochSnowflakeControllerTest extends AbstractControllerTest  {

    private final String polygon = "POLYGON ((-126.91406249999999 44.08758502824516, -116.3671875 44.08758502824516, -116.3671875 50.736455137010665, -126.91406249999999 50.736455137010665, -126.91406249999999 44.08758502824516))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/kochSnowflake/wkt/wkt?number=20&geom=" + URLEncoder.encode(polygon, "UTF-8"));
        String geometryStr = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((-123.85691503625516 47.92631197534777, -124.96506005438275 49.84567544889907, " +
            "-122.74877001812759 49.84567544889908, -121.640625 51.76503892245039, -120.53247998187241 49.84567544889908, " +
            "-118.31618994561725 49.84567544889907, -119.42433496374484 47.92631197534777, -118.31618994561725 46.006948501796465, " +
            "-120.53247998187241 46.006948501796465, -121.640625 44.08758502824516, -122.74877001812759 46.006948501796465, " +
            "-124.96506005438275 46.006948501796465, -123.85691503625516 47.92631197534777))", geometryStr);
    }
    
    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/kochSnowflake/wkt/wkt?number=30", polygon).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometryStr = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((-123.85691503625516 47.92631197534777, -124.96506005438275 49.84567544889907, " +
            "-122.74877001812759 49.84567544889908, -121.640625 51.76503892245039, -120.53247998187241 49.84567544889908, " +
            "-118.31618994561725 49.84567544889907, -119.42433496374484 47.92631197534777, -118.31618994561725 46.006948501796465, " +
            "-120.53247998187241 46.006948501796465, -121.640625 44.08758502824516, -122.74877001812759 46.006948501796465, " +
            "-124.96506005438275 46.006948501796465, -123.85691503625516 47.92631197534777))", geometryStr);
    }

}
