package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class SuperCircleControllerTest extends AbstractControllerTest  {

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/superCircle/wkt/wkt" +
                "?geom=" + URLEncoder.encode("POINT (100 100)", "UTF-8") +
                "&width=50" +
                "&height=40" +
                "&numberOfPoints=10" +
                "&rotation=0" +
                "&center=false" +
                "&power=3.2"
        );
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((125 140, 141.10490331949254 136.10490331949254, "
                + "145 120, 141.10490331949254 103.89509668050746, 125 100, "
                + "108.89509668050746 103.89509668050745, 105 120, "
                + "108.89509668050745 136.10490331949254, 125 140))", geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/superCircle/wkt/wkt" +
                "?width=50" +
                "&height=40" +
                "&numberOfPoints=10" +
                "&rotation=0" +
                "&center=false" +
                "&power=3.2"
                , "POINT (100 100)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((125 140, 141.10490331949254 136.10490331949254, "
                + "145 120, 141.10490331949254 103.89509668050746, 125 100, "
                + "108.89509668050746 103.89509668050745, 105 120, "
                + "108.89509668050745 136.10490331949254, 125 140))", geometry);
    }

}
