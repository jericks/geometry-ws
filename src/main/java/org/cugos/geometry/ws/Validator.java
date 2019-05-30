package org.cugos.geometry.ws;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;

public class Validator {

    public static boolean hasRequiredNumberOfGeometries(Geometry geometry, int requiredNumber) {
        return geometry.getNumGeometries() == requiredNumber;
    }

    public static  boolean isGeometryCollection(Geometry geometry) {
        return geometry instanceof GeometryCollection;
    }

}
