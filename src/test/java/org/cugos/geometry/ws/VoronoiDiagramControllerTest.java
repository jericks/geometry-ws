package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class VoronoiDiagramControllerTest extends AbstractControllerTest  {

    private final String geometryWkt = "MULTIPOINT ((-126.91406249999999 44.08758502824516), (-116.3671875 44.08758502824516), " +
        "(-116.3671875 50.736455137010665), (-126.91406249999999 50.736455137010665))";

    private final String voronoiDiagramWkt = "GEOMETRYCOLLECTION (POLYGON ((-137.46093749999997 33.54071002824517, " +
        "-137.46093749999997 47.41202008262791, -121.640625 47.41202008262791, -121.640625 33.54071002824517, " +
        "-137.46093749999997 33.54071002824517)), POLYGON ((-105.82031250000001 47.41202008262793, " +
        "-105.82031250000001 33.54071002824517, -121.640625 33.54071002824517, -121.640625 47.41202008262791, " +
        "-105.82031250000001 47.41202008262793)), POLYGON ((-137.46093749999997 47.41202008262791, " +
        "-137.46093749999997 61.28333013701065, -121.640625 61.28333013701065, -121.640625 47.41202008262791, " +
        "-137.46093749999997 47.41202008262791)), POLYGON ((-121.640625 61.28333013701065, " +
        "-105.82031250000001 61.28333013701065, -105.82031250000001 47.41202008262793, -121.640625 47.41202008262791, " +
        "-121.640625 61.28333013701065)))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/voronoiDiagram/wkt/wkt?geom=" + URLEncoder.encode(geometryWkt, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(voronoiDiagramWkt, geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/voronoiDiagram/wkt/wkt", geometryWkt).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(voronoiDiagramWkt, geometry);
    }

}
