package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class VariableBufferControllerTest extends AbstractControllerTest {

    private final String inputGeometry = "LINESTRING (0 1, 2 3, 4 5)";

    private final String distances = "5,10,15";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/variableBuffer/wkt/wkt" +
                "?geom=" + URLEncoder.encode(inputGeometry, "UTF-8") +
                "&distances=" + distances);
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = GeometryReaders.find("wkt").read(geometryStr);
        assertNotNull(geometry);
        assertTrue(geometry instanceof Polygon);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/variableBuffer/wkt/wkt?distances=" + distances, inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = GeometryReaders.find("wkt").read(geometryStr);
        assertNotNull(geometry);
        assertTrue(geometry instanceof Polygon);
    }

}
