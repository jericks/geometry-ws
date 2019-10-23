package org.cugos.geometry.ws;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.algorithm.Angle;
import org.locationtech.jts.geom.Geometry;

@Controller("/angle")
public class AngleController {
  
  @Get("/{from}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "angle get", summary = "Angle", description = "Calculate the angle between two geometries")
  public HttpResponse<String> get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Degree Type") @QueryValue(value="type", defaultValue = "degrees") String type) throws Exception {
    return angle(from, geometryString, type);
  }

  @Post("/{from}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "angle post", summary = "Angle", description = "Calculate the angle between two geometries")
  public HttpResponse<String> post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Degree Type") @QueryValue(value="type", defaultValue = "degrees") String type) throws Exception {
     return angle(from, geometryString, type);
  }

  private HttpResponse<String> angle(String from, String geometryString, String type) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    Geometry geometry = reader.read(geometryString);
    if (Validator.hasRequiredNumberOfGeometries(geometry, 2)) {
      Geometry firstGeometry = geometry.getGeometryN(0);
      Geometry secondGeometry = geometry.getGeometryN(1);
      double result = Angle.angle(firstGeometry.getCoordinate(), secondGeometry.getCoordinate());
      if (type.equalsIgnoreCase("degrees")) {
        result = Angle.toDegrees(result);
      }
      return HttpResponse.ok(String.valueOf(result));
    } else {
      return HttpResponse.badRequest("A Geometry Collection with two geometries is required!");
    }
  }

}