package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.*;

@Controller("/startPoint")
public class StartPointController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "start point get", summary = "Start Point", description = "Get the start point of a Geometry")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString) throws Exception {
    return startPoint(from, to, geometryString);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "start point post", summary = "Start Point", description = "Get the start point of a Geometry")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString) throws Exception {
     return startPoint(from, to, geometryString);
  }

  private HttpResponse startPoint(String from, String to, String geometryString) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    if (!(geometry instanceof Lineal)) {
      return HttpResponse.badRequest("The input geometry must be a LineString or a MultiLineString!");
    } else {
      LineString lineString;
      if (geometry instanceof MultiLineString) {
        lineString = (LineString) (geometry).getGeometryN(0);
      } else {
        lineString = (LineString) geometry;
      }
      Point point = lineString.getStartPoint();
      String content = writer.write(point);
      return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
    }
  }

}