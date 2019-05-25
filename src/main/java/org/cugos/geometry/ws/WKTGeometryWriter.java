package org.cugos.geometry.ws;

import org.locationtech.jts.geom.Geometry;

public class WKTGeometryWriter implements GeometryWriter {

    private final org.locationtech.jts.io.WKTWriter wktWriter = new org.locationtech.jts.io.WKTWriter();

    @Override
    public String write(Geometry geometry) {
        return wktWriter.write(geometry);
    }

    @Override
    public String getName() {
        return "wkt";
    }

    @Override
    public String getMediaType() {
        return "text/plain";
    }
}
