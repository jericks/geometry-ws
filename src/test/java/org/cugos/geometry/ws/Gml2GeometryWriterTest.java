package org.cugos.geometry.ws;

import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Gml2GeometryWriterTest {

    @Test
    public void getName() {
        Gml2GeometryWriter writer = new Gml2GeometryWriter();
        assertEquals("gml2", writer.getName());
    }

    @Test
    public void isRegistered() {
        assertNotNull(GeometryWriters.find("gml2"));
    }

    @Test
    public void write() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry geometry = geometryFactory.createPoint(new Coordinate(-123.45, 42.56));
        Gml2GeometryWriter writer = new Gml2GeometryWriter();
        String gml2 = writer.write(geometry);
        assertEquals("<gml:Point>\n" +
            "  <gml:coordinates>\n" +
            "    -123.45,42.56 \n" +
            "  </gml:coordinates>\n" +
            "</gml:Point>\n", gml2);
    }

}
