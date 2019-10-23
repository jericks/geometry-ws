package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;

@Controller("/withinDistance")
public class WithinDistanceController {
  
  @Get("/{from}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "within distance get", summary = "Within Distance", description = "Whether one geometry is within a distance of another")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Distance") @QueryValue("distance") double distance) throws Exception {
    return withinDistance(from, geometryString, distance);
  }

  @Post("/{from}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "within distance post", summary = "Within Distance", description = "Whether one geometry is within a distance of another")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Distance") @QueryValue("distance") double distance) throws Exception {
    return withinDistance(from, geometryString, distance);
  }

  private HttpResponse withinDistance(String from, String geometryString, double distance) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    Geometry geometry = reader.read(geometryString);
    if (Validator.hasRequiredNumberOfGeometries(geometry, 2)) {
      Geometry firstGeometry = geometry.getGeometryN(0);
      Geometry secondGeometry = geometry.getGeometryN(1);
      boolean result = firstGeometry.isWithinDistance(secondGeometry, distance);
      return HttpResponse.ok(String.valueOf(result));
    } else {
      return HttpResponse.badRequest("A Geometry Collection with two geometries is required!");
    }
  }

}