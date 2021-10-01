package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

import static org.junit.jupiter.api.Assertions.*;

public class FixControllerTest extends AbstractControllerTest {

    @Test
    public void fixGet() throws Exception {
        HttpRequest request = HttpRequest.GET("/fix/wkt/wkt?geom=LINESTRING(0%200,%200%200,%200%200,%201%201)");
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = GeometryReaders.find("wkt").read(geometryStr);
        assertTrue(geometry instanceof LineString);
        assertEquals("LINESTRING (0 0, 1 1)", GeometryWriters.find("wkt").write(geometry));
    }

    @Test
    public void fixPost() throws Exception {
        HttpRequest request = HttpRequest.POST("/fix/wkt/wkt/?d=10", "LINESTRING (0 0, 0 0, 0 0, 1 1)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = GeometryReaders.find("wkt").read(geometryStr);
        assertTrue(geometry instanceof LineString);
        assertEquals("LINESTRING (0 0, 1 1)", GeometryWriters.find("wkt").write(geometry));
    }

}
