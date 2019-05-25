package org.cugos.geometry.ws;

import org.locationtech.jts.geom.Geometry;

public interface GeometryWriter {

    String write(Geometry geometry);

    String getName();

    String getMediaType();

}
