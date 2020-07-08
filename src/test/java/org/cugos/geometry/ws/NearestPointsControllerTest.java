package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.exceptions.HttpClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NearestPointsControllerTest extends AbstractControllerTest  {

    private final String firstGeometry = "POLYGON ((90 90, 90 110, 110 110, 110 90, 90 90))";
    private final String  secondGeometry = "POLYGON ((173.96210441769105 -94.53669248798772, 193.14058991095382 -88.86344877872318, " +
            "198.81383362021836 -108.04193427198595, 179.6353481269556 -113.71517798125049, " +
            "173.96210441769105 -94.53669248798772))";
    private final String closestPoints = "MULTIPOINT ((110 90), (173.96210441769105 -94.53669248798772))";

    @Test
    public void getIntersection() throws Exception {
        HttpRequest request = HttpRequest.GET("/nearestPoints/wkt/wkt" +
            "?geom=" + URLEncoder.encode(Geometries.geometryCollection(firstGeometry, secondGeometry), "UTF-8"));
        String value = client.toBlocking().retrieve(request);
        assertEquals(closestPoints, value);
    }

    @Test
    public void postIntersection() throws Exception {
        HttpRequest request = HttpRequest.POST("/nearestPoints/wkt/wkt", Geometries.geometryCollection(firstGeometry, secondGeometry))
            .contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(closestPoints, geometry);
    }

    @Test
    public void badRequest() throws Exception {
        Assertions.assertThrows(HttpClientException.class, () -> {
            HttpRequest request = HttpRequest.GET("/nearestPoints/wkt/wkt?geom=" + URLEncoder.encode(firstGeometry, "UTF-8"));
            client.toBlocking().retrieve(request);
        });
    }

}
