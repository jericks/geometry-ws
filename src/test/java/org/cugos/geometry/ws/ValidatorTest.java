package org.cugos.geometry.ws;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    @Test
    public void isGeometryCollection() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry multiPoint = geometryFactory.createMultiPointFromCoords(new Coordinate[] {
            new Coordinate(1,1),
            new Coordinate(2,2)
        });
        assertTrue(Validator.isGeometryCollection(multiPoint));
        Geometry point = geometryFactory.createPoint(new Coordinate(1,1));
        assertFalse(Validator.isGeometryCollection(point));
    }

    @Test
    public void hasRequiredNumberOfGeometries() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry geometry = geometryFactory.createMultiPointFromCoords(new Coordinate[] {
            new Coordinate(1,1),
            new Coordinate(2,2)
        });
        assertTrue(Validator.hasRequiredNumberOfGeometries(geometry, 2));
        assertFalse(Validator.hasRequiredNumberOfGeometries(geometry, 1));
        assertFalse(Validator.hasRequiredNumberOfGeometries(geometry, 3));
    }

}
