package org.cugos.geometry.ws;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LeafletGeometryWriterTest {

    @Test
    public void getName() {
        LeafletGeometryWriter writer = new LeafletGeometryWriter();
        assertEquals("leaflet", writer.getName());
    }

    @Test
    public void getMediaType() {
        LeafletGeometryWriter writer = new LeafletGeometryWriter();
        assertEquals("text/html", writer.getMediaType());
    }

    @Test
    public void isRegistered() {
        assertNotNull(GeometryWriters.find("leaflet"));
    }

    @Test
    public void write() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry geometry = geometryFactory.createPoint(new Coordinate(-123.45, 42.56));
        LeafletGeometryWriter writer = new LeafletGeometryWriter();
        String html = writer.write(geometry);
        assertEquals("<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "\t<title>Leaflet</title>\n" +
            "\t<meta charset=\"utf-8\" />\n" +
            "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.5.1/dist/leaflet.css\" integrity=\"sha512-xwE/Az9zrjBIphAcBb3F6JVqxf46+CDLwfLMHloNu6KEQCAWi6HcDUbeOfBIptF7tcCzusKFjFw2yuvEpDL9wQ==\" crossorigin=\"\"/>\n" +
            "    <script src=\"https://unpkg.com/leaflet@1.5.1/dist/leaflet.js\" integrity=\"sha512-GffPMF3RvMeYyc1LWMHtK8EbPv0iNZ8/oTtHPx9/cc2ILxQ+u905qIwdpULaqDkyBKgOaB57QTMg7ztg8Jm2Og==\" crossorigin=\"\"></script>\n" +
            "    <style>\n" +
            "      html, body {\n" +
            "        width: 100%;\n" +
            "        height: 100%;\n" +
            "        margin: 0;\n" +
            "        padding: 0;\n" +
            "      }\n" +
            "      #map {\n" +
            "        width: 100%;\n" +
            "        height: 100%;\n" +
            "      }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div id=\"map\"></div>\n" +
            "    <script>\n" +
            "        var map = L.map('map');\n" +
            "        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n" +
            "            attribution: 'Map data &copy; <a href=\"https://www.openstreetmap.org/\">OpenStreetMap</a> contributors'\n" +
            "        }).addTo(map);\n" +
            "\n" +
            "      var geometry = {\n" +
            "        \"type\": \"FeatureCollection\",\n" +
            "        \"features\": [\n" +
            "          {\n" +
            "            \"type\": \"Feature\",\n" +
            "            \"properties\": {},\n" +
            "            \"geometry\": {\"type\":\"Point\",\"coordinates\":[-123.45,42.56],\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:0\"}}}          }\n" +
            "        ]\n" +
            "      };\n" +
            "\n" +
            "        var geojsonLayer = L.geoJSON(geometry).addTo(map);\n" +
            "\n" +
            "        map.fitBounds(geojsonLayer.getBounds());\n" +
            "\n" +
            "    </script>\n" +
            "</body>\n" +
            "</html>\n", html);
    }

}
