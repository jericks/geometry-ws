package org.cugos.geometry.ws;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonWriter;

public class GeoJsonGeometryWriter implements GeometryWriter {

    private final GeoJsonWriter geoJsonWriter = new GeoJsonWriter();

    @Override
    public String write(Geometry geometry) {
        return geoJsonWriter.write(geometry);
    }

    @Override
    public String getName() {
        return "geojson";
    }

    @Override
    public String getMediaType() {
        return "application/json";
    }
}
