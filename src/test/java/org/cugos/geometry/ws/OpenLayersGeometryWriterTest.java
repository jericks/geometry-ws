package org.cugos.geometry.ws;

import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OpenLayersGeometryWriterTest {

    @Test
    public void getName() {
        OpenLayersGeometryWriter writer = new OpenLayersGeometryWriter();
        assertEquals("openlayers", writer.getName());
    }

    @Test
    public void getMediaType() {
        OpenLayersGeometryWriter writer = new OpenLayersGeometryWriter();
        assertEquals("text/html", writer.getMediaType());
    }

    @Test
    public void isRegistered() {
        assertNotNull(GeometryWriters.find("openlayers"));
    }

    @Test
    public void write() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry geometry = geometryFactory.createPoint(new Coordinate(-123.45, 42.56));
        OpenLayersGeometryWriter writer = new OpenLayersGeometryWriter();
        String html = writer.write(geometry);
        assertEquals("<!doctype html>\n" +
            "<html lang=\"en\">\n" +
            "  <head>\n" +
            "    <link rel=\"stylesheet\" href=\"https://cdn.rawgit.com/openlayers/openlayers.github.io/master/en/v5.3.0/css/ol.css\" type=\"text/css\">\n" +
            "    <style>\n" +
            "      html, body {\n" +
            "        width: 100%;\n" +
            "        height: 100%;\n" +
            "        margin: 0;\n" +
            "        padding: 0;\n" +
            "      }\n" +
            "      .map {\n" +
            "        width: 100%;\n" +
            "        height: 100%;\n" +
            "      }\n" +
            "    </style>\n" +
            "    <script src=\"https://cdn.rawgit.com/openlayers/openlayers.github.io/master/en/v5.3.0/build/ol.js\"></script>\n" +
            "    <title>OpenLayers</title>\n" +
            "  </head>\n" +
            "  <body>\n" +
            "    <div id=\"map\" class=\"map\"></div>\n" +
            "    <script type=\"text/javascript\">\n" +
            "    \n" +
            "      var geometry = {\n" +
            "        \"type\": \"FeatureCollection\",\n" +
            "        \"features\": [\n" +
            "          {\n" +
            "            \"type\": \"Feature\",\n" +
            "            \"properties\": {},\n" +
            "            \"geometry\": {\"type\":\"Point\",\"coordinates\":[-123.45,42.56],\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:0\"}}}          }\n" +
            "        ]\n" +
            "      };\n" +
            "      var source = new ol.source.Vector({\n" +
            "        features: (new ol.format.GeoJSON()).readFeatures(geometry, {\n" +
            "          featureProjection: \"EPSG:3857\"\n" +
            "        })\n" +
            "      });\n" +
            "\n" +
            "      var layer = new ol.layer.Vector({\n" +
            "        source: source\n" +
            "      });\n" +
            "\n" +
            "\n" +
            "      var map = new ol.Map({\n" +
            "        target: 'map',\n" +
            "        layers: [\n" +
            "          new ol.layer.Tile({\n" +
            "            source: new ol.source.OSM()\n" +
            "          }),\n" +
            "          layer\n" +
            "        ],\n" +
            "        view: new ol.View({\n" +
            "          center: ol.proj.fromLonLat([0,0]),\n" +
            "          zoom: 4\n" +
            "        })\n" +
            "      });\n" +
            "\n" +
            "      var extent = layer.getSource().getExtent();\n" +
            "      map.getView().fit(extent, {size: map.getSize(), maxZoom: 10});\n" +
            "      \n" +
            "    </script>\n" +
            "  </body>\n" +
            "</html>\n", html);
    }

}
