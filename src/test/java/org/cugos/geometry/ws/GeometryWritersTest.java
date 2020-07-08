package org.cugos.geometry.ws;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GeometryWritersTest {

    @Test
    public void find() {
        assertNotNull(GeometryWriters.find("wkt"));
        assertNotNull(GeometryWriters.find("geojson"));
        assertNotNull(GeometryWriters.find("kml"));
        assertNotNull(GeometryWriters.find("gml2"));
        assertNull(GeometryWriters.find("asdf"));
    }

    @Test
    public void list() {
        List<GeometryWriter> geometryWriters = GeometryWriters.list();
        assertFalse(geometryWriters.isEmpty());
        assertTrue(geometryWriters.stream().filter(reader -> reader.getName().equalsIgnoreCase("wkt")).findFirst().isPresent());
        assertTrue(geometryWriters.stream().filter(reader -> reader.getName().equalsIgnoreCase("geojson")).findFirst().isPresent());
        assertTrue(geometryWriters.stream().filter(reader -> reader.getName().equalsIgnoreCase("kml")).findFirst().isPresent());
        assertTrue(geometryWriters.stream().filter(reader -> reader.getName().equalsIgnoreCase("gml2")).findFirst().isPresent());
        assertFalse(geometryWriters.stream().filter(reader -> reader.getName().equalsIgnoreCase("asdf")).findFirst().isPresent());
    }

}
