package org.cugos.geometry.ws;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GeoJsonGeometryReaderTest {

    @Test
    public void getName() {
        GeoJsonGeometryReader reader = new GeoJsonGeometryReader();
        assertEquals("geojson", reader.getName());
    }

    @Test
    public void isRegistered() {
        assertNotNull(GeometryReaders.find("geojson"));
    }

    @Test
    public void read() throws Exception {
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry expectedGeometry = geometryFactory.createPoint(new Coordinate(-123.45, 42.56));
        String geojson = "{\"type\":\"Point\",\"coordinates\":[-123.45,42.56],\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:0\"}}}";
        GeoJsonGeometryReader reader = new GeoJsonGeometryReader();
        Geometry actualGeometry = reader.read(geojson);
        assertEquals(expectedGeometry, actualGeometry);
    }

}
