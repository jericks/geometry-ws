package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EllipseControllerTest extends AbstractControllerTest  {

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/ellipse/wkt/wkt" +
                "?geom=" + URLEncoder.encode("POINT (100 100)", "UTF-8") +
                "&width=50" +
                "&height=40" +
                "&numberOfPoints=10" +
                "&rotation=0" +
                "&center=false"
        );
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((150 120, 145.22542485937367 131.75570504584945, "
                + "132.72542485937367 139.02113032590307, "
                + "117.27457514062631 139.02113032590307, "
                + "104.77457514062633 131.75570504584945, "
                + "100 120, 104.77457514062631 108.24429495415055, "
                + "117.27457514062631 100.97886967409693, "
                + "132.72542485937367 100.97886967409693, "
                + "145.22542485937367 108.24429495415053, 150 120))", geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/ellipse/wkt/wkt" +
                "?width=50" +
                "&height=40" +
                "&numberOfPoints=10" +
                "&rotation=0" +
                "&center=false"
                , "POINT (100 100)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((150 120, 145.22542485937367 131.75570504584945, "
                + "132.72542485937367 139.02113032590307, "
                + "117.27457514062631 139.02113032590307, "
                + "104.77457514062633 131.75570504584945, "
                + "100 120, 104.77457514062631 108.24429495415055, "
                + "117.27457514062631 100.97886967409693, "
                + "132.72542485937367 100.97886967409693, "
                + "145.22542485937367 108.24429495415053, 150 120))", geometry);
    }

}
