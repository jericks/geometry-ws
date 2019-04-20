package org.cugos.geometry.ws;

import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import static org.junit.Assert.*;

public class WKTGeometryWriterTest {

    @Test
    public void getName() {
        WKTGeometryWriter writer = new WKTGeometryWriter();
        assertEquals("wkt", writer.getName());
    }

    @Test
    public void isRegistered() {
        assertNotNull(GeometryWriters.find("wkt"));
    }

    @Test
    public void write() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry geometry = geometryFactory.createPoint(new Coordinate(-123.45, 42.56));
        WKTGeometryWriter writer = new WKTGeometryWriter();
        String wkt = writer.write(geometry);
        assertEquals("POINT (-123.45 42.56)", wkt);
    }

}
