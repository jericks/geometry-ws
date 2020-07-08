package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VoronoiDiagramControllerTest extends AbstractControllerTest  {

    private final String geometryWkt = "MULTIPOINT ((-126.91406249999999 44.08758502824516), (-116.3671875 44.08758502824516), " +
        "(-116.3671875 50.736455137010665), (-126.91406249999999 50.736455137010665))";

    private final String voronoiDiagramWkt = "GEOMETRYCOLLECTION (POLYGON ((-139.38178266003166 31.619864868213487, " +
            "-139.38178266003166 47.41202008262791, -121.640625 47.41202008262791, -121.640625 31.619864868213487, " +
            "-139.38178266003166 31.619864868213487)), POLYGON ((-103.89946733996833 47.41202008262791, " +
            "-103.89946733996833 31.619864868213487, -121.640625 31.619864868213487, -121.640625 47.41202008262791, " +
            "-103.89946733996833 47.41202008262791)), POLYGON ((-139.38178266003166 47.41202008262791, " +
            "-139.38178266003166 63.20417529704234, -121.640625 63.20417529704234, -121.640625 47.41202008262791, " +
            "-139.38178266003166 47.41202008262791)), POLYGON ((-121.640625 63.20417529704234, " +
            "-103.89946733996833 63.20417529704234, -103.89946733996833 47.41202008262791, -121.640625 47.41202008262791, " +
            "-121.640625 63.20417529704234)))";

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
