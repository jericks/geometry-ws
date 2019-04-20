package org.cugos.geometry.ws;

import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GeoJsonGeometryWriterTest {

    @Test
    public void getName() {
        GeoJsonGeometryWriter writer = new GeoJsonGeometryWriter();
        assertEquals("geojson", writer.getName());
    }

    @Test
    public void isRegistered() {
        assertNotNull(GeometryWriters.find("geojson"));
    }

    @Test
    public void write() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry geometry = geometryFactory.createPoint(new Coordinate(-123.45, 42.56));
        GeoJsonGeometryWriter writer = new GeoJsonGeometryWriter();
        String geoJson = writer.write(geometry);
        assertEquals("{\"type\":\"Point\",\"coordinates\":[-123.45,42.56],\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:0\"}}}", geoJson);
    }

}
