package org.cugos.geometry.ws;

import org.locationtech.jts.geom.Geometry;

public interface GeometryReader {

    Geometry read(String str) throws Exception;

    String getName();

}
