package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

import java.net.URLEncoder;

import static org.junit.Assert.*;

public class RandomWalkControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/randomWalk/wkt/wkt" +
            "?geom=" + URLEncoder.encode(inputGeometry, "UTF-8") +
            "&number=10" +
            "&angle=45" +
            "&distance=10" +
            "&change=0.80"
        );
        String wkt = client.toBlocking().retrieve(request);
        Geometry geometry = GeometryReaders.find("wkt").read(wkt);
        assertTrue(geometry instanceof LineString);
        assertEquals(11, geometry.getNumPoints());
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/randomWalk/wkt/geojson" +
            "?number=10" +
            "&angle=45" +
            "&distance=10" +
            "&change=0.80",
            inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geojson = client.toBlocking().retrieve(request);
        Geometry geometry = GeometryReaders.find("geojson").read(geojson);
        assertTrue(geometry instanceof LineString);
        assertEquals(11, geometry.getNumPoints());
    }

}
