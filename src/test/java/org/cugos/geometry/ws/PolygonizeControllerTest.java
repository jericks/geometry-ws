package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.WKTReader;

import java.net.URLEncoder;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class PolygonizeControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "MULTILINESTRING ((-5.70068359375 45.1416015625, "
            + "-4.6 46.4), (-4.6 46.4, 1 52.2), (1 52.2, 2.47314453125 "
            + "53.9306640625), (-1.21826171875 53.9306640625, 1 52.2), "
            + "(1 52.2, 5.6 48.6), (5.6 48.6, 8.88916015625 46.1962890625), "
            + "(0.71533203125 42.63671875, 1.8 44), (1.8 44, 5.6 48.6), "
            + "(5.6 48.6, 7.13134765625 50.37109375), (-5.83251953125 "
            + "46.943359375, -4.6 46.4), (-4.6 46.4, 1.8 44), (1.8 44, "
            + "4.45068359375 42.98828125))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/polygonize/wkt/wkt" +
                "?geom=" + URLEncoder.encode(inputGeometry, "UTF-8") +
                "&report=false"
        );
        String geometry = client.toBlocking().retrieve(request);
        Geometry outputGeometry = new WKTReader().read(geometry);
        assertTrue(outputGeometry instanceof MultiPolygon);
        assertEquals(1, outputGeometry.getNumGeometries());
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/polygonize/wkt/wkt?report=true", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        Geometry outputGeometry = new WKTReader().read(geometry);
        assertTrue(outputGeometry instanceof GeometryCollection);
        assertEquals(4, outputGeometry.getNumGeometries());
        assertFalse(outputGeometry.getGeometryN(0).isEmpty());
        assertTrue(outputGeometry.getGeometryN(1).isEmpty());
        assertFalse(outputGeometry.getGeometryN(2).isEmpty());
        assertTrue(outputGeometry.getGeometryN(3).isEmpty());
    }

}
