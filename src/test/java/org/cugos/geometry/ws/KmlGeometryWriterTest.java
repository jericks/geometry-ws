package org.cugos.geometry.ws;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class KmlGeometryWriterTest {

    @Test
    public void getName() {
        KmlGeometryWriter writer = new KmlGeometryWriter();
        assertEquals("kml", writer.getName());
    }

    @Test
    public void isRegistered() {
        assertNotNull(GeometryWriters.find("kml"));
    }

    @Test
    public void write() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry geometry = geometryFactory.createPoint(new Coordinate(-123.45, 42.56));
        KmlGeometryWriter writer = new KmlGeometryWriter();
        String kml = writer.write(geometry);
        assertEquals("<Point>\n" +
            "  <coordinates>-123.45,42.56</coordinates>\n" +
            "</Point>\n", kml);
    }

}
