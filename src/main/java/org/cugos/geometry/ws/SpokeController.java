package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.*;

@Controller("/spoke")
public class SpokeController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId="spoke get", summary = "Spoke", description = "Create a spoke diagram with lines between a single Geometry to other Geometries")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString) throws Exception {
    return spoke(from, to, geometryString);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "spoke post", summary = "Spoke", description = "Create a spoke diagram with lines between a single Geometry to other Geometries")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString) throws Exception {
     return spoke(from, to, geometryString);
  }

  private HttpResponse spoke(String from, String to, String geometryString) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    if (Validator.hasRequiredNumberOfGeometries(geometry, 2)) {
      Geometry firstGeometry = geometry.getGeometryN(0);
      Geometry secondGeometry = geometry.getGeometryN(1);
      GeometryFactory factory = new GeometryFactory();
      Coordinate center = firstGeometry.getCoordinate();
      int number = secondGeometry.getNumGeometries();
      LineString[] lines = new LineString[number];
      for (int i = 0; i < number; i++) {
        Geometry g = secondGeometry.getGeometryN(i);
        lines[i] = factory.createLineString(new Coordinate[]{center, g.getCoordinate()});
      }
      MultiLineString multiLineString = factory.createMultiLineString(lines);
      String content = writer.write(multiLineString);
      return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
    } else {
      return HttpResponse.badRequest("A Geometry Collection with two geometries is required!");
    }
  }

}