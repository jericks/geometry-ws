package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.algorithm.construct.LargestEmptyCircle;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

@Controller("/largestEmptyCircle")
public class LargestEmptyCircleController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "largest empty circle get", summary = "Calculate the largest empty circle", description = "Calculate the largest empty circle")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Tolerance") @QueryValue(value = "tolerance", defaultValue = "1.0") double tolerance) throws Exception {
    return largestEmptyCircle(from, to, geometryString, tolerance);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "largest empty circle post", summary = "Calculate the largest empty circle", description = "Calculate the largest empty circle")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Tolerance") @QueryValue(value = "tolerance", defaultValue = "1.0") double tolerance) throws Exception {
     return largestEmptyCircle(from, to, geometryString, tolerance);
  }

  private HttpResponse largestEmptyCircle(String from, String to, String geometryString, double tolerance) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    LargestEmptyCircle algorithm = new LargestEmptyCircle(geometry, tolerance);
    LineString radiusLineString = algorithm.getRadiusLine();
    Point centerPoint = radiusLineString.getStartPoint();
    Geometry outputGeometry = centerPoint.buffer(radiusLineString.getLength());
    String content = writer.write(outputGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}