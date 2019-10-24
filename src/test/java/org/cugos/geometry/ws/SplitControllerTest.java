package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SplitControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
    private final String otherGeometry = "LINESTRING (0 0, 10 10)";

    @Test
    public void getIntersection() throws Exception {
        HttpRequest request = HttpRequest.GET("/split/wkt/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(inputGeometry, otherGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        Geometry splitGeom = GeometryReaders.find("wkt").read(value);
        assertNotNull(splitGeom);
        assertEquals(2, splitGeom.getNumGeometries());
    }

    @Test
    public void postIntersection() throws Exception {
        HttpRequest request = HttpRequest.POST("/split/wkt/wkt", Geometries.geometryCollection(inputGeometry, otherGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String value = client.toBlocking().retrieve(request);
        Geometry splitGeom = GeometryReaders.find("wkt").read(value);
        assertNotNull(splitGeom);
        assertEquals(2, splitGeom.getNumGeometries());
    }

    @Test(expected = HttpClientException.class)
    public void badRequest() throws Exception {
        HttpRequest request = HttpRequest.GET("/split/wkt/wkt?geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
