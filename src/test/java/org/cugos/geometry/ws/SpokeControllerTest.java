package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiLineString;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpokeControllerTest extends AbstractControllerTest  {

    private final String pt = "POINT (5 5)";

    private final String mpt = "MULTIPOINT ((5.875473869469681 1.0101660098606535), " +
            "(19.64273518313129 8.032868631563336), " +
            "(19.397302929472787 10.139284609662209), " +
            "(12.61792804667091 17.61654337241537), " +
            "(4.802498121787375 9.17962232316298))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/spoke/wkt/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(pt, mpt), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        Geometry spokeGeometry = GeometryReaders.find("wkt").read(value);
        assertTrue(spokeGeometry instanceof MultiLineString);
        assertEquals(5, spokeGeometry.getNumGeometries());
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/spoke/wkt/geojson", Geometries.geometryCollection(pt, mpt))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String value = client.toBlocking().retrieve(request);
        Geometry spokeGeometry = GeometryReaders.find("geojson").read(value);
        assertTrue(spokeGeometry instanceof MultiLineString);
        assertEquals(5, spokeGeometry.getNumGeometries());
    }

    @Test
    public void badRequest() throws Exception {
        Assertions.assertThrows(HttpClientException.class, () -> {
            HttpRequest request = HttpRequest.GET("/spoke/wkt/geojson?geom=" + URLEncoder.encode(pt, "UTF-8"));
            client.toBlocking().retrieve(request);
        });
    }

}
