package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class MinimumBoundingCircleControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "MULTIPOINT ((12.27256417862947 12.73833434783841), "
        + "(13.737894633461437 7.658802439672621), "
        + "(6.857126638942733 8.821305316892328), "
        + "(9.260874914207697 13.087320259444919), "
        + "(8.017822881853032 7.492806794533148))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/minimumBoundingCircle/wkt/wkt?geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = GeometryReaders.find("wkt").read(geometryStr);
        assertTrue(geometry instanceof Polygon);
        assertFalse(geometry.isEmpty());
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/minimumBoundingCircle/wkt/wkt", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = GeometryReaders.find("wkt").read(geometryStr);
        assertTrue(geometry instanceof Polygon);
        assertFalse(geometry.isEmpty());
    }

}
