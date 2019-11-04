package org.cugos.geometry.ws;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

public class ProjectControllerTest extends AbstractControllerTest  {

    @Test
    public void getEPSG() throws Exception {
        HttpRequest request = HttpRequest.GET("/project/wkt/wkt?source=EPSG:2927&target=EPSG:4326&geom=" + URLEncoder.encode("POINT (1186683.01 641934.58)", "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (-122.32131937934592 47.07927009358412)", geometry);
    }

    @Test
    public void getName() throws Exception {
        HttpRequest request = HttpRequest.GET("/project/wkt/wkt" +
                "?source=EPSG:2927" +
                "&target=" + URLEncoder.encode("+title=long/lat:WGS84 +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees", "UTF-8") +
                "&geom=" + URLEncoder.encode("POINT (1186683.01 641934.58)", "UTF-8"));
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (-122.32131937934592 47.07927009358412)", geometry);
    }

    @Test
    public void postEPSG() throws Exception {
        HttpRequest request = HttpRequest.POST("/project/wkt/wkt?source=EPSG:2927&target=EPSG:4326", "POINT (1186683.01 641934.58)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POINT (-122.32131937934592 47.07927009358412)", geometry);
    }


    @Test
    public void multiPoint() throws Exception {
        HttpRequest request = HttpRequest.POST("/project/wkt/wkt?source=EPSG:2927&target=EPSG:4326","MULTIPOINT (1163250.81 603866.43, " +
                "1322293.09 652475.80)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("MULTIPOINT ((-122.41163921633179 46.97340115028346), " +
                "(-121.77777166360232 47.115470671407195))", geometry);
    }

    @Test
    public void lineString() throws Exception {
        HttpRequest request = HttpRequest.POST("/project/wkt/wkt?source=EPSG:2927&target=EPSG:4326","LINESTRING (1163250.81 603866.43, " +
                "1322293.09 652475.80)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("LINESTRING (-122.41163921633179 46.97340115028346, " +
                "-121.77777166360232 47.115470671407195)", geometry);
    }

    @Test
    public void multiLineString() throws Exception {
        HttpRequest request = HttpRequest.POST("/project/wkt/wkt?source=EPSG:2927&target=EPSG:4326","MULTILINESTRING ((1168442.10 584989.01, " +
                "1188263.39 598675.14), (1209972.43 597258.33, " +
                "1209972.43 597258.33))").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("MULTILINESTRING ((-122.38903881281448 46.9219983979246, -122.31099168111484 46.9607892481702), (-122.22394569993035 46.958239147269786, -122.22394569993035 46.958239147269786))", geometry);
    }

    @Test
    public void linearRing() throws Exception {
        HttpRequest request = HttpRequest.POST("/project/wkt/wkt?source=EPSG:2927&target=EPSG:4326","LINEARRING (1170329.84 666161.92, " +
                "1170329.84 634070.30, 1207140.81 634070.30, " +
                "1207140.81 666161.92, 1170329.84 666161.92)").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("LINEARRING (-122.38926644275512 47.14462361201932, " +
                "-122.38618284071536 47.056659810491965, -122.23853186161534 47.058978681538825, " +
                "-122.24137424175268 47.1469462054281, -122.38926644275512 47.14462361201932)", geometry);
    }

    @Test
    public void polygon() throws Exception {
        HttpRequest request = HttpRequest.POST("/project/wkt/wkt?source=EPSG:2927&target=EPSG:4326","POLYGON ((1170329.84 666161.92, " +
                "1170329.84 634070.30, 1207140.81 634070.30, " +
                "1207140.81 666161.92, 1170329.84 666161.92))").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((-122.38926644275512 47.14462361201932, " +
                "-122.38618284071536 47.056659810491965, -122.23853186161534 47.058978681538825, " +
                "-122.24137424175268 47.1469462054281, -122.38926644275512 47.14462361201932))", geometry);
    }

    @Test
    public void polygonWithHoles() throws Exception {
        HttpRequest request = HttpRequest.POST("/project/wkt/wkt?source=EPSG:2927&target=EPSG:4326","POLYGON ((1176936.94 660026.76, " +
                "1176936.94 650116.11, 1185431.78 650116.11, " +
                "1185431.78 660026.76, 1176936.94 660026.76), " +
                "(1170329.84 666161.92, 1170329.84 634070.30, " +
                "1207140.81 634070.30, 1207140.81 666161.92, " +
                "1170329.84 666161.92))").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("POLYGON ((-122.36214062183883 47.128237891850084, -122.36120122901394 47.101072504701726, " +
                "-122.3271007108911 47.101617006698966, -122.32802290480767 47.12878266363337, " +
                "-122.36214062183883 47.128237891850084), (-122.38926644275512 47.14462361201932, " +
                "-122.38618284071536 47.056659810491965, -122.23853186161534 47.058978681538825, " +
                "-122.24137424175268 47.1469462054281, -122.38926644275512 47.14462361201932))", geometry);
    }

    @Test
    public void multiPolygon() throws Exception {
        HttpRequest request = HttpRequest.POST("/project/wkt/wkt?source=EPSG:2927&target=EPSG:4326","MULTIPOLYGON (((1173633.39 666633.86, " +
                "1173633.39 654363.53, 1184959.84 654362.53, " +
                "1184959.84 666633.86, 1173633.39 666633.86)), " +
                "((1159947.26 662386.44, 1159947.26 642565.14, " +
                "1168442.10 642565.14, 1168442.10 662386.44, " +
                "1159947.26 662386.44)))").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("MULTIPOLYGON (((-122.37603969117592 47.14613335960035, -122.37486770512186 47.112500325933574, " +
                "-122.32939064814983 47.11322672481739, -122.33053432020763 47.14686294686659, " +
                "-122.37603969117592 47.14613335960035)), ((-122.43060595159984 47.133585917006876, " +
                "-122.42865889085566 47.079256119282064, -122.39457286784855 47.07982054112014, " +
                "-122.3964855440993 47.13415089827387, -122.43060595159984 47.133585917006876)))", geometry);
    }

    @Test
    public void geometryCollection() throws Exception {
        HttpRequest request = HttpRequest.POST("/project/wkt/wkt?source=EPSG:2927&target=EPSG:4326","GEOMETRYCOLLECTION (POLYGON ((1165138.55 652003.86, " +
                "1165138.55 643980.95, 1175993.07 643980.95, " +
                "1175993.07 652003.86, 1165138.55 652003.86)), " +
                "LINESTRING (1175049.20 667577.73, 1178352.74 652475.79), " +
                "POINT (1167970.16 659554.83))").contentType(MediaType.TEXT_PLAIN_TYPE);
        String geometry = client.toBlocking().retrieve(request);
        assertEquals("GEOMETRYCOLLECTION (POLYGON ((-122.40874531445743 47.10547387775581, " +
                "-122.40796607905237 47.08348300510544, -122.36440789255106 47.084194618087764, " +
                "-122.3651693517066 47.10618577613497, -122.40874531445743 47.10547387775581)), " +
                "LINESTRING (-122.37044151241236 47.148812677741184, -122.35574078098566 47.10763194090735), " +
                "POINT (-122.39810741033439 47.126358373714936))", geometry);
    }

}
