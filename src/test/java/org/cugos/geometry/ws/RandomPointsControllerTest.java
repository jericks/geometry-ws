package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class RandomPointsControllerTest extends AbstractControllerTest  {

    private final String polygon = "POLYGON ((-126.91406249999999 44.08758502824516, -116.3671875 44.08758502824516, -116.3671875 50.736455137010665, -126.91406249999999 50.736455137010665, -126.91406249999999 44.08758502824516))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/randomPoints/wkt/wkt?number=10&geom=" + URLEncoder.encode(polygon, "UTF-8"));
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = new WKTGeometryReader().read(geometryStr);
        assertEquals(10, geometry.getNumGeometries());
    }

    @Test
    public void getGridded() throws Exception {
        HttpRequest request = HttpRequest.GET("/randomPoints/wkt/wkt" +
            "?number=10" +
            "&gridded=true" + 
            "&geom=" + URLEncoder.encode(polygon, "UTF-8"));
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = new WKTGeometryReader().read(geometryStr);
        assertEquals(16, geometry.getNumGeometries());
    }

    @Test
    public void getGriddedAndConstrained() throws Exception {
        HttpRequest request = HttpRequest.GET("/randomPoints/wkt/wkt" +
            "?number=10" +
            "&gridded=true" +
            "&constrainedToCircle=true" +
            "&geom=" + URLEncoder.encode(polygon, "UTF-8"));
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = new WKTGeometryReader().read(geometryStr);
        assertEquals(16, geometry.getNumGeometries());
    }

    @Test
    public void getGriddedAndConstrainedWithGutter() throws Exception {
        HttpRequest request = HttpRequest.GET("/randomPoints/wkt/wkt" +
            "?number=10" +
            "&gridded=true" +
            "&constrainedToCircle=true" +
            "&gutterFraction=0.5" +
            "&geom=" + URLEncoder.encode(polygon, "UTF-8"));
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = new WKTGeometryReader().read(geometryStr);
        assertEquals(16, geometry.getNumGeometries());
    }

    @Test
    public void getWithLineString() throws Exception {
        HttpRequest request = HttpRequest.GET("/randomPoints/wkt/wkt?number=5&geom=" + URLEncoder.encode("LINESTRING (1 1, 5 5, 10 10)", "UTF-8"));
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = new WKTGeometryReader().read(geometryStr);
        assertEquals(5, geometry.getNumGeometries());
    }
    
    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/randomPoints/wkt/wkt?number=10", polygon).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = new WKTGeometryReader().read(geometryStr);
        assertEquals(10, geometry.getNumGeometries());
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/randomPoints/wkt/wkt?number=10&geom=" + URLEncoder.encode("POINT (1 2)", "UTF-8"));
        client.toBlocking().retrieve(request);
    }
}
