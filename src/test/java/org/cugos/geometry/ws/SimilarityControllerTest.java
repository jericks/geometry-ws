package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class SimilarityControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0))";
    private final String otherGeometry = "POLYGON ((2 2, 2 14, 14 14, 14 2, 2 2))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/similarity/wkt?algorithm=area" +
            "&geom=" + URLEncoder.encode(Geometries.geometryCollection(inputGeometry, otherGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals("0.35555555555555557", value);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/similarity/wkt?algorithm=hausdorff",
            Geometries.geometryCollection(inputGeometry, otherGeometry)).contentType(MediaType.TEXT_PLAIN_TYPE);
        String value = client.toBlocking().retrieve(request);
        assertEquals("0.7142857142857142", value);
    }

    @Test(expected = HttpClientException.class)
    public void onlyOneGeometry() throws Exception {
        HttpRequest request = HttpRequest.GET("/similarity/wkt?algorithm=hausdorff&geom=" +
                URLEncoder.encode(inputGeometry, "UTF-8"));
        client.toBlocking().retrieve(request);
    }

    @Test(expected = HttpClientException.class)
    public void unknownAlgorithm() throws Exception {
        HttpRequest request = HttpRequest.GET("/similarity/wkt?algorithm=badalgorithm&geom=" +
                URLEncoder.encode(Geometries.geometryCollection(inputGeometry, otherGeometry), "UTF-8"));
        client.toBlocking().retrieve(request);
    }

}
