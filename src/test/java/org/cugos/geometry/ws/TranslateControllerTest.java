package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class TranslateControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "MULTIPOINT ((12.27256417862947 12.73833434783841), "
        + "(13.737894633461437 7.658802439672621), "
        + "(6.857126638942733 8.821305316892328), "
        + "(9.260874914207697 13.087320259444919), "
        + "(8.017822881853032 7.492806794533148))";

    private final String outputGeometry = "MULTIPOINT ((13.472564178629469 15.038334347838411), " +
        "(14.937894633461436 9.95880243967262), " +
        "(8.057126638942734 11.121305316892329), " +
        "(10.460874914207697 15.38732025944492), " +
        "(9.217822881853031 9.792806794533147))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/translate/wkt/wkt?x=1.2&y=2.3&geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(outputGeometry, geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/translate/wkt/wkt?x=1.2&y=2.3&", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(outputGeometry, geometry);
    }

}
