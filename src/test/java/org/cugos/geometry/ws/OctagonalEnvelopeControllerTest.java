package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OctagonalEnvelopeControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "MULTIPOINT ((12.27256417862947 12.73833434783841), "
        + "(13.737894633461437 7.658802439672621), "
        + "(6.857126638942733 8.821305316892328), "
        + "(9.260874914207697 13.087320259444919), "
        + "(8.017822881853032 7.492806794533148))";

    private final String outputGeometry = "POLYGON ((6.857126638942733 8.653503037443446, "
        + "6.857126638942733 10.683571984179956, 9.260874914207697 "
        + "13.087320259444919, 11.923578267022961 13.087320259444919, "
        + "13.737894633461437 11.273003893006443, 13.737894633461437 "
        + "7.658802439672621, 13.571898988321964 7.492806794533148, "
        + "8.017822881853032 7.492806794533148, 6.857126638942733 "
        + "8.653503037443446))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/octagonalEnvelope/wkt/wkt?geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(outputGeometry, geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/octagonalEnvelope/wkt/wkt", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(outputGeometry, geometry);
    }

}
