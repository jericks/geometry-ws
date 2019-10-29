package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class NormalizeControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "MULTIPOINT ((12.27256417862947 12.73833434783841), "
            + "(13.737894633461437 7.658802439672621), "
            + "(6.857126638942733 8.821305316892328), "
            + "(9.260874914207697 13.087320259444919), "
            + "(8.017822881853032 7.492806794533148))";

    private final String expectedGeometry = "MULTIPOINT ((6.857126638942733 8.821305316892328), "
            + "(8.017822881853032 7.492806794533148), (9.260874914207697 "
            + "13.087320259444919), (12.27256417862947 12.73833434783841), "
            + "(13.737894633461437 7.658802439672621))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/normalize/wkt/wkt?geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(expectedGeometry, geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/normalize/wkt/wkt", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(expectedGeometry, geometry);
    }

}
