package org.cugos.geometry.ws;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WKTGeometryReaderTest {

    @Test
    public void getName() {
        WKTGeometryReader reader = new WKTGeometryReader();
        assertEquals("wkt", reader.getName());
    }

    @Test
    public void isRegistered() {
        assertNotNull(GeometryReaders.find("wkt"));
    }

    @Test
    public void read() throws Exception {
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry expectedGeometry = geometryFactory.createPoint(new Coordinate(-123.45, 42.56));
        String wkt = "POINT (-123.45 42.56)";
        WKTGeometryReader reader = new WKTGeometryReader();
        Geometry actualGeometry = reader.read(wkt);
        assertEquals(expectedGeometry, actualGeometry);
    }

}
