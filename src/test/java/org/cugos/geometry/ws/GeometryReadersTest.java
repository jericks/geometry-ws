package org.cugos.geometry.ws;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GeometryReadersTest {

    @Test
    public void find() {
        assertNotNull(GeometryReaders.find("wkt"));
        assertNotNull(GeometryReaders.find("geojson"));
        assertNull(GeometryReaders.find("asdf"));
    }

    @Test
    public void list() {
        List<GeometryReader> geometryReaders = GeometryReaders.list();
        assertFalse(geometryReaders.isEmpty());
        assertTrue(geometryReaders.stream().filter(reader -> reader.getName().equalsIgnoreCase("wkt")).findFirst().isPresent());
        assertTrue(geometryReaders.stream().filter(reader -> reader.getName().equalsIgnoreCase("geojson")).findFirst().isPresent());
        assertFalse(geometryReaders.stream().filter(reader -> reader.getName().equalsIgnoreCase("asdf")).findFirst().isPresent());
    }

}
