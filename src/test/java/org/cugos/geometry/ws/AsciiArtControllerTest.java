package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AsciiArtControllerTest extends AbstractControllerTest  {

    private static final String NEW_LINE = System.getProperty("line.separator");

    @Test
    public void get() throws Exception {
        HttpRequest request = HttpRequest.GET("/asciiArt/wkt?geom=" + URLEncoder.encode("POINT (46.801 53.972)", "UTF-8"));
        String actual = client.toBlocking().retrieve(request);
        String expected = " ____    ___   ___  _   _  _____     __ _  _     __        ___    ___   _    ____   _____      ___    _____  ____  __  " + NEW_LINE +
                "|  _ \\  / _ \\ |_ _|| \\ | ||_   _|   / /| || |   / /_      ( _ )  / _ \\ / |  | ___| |___ /     / _ \\  |___  ||___ \\ \\ \\ " + NEW_LINE +
                "| |_) || | | | | | |  \\| |  | |    | | | || |_ | '_ \\     / _ \\ | | | || |  |___ \\   |_ \\    | (_) |    / /   __) | | |" + NEW_LINE +
                "|  __/ | |_| | | | | |\\  |  | |    | | |__   _|| (_) | _ | (_) || |_| || |   ___) | ___) | _  \\__, |   / /   / __/  | |" + NEW_LINE +
                "|_|     \\___/ |___||_| \\_|  |_|     \\_\\   |_|   \\___/ (_) \\___/  \\___/ |_|  |____/ |____/ (_)   /_/   /_/   |_____|/_/ " + NEW_LINE;
        assertEquals(expected, actual);
    }
    
    @Test
    public void post() throws Exception {
        HttpRequest request = HttpRequest.POST("/asciiArt/wkt", "LINESTRING (0 0, 10 10)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String actual = client.toBlocking().retrieve(request);
        String expected = " _      ___  _   _  _____  ____   _____  ____   ___  _   _   ____     __  ___      ___        _   ___     _   ___  __  " + NEW_LINE +
                "| |    |_ _|| \\ | || ____|/ ___| |_   _||  _ \\ |_ _|| \\ | | / ___|   / / / _ \\    / _ \\      / | / _ \\   / | / _ \\ \\ \\ " + NEW_LINE +
                "| |     | | |  \\| ||  _|  \\___ \\   | |  | |_) | | | |  \\| || |  _   | | | | | |  | | | | _   | || | | |  | || | | | | |" + NEW_LINE +
                "| |___  | | | |\\  || |___  ___) |  | |  |  _ <  | | | |\\  || |_| |  | | | |_| |  | |_| |( )  | || |_| |  | || |_| | | |" + NEW_LINE +
                "|_____||___||_| \\_||_____||____/   |_|  |_| \\_\\|___||_| \\_| \\____|   \\_\\ \\___/    \\___/ |/   |_| \\___/   |_| \\___/ /_/ " + NEW_LINE;
        assertEquals(expected, actual);
    }

}
