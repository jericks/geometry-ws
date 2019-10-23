package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;

@Controller("/equals")
public class EqualsController {
  
  @Get("/{from}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "is valid get", summary = "Is Valid", description = "Determine if the input geometry is valid or not.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Input Geometry") @QueryValue(value = "type", defaultValue = "extact") String type,
      @Parameter(description = "Input Geometry") @QueryValue(value = "tolerance", defaultValue = "-1") double tolerance) throws Exception {
    return equals(from, geometryString, type, tolerance);
  }

  @Post("/{from}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "is valid post", summary = "Is Valid", description = "Determine if the input geometry is valid or not.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Input Geometry") @QueryValue(value = "type", defaultValue = "extact") String type,
      @Parameter(description = "Input Geometry") @QueryValue(value = "tolerance", defaultValue = "-1") double tolerance) throws Exception {
     return equals(from, geometryString, type, tolerance);
  }

  private HttpResponse equals(String from, String geometryString, String type, double tolerance) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    Geometry geometry = reader.read(geometryString);
    if (Validator.hasRequiredNumberOfGeometries(geometry, 2)) {
      Geometry firstGeometry = geometry.getGeometryN(0);
      Geometry secondGeometry = geometry.getGeometryN(1);

      boolean equals;
      if (type != null && type.equalsIgnoreCase("exact")) {
        if (tolerance > 0) {
          equals = firstGeometry.equalsExact(secondGeometry, tolerance);
        } else {
          equals = firstGeometry.equalsExact(secondGeometry);
        }
      } else if (type != null && type.equalsIgnoreCase("norm")) {
        equals = firstGeometry.equalsNorm(secondGeometry);
      } else if (type != null && type.equalsIgnoreCase("topo")) {
        equals = firstGeometry.equalsTopo(secondGeometry);
      } else {
        equals = firstGeometry.equals(secondGeometry);
      }

      return HttpResponse.ok(String.valueOf(equals)).contentType(new MediaType("text/plain"));
    } else {
      return HttpResponse.badRequest("A Geometry Collection with two geometries is required!");
    }


  }

}