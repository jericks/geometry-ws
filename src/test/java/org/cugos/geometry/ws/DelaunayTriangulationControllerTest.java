package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class DelaunayTriangulationControllerTest extends AbstractControllerTest {

    private final String inputGeometry = "MULTIPOINT ((12.27256417862947 12.73833434783841), "
        + "(13.737894633461437 7.658802439672621), "
        + "(6.857126638942733 8.821305316892328), "
        + "(9.260874914207697 13.087320259444919), "
        + "(8.017822881853032 7.492806794533148))";

    private final String nonConformingOutputGeometry = "GEOMETRYCOLLECTION (POLYGON ((6.857126638942733 8.821305316892328, "
        + "8.017822881853032 7.492806794533148, 9.260874914207697 13.087320259444919, "
        + "6.857126638942733 8.821305316892328)), POLYGON ((9.260874914207697 "
        + "13.087320259444919, 8.017822881853032 7.492806794533148, 12.27256417862947 "
        + "12.73833434783841, 9.260874914207697 13.087320259444919)), POLYGON ((12.27256417862947 "
        + "12.73833434783841, 8.017822881853032 7.492806794533148, 13.737894633461437 "
        + "7.658802439672621, 12.27256417862947 12.73833434783841)))";

    private final String conformingOutputGeometry = "GEOMETRYCOLLECTION (POLYGON ((6.857126638942733 8.821305316892328, "
        + "8.017822881853032 7.492806794533148, 9.260874914207697 13.087320259444919, "
        + "6.857126638942733 8.821305316892328)), POLYGON ((9.260874914207697 "
        + "13.087320259444919, 8.017822881853032 7.492806794533148, 12.27256417862947 "
        + "12.73833434783841, 9.260874914207697 13.087320259444919)), POLYGON ((12.27256417862947 "
        + "12.73833434783841, 8.017822881853032 7.492806794533148, 13.737894633461437 "
        + "7.658802439672621, 12.27256417862947 12.73833434783841)))";

    @Test
    public void nonConformingGet() throws Exception {
        HttpRequest request = HttpRequest.GET("/delaunayTriangulation/wkt/wkt?conforming=false&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(nonConformingOutputGeometry, geometry);
    }

    @Test
    public void nonConformingPost() throws Exception {
        HttpRequest request = HttpRequest.POST("/delaunayTriangulation/wkt/wkt/?conforming=false", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(nonConformingOutputGeometry, geometry);
    }

    @Test
    public void conformingGet() throws Exception {
        HttpRequest request = HttpRequest.GET("/delaunayTriangulation/wkt/wkt?conforming=true&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(conformingOutputGeometry, geometry);
    }

    @Test
    public void conformingPost() throws Exception {
        HttpRequest request = HttpRequest.POST("/delaunayTriangulation/wkt/wkt/?conforming=true", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(conformingOutputGeometry, geometry);
    }

}
