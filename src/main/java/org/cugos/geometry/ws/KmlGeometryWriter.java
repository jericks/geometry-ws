package org.cugos.geometry.ws;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.kml.KMLWriter;

public class KmlGeometryWriter implements GeometryWriter {

    private final KMLWriter kmlWriter = new KMLWriter();

    @Override
    public String write(Geometry geometry) {
        return kmlWriter.write(geometry);
    }

    @Override
    public String getName() {
        return "kml";
    }

    @Override
    public String getMediaType() {
        return "application/xml";
    }

}
