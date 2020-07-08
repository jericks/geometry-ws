package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CentroidControllerTest extends AbstractControllerTest  {

    private final String polygon = "POLYGON ((-126.91406249999999 44.08758502824516, -116.3671875 44.08758502824516, -116.3671875 50.736455137010665, -126.91406249999999 50.736455137010665, -126.91406249999999 44.08758502824516))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/centroid/wkt/wkt?geom=" + URLEncoder.encode(polygon, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (-121.64062499999999 47.41202008262791)", geometry);
    }

    @Test
    public void getGeoJsonFromWkt() throws Exception {
        HttpRequest request = HttpRequest.GET("/centroid/wkt/geojson?geom=" + URLEncoder.encode(polygon, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("{\"type\":\"Point\",\"coordinates\":[-121.640625,47.41202008],\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:0\"}}}", geometry);
    }

    @Test
    public void getKmlFromWkt() throws Exception {
        HttpRequest request = HttpRequest.GET("/centroid/wkt/kml?geom=" + URLEncoder.encode(polygon, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("<Point>\n" +
            "  <coordinates>-121.64062499999999,47.41202008262791</coordinates>\n" +
            "</Point>\n", geometry);
    }

    @Test
    public void getGml2FromWkt() throws Exception {
        HttpRequest request = HttpRequest.GET("/centroid/wkt/gml2?geom=" + URLEncoder.encode(polygon, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("<gml:Point>\n" +
            "  <gml:coordinates>\n" +
            "    -121.64062499999999,47.41202008262791 \n" +
            "  </gml:coordinates>\n" +
            "</gml:Point>\n", geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/centroid/wkt/wkt", polygon).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (-121.64062499999999 47.41202008262791)", geometry);
    }

}
