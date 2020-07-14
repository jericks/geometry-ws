package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LargestEmptyCircleControllerTest extends AbstractControllerTest {

    private final String inputGeometry = "MULTIPOINT ((-122.3935317993164 47.57571508225466)," +
            "(-122.38838195800781 47.57444120741259)," +
            "(-122.39061355590819 47.5823155737249)," +
            "(-122.38357543945312 47.58034709317149)," +
            "(-122.38237380981445 47.57756793579513)," +
            "(-122.38666534423827 47.58521026361082)," +
            "(-122.39473342895508 47.581157652951454))";

    private final String outputGeometry = "POLYGON ((-122.385094264244 47.57953640127443, -122.38516073478301 47.57886151456605, " +
            "-122.38535759197453 47.578212563375395, -122.38567727070705 47.577614486532774, " +
            "-122.3861074859062 47.57709026779586, -122.38663170464311 47.57666005259672, " +
            "-122.38722978148573 47.576340373864184, -122.38787873267638 47.57614351667268, " +
            "-122.38855361938477 47.57607704613366, -122.38922850609315 47.57614351667268, " +
            "-122.3898774572838 47.576340373864184, -122.39047553412642 47.57666005259672, " +
            "-122.39099975286334 47.57709026779586, -122.39142996806248 47.577614486532774, " +
            "-122.391749646795 47.578212563375395, -122.39194650398652 47.57886151456605, " +
            "-122.39201297452553 47.57953640127443, -122.39194650398652 47.58021128798281, " +
            "-122.391749646795 47.58086023917347, -122.39142996806248 47.58145831601609, " +
            "-122.39099975286334 47.581982534753, -122.39047553412642 47.58241274995214, " +
            "-122.3898774572838 47.58273242868468, -122.38922850609315 47.58292928587618, " +
            "-122.38855361938477 47.5829957564152, -122.38787873267638 47.58292928587618, " +
            "-122.38722978148573 47.58273242868468, -122.38663170464311 47.58241274995214, " +
            "-122.3861074859062 47.581982534753, -122.38567727070705 47.58145831601609, " +
            "-122.38535759197453 47.58086023917347, -122.38516073478301 47.58021128798281, " +
            "-122.385094264244 47.57953640127443))";

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/largestEmptyCircle/wkt/wkt?geom=" + URLEncoder.encode(inputGeometry, "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(outputGeometry, geometry);
    }

    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/largestEmptyCircle/wkt/wkt", inputGeometry).contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals(outputGeometry, geometry);
    }

}
