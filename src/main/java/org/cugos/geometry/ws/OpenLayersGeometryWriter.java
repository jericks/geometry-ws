package org.cugos.geometry.ws;

import org.locationtech.jts.geom.Geometry;

public class OpenLayersGeometryWriter implements GeometryWriter {

    @Override
    public String write(Geometry geometry) {
        String str = "<!doctype html>\n" +
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
            "            \"geometry\": " + new GeoJsonGeometryWriter().write(geometry) +
            "          }\n" +
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
            "</html>\n";
        return str;
    }

    @Override
    public String getName() {
        return "openlayers";
    }

    @Override
    public String getMediaType() {
        return "text/html";
    }
}
