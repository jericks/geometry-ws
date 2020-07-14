package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaximumInscribedCircleControllerTest extends AbstractControllerTest {

    private final String inputGeometry = "POLYGON ((-122.38855361938475 47.5805786829606, -122.38636493682861 47.5783206388176, " +
            "-122.38700866699219 47.5750491969984, -122.38177299499512 47.57502024527343, " +
            "-122.38481998443604 47.5780600889959, -122.38151550292969 47.5805786829606, " +
            "-122.38855361938475 47.5805786829606))";

    private final String outputGeometry = "POLYGON ((-122.38509435187233 47.577799464117014, -122.3851074188168 47.57766679320322, " +
            "-122.38514611749491 47.57753922075825, -122.38520896073814 47.5774216493196, " +
            "-122.38529353351589 47.57731859709174, -122.38539658574375 47.57723402431398, " +
            "-122.3855141571824 47.57717118107076, -122.38564172962738 47.577132482392656, " +
            "-122.38577440054117 47.577119415448166, -122.38590707145495 47.577132482392656, " +
            "-122.38603464389993 47.57717118107076, -122.38615221533858 47.57723402431398, " +
            "-122.38625526756644 47.57731859709174, -122.38633984034419 47.5774216493196, " +
            "-122.38640268358742 47.57753922075825, -122.38644138226553 47.57766679320322, " +
            "-122.38645444921 47.577799464117014, -122.38644138226553 47.57793213503081, " +
            "-122.38640268358742 47.57805970747578, -122.38633984034419 47.57817727891443, " +
            "-122.38625526756644 47.57828033114229, -122.38615221533858 47.57836490392005, " +
            "-122.38603464389993 47.57842774716327, -122.38590707145495 47.57846644584137, " +
            "-122.38577440054117 47.57847951278586, -122.38564172962738 47.57846644584137, " +
            "-122.3855141571824 47.57842774716327, -122.38539658574375 47.57836490392005, " +
            "-122.38529353351589 47.57828033114229, -122.38520896073814 47.57817727891443, " +
            "-122.38514611749491 47.57805970747578, -122.3851074188168 47.57793213503081, " +
            "-122.38509435187233 47.577799464117014))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/maximumInscribedCircle/wkt/wkt?geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(outputGeometry, geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/maximumInscribedCircle/wkt/wkt", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(outputGeometry, geometry);
    }

}
