package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Lineal;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.linearref.LengthIndexedLine;

@Controller("/locatePoint")
public class LocatePointController {
  
  @Get("/{from}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "locate point get", summary = "Locate Point", description = "Locate a point along a linestring")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString) throws Exception {
    return locatePoint(from, geometryString);
  }

  @Post("/{from}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "locate point post", summary = "Locate Point", description = "Locate a point along a linestring")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @Body String geometryString) throws Exception {
    return locatePoint(from, geometryString);
  }

  private HttpResponse locatePoint(String from, String geometryString) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    Geometry geometry = reader.read(geometryString);
    if (Validator.hasRequiredNumberOfGeometries(geometry, 2)) {
      Geometry firstGeometry = geometry.getGeometryN(0);
      Geometry secondGeometry = geometry.getGeometryN(1);

      Point point = null;
      Geometry line = null;

      if (firstGeometry instanceof Point) {
        point = (Point)firstGeometry;
      } else if (secondGeometry instanceof Point) {
        point = (Point)secondGeometry;
      }

      if (firstGeometry instanceof Lineal) {
        line = firstGeometry;
      } else if (secondGeometry instanceof Lineal) {
        line = secondGeometry;
      }

      if (point == null || line == null) {
        return HttpResponse.badRequest("Please provide a Point and a Linear Geometry!");
      }

      LengthIndexedLine indexedLine = new LengthIndexedLine(line);
      double position = indexedLine.indexOf(point.getCoordinate());
      double length = line.getLength();
      double percentAlong = position / length;
      return HttpResponse.ok(String.valueOf(percentAlong)).contentType(MediaType.TEXT_PLAIN_TYPE);
    } else {
      return HttpResponse.badRequest("A Geometry Collection with two geometries is required!");
    }
  }

}