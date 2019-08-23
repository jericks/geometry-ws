package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.linearref.LengthIndexedLine;

@Controller("/placePoint")
public class PlacePointController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Place Point", description = "Place a point on the input linear geometry.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString
  ) throws Exception {
    return placePoint(from, to, geometryString);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Place Point", description = "Place a point on the input linear geometry.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString
  ) throws Exception {
     return placePoint(from, to, geometryString);
  }

  private HttpResponse placePoint(String from, String to, String geometryString) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);

    if (Validator.hasRequiredNumberOfGeometries(geometry, 2)) {
      Geometry firstGeometry = geometry.getGeometryN(0);
      Geometry secondGeometry = geometry.getGeometryN(1);

      Point point = null;
      Geometry line = null;

      if (firstGeometry instanceof Point) {
        point = (Point) firstGeometry;
      } else if (secondGeometry instanceof Point) {
        point = (Point) secondGeometry;
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
      Coordinate coord = indexedLine.extractPoint(position);
      GeometryFactory factory = new GeometryFactory();
      Point snappedPoint = factory.createPoint(coord);

      return HttpResponse.ok(writer.write(snappedPoint)).contentType(new MediaType(writer.getMediaType()));
    } else {
      return HttpResponse.badRequest("A Geometry Collection with two geometries is required!");
    }
  }

}