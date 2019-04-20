package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class HilbertCurveControllerTest extends AbstractControllerTest  {

    private final String polygon = "POLYGON ((-126.91406249999999 44.08758502824516, -116.3671875 44.08758502824516, -116.3671875 50.736455137010665, -126.91406249999999 50.736455137010665, -126.91406249999999 44.08758502824516))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/hilbertCurve/wkt/wkt?number=10&geom=" + URLEncoder.encode(polygon, "UTF-8"));
        String geometryStr = client.toBlocking().retrieve(request);
        System.out.println(geometryStr);
        assertEquals("LINESTRING (-124.96506005438275 44.08758502824516, -122.74877001812759 44.08758502824516, " +
            "-122.74877001812759 46.303875064500325, -124.96506005438275 46.303875064500325, " +
            "-124.96506005438275 48.520165100755484, -124.96506005438275 50.73645513701065, " +
            "-122.74877001812759 50.73645513701065, -122.74877001812759 48.520165100755484, " +
            "-120.53247998187241 48.520165100755484, -120.53247998187241 50.73645513701065, " +
            "-118.31618994561725 50.73645513701065, -118.31618994561725 48.520165100755484, " +
            "-118.31618994561725 46.303875064500325, -120.53247998187241 46.303875064500325, " +
            "-120.53247998187241 44.08758502824516, -118.31618994561725 44.08758502824516)", geometryStr);
    }
    
    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/hilbertCurve/wkt/wkt?number=10", polygon).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometryStr = client.toBlocking().retrieve(request);
        System.out.println(geometryStr);
        assertEquals("LINESTRING (-124.96506005438275 44.08758502824516, -122.74877001812759 44.08758502824516, " +
            "-122.74877001812759 46.303875064500325, -124.96506005438275 46.303875064500325, " +
            "-124.96506005438275 48.520165100755484, -124.96506005438275 50.73645513701065, " +
            "-122.74877001812759 50.73645513701065, -122.74877001812759 48.520165100755484, " +
            "-120.53247998187241 48.520165100755484, -120.53247998187241 50.73645513701065, " +
            "-118.31618994561725 50.73645513701065, -118.31618994561725 48.520165100755484, " +
            "-118.31618994561725 46.303875064500325, -120.53247998187241 46.303875064500325, " +
            "-120.53247998187241 44.08758502824516, -118.31618994561725 44.08758502824516)", geometryStr);
    }

}
