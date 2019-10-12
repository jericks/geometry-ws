package org.cugos.geometry.ws;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.util.GeometricShapeFactory;

public class Geometries {

    public static String geometryCollection(String firstGeometry, String secondGeometry) {
        return String.format("GEOMETRYCOLLECTION (%s, %s)", firstGeometry, secondGeometry);
    }

    public static GeometricShapeFactory prepareGeometricShapeFactory(
            GeometricShapeFactory shapeFactory, Geometry geometry, int width, int height, int numberOfPoints, double rotation, boolean center
    ) {
        // Validate arguments
        if (geometry instanceof Point) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("When the Geometry is a point, then width and height are required!");
            }
        }
        shapeFactory.setNumPoints(numberOfPoints);
        shapeFactory.setRotation(rotation);
        if (geometry instanceof Point) {
            shapeFactory.setWidth(width);
            shapeFactory.setHeight(height);
            if (center) {
                shapeFactory.setCentre(geometry.getCoordinate());
            } else {
                shapeFactory.setBase(geometry.getCoordinate());
            }
        } else {
            shapeFactory.setEnvelope(geometry.getEnvelopeInternal());
        }
        return shapeFactory;
    }

}
