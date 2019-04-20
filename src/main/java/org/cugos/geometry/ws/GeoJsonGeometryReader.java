package org.cugos.geometry.ws;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonReader;

public class GeoJsonGeometryReader implements GeometryReader {

    private final GeoJsonReader geoJsonReader = new GeoJsonReader();

    @Override
    public Geometry read(String str) throws Exception {
        return geoJsonReader.read(str);
    }

    @Override
    public String getName() {
        return "geojson";
    }
}
