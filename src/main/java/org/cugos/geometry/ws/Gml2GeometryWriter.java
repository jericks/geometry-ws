package org.cugos.geometry.ws;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.gml2.GMLWriter;

public class Gml2GeometryWriter implements GeometryWriter {

    private final GMLWriter gmlWriter = new GMLWriter();

    @Override
    public String write(Geometry geometry) {
        return gmlWriter.write(geometry);
    }

    @Override
    public String getName() {
        return "gml2";
    }

    @Override
    public String getMediaType() {
        return "application/xml";
    }
}
