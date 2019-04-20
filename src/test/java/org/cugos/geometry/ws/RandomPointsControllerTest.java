package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
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
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/randomPoints/wkt/wkt?number=10", polygon).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = new WKTGeometryReader().read(geometryStr);
        assertEquals(10, geometry.getNumGeometries());
    }

}
