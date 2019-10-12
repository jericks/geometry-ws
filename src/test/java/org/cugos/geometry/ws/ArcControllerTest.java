package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class ArcControllerTest extends AbstractControllerTest  {

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/arc/wkt/wkt" +
                "?geom=" + URLEncoder.encode("POINT (100 100)", "UTF-8") +
                "&width=50" +
                "&height=50" +
                "&numberOfPoints=10" +
                "&rotation=0" +
                "&center=false" +
                "&startAngle=45" +
                "&angleExtent=90" +
                "&degrees=false"
        );
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("LINESTRING (138.13304972044324 146.27258811335295, "
                + "121.38674369432238 149.73750955269105, "
                + "106.33112045049884 141.62747534553029, "
                + "100.01087342937183 125.73726063039155, "
                + "105.3833164538481 109.50207347255339, "
                + "119.93462372472202 100.51853837717505, "
                + "136.85607685018496 102.99015125625557, "
                + "148.2299398518295 115.76041697475566, "
                + "148.7342558247792 132.8539862772947, "
                + "138.13304972044324 146.27258811335295)", geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/arc/wkt/wkt" +
                "?width=50" +
                "&height=50" +
                "&numberOfPoints=10" +
                "&rotation=0" +
                "&center=false" +
                "&startAngle=45" +
                "&angleExtent=90" +
                "&degrees=false", "POINT (100 100)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("LINESTRING (138.13304972044324 146.27258811335295, "
                + "121.38674369432238 149.73750955269105, "
                + "106.33112045049884 141.62747534553029, "
                + "100.01087342937183 125.73726063039155, "
                + "105.3833164538481 109.50207347255339, "
                + "119.93462372472202 100.51853837717505, "
                + "136.85607685018496 102.99015125625557, "
                + "148.2299398518295 115.76041697475566, "
                + "148.7342558247792 132.8539862772947, "
                + "138.13304972044324 146.27258811335295)", geometry);
    }

}
