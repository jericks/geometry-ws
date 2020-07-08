package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import static org.junit.jupiter.api.Assertions.*;

public class BufferControllerTest extends AbstractControllerTest {

    @Test
    public void bufferGet() throws Exception {
        HttpRequest request = HttpRequest.GET("/buffer/wkt/wkt?geom=POINT(1%201)&d=10");
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = GeometryReaders.find("wkt").read(geometryStr);
        assertTrue(geometry instanceof Polygon);
        assertFalse(geometry.isEmpty());
    }

    @Test
    public void bufferPost() throws Exception {
        HttpRequest request = HttpRequest.POST("/buffer/wkt/wkt/?d=10", "POINT (1 1)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometryStr = client.toBlocking().retrieve(request);
        Geometry geometry = GeometryReaders.find("wkt").read(geometryStr);
        assertTrue(geometry instanceof Polygon);
        assertFalse(geometry.isEmpty());
    }

}
