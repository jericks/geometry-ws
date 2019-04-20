package org.cugos.geometry.ws;

import org.locationtech.jts.geom.Geometry;

public class WKTGeometryReader implements GeometryReader {

    private final org.locationtech.jts.io.WKTReader wktReader = new org.locationtech.jts.io.WKTReader();

    @Override
    public Geometry read(String str) throws Exception {
        return wktReader.read(str);
    }

    @Override
    public String getName() {
        return "wkt";
    }
}
