package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinimumBoundingCircleControllerTest extends AbstractControllerTest  {

    private final String inputGeometry = "MULTIPOINT ((12.27256417862947 12.73833434783841), "
        + "(13.737894633461437 7.658802439672621), "
        + "(6.857126638942733 8.821305316892328), "
        + "(9.260874914207697 13.087320259444919), "
        + "(8.017822881853032 7.492806794533148))";

    private final String outputGeometry = "POLYGON ((14.261208198913506 9.566699017816614, "
        + "14.189353574798158 8.83714677603589, 13.976551035360327 "
        + "8.13563081776919, 13.630978462849573 7.489110007878018, "
        + "13.165916017428149 6.9224297785134485, 12.599235788063577 "
        + "6.457367333092024, 11.952714978172406 6.111794760581269, "
        + "11.251199019905705 5.898992221143438, 10.521646778124982 "
        + "5.82713759702809, 9.792094536344258 5.898992221143438, "
        + "9.090578578077558 6.111794760581269, 8.444057768186386 "
        + "6.457367333092024, 7.877377538821817 6.922429778513448, "
        + "7.412315093400392 7.489110007878018, 7.066742520889636 "
        + "8.13563081776919, 6.853939981451806 8.837146776035892, "
        + "6.782085357336458 9.566699017816617, 6.853939981451807 "
        + "10.29625125959734, 7.066742520889639 10.997767217864043, "
        + "7.4123150934003945 11.644288027755213, 7.87737753882182 "
        + "12.210968257119784, 8.444057768186392 12.676030702541208, "
        + "9.090578578077565 13.021603275051962, 9.792094536344267 "
        + "13.234405814489792, 10.52164677812499 13.306260438605138, "
        + "11.251199019905716 13.234405814489788, 11.952714978172416 "
        + "13.021603275051955, 12.599235788063588 12.676030702541198, "
        + "13.165916017428156 12.21096825711977, 13.63097846284958 "
        + "11.644288027755199, 13.976551035360332 10.997767217864025, "
        + "14.189353574798162 10.296251259597323, 14.261208198913506 "
        + "9.566699017816614))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/minimumBoundingCircle/wkt/wkt?geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(outputGeometry, geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/minimumBoundingCircle/wkt/wkt", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(outputGeometry, geometry);
    }

}
