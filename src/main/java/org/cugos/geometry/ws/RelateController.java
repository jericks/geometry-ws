package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;

@Controller("/relate")
public class RelateController {
  
  @Get("/{from}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "relate get", summary = "Relate", description = "Determine if the input Geometry and the other Geometry are related according to the DE-9IM intersection matrix or calculate the DE-9IM.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "DE-9IM intersection matrix") @QueryValue(value = "matrix", defaultValue = "") String intersectionMatrix
  ) throws Exception {
    return relate(from, geometryString, intersectionMatrix);
  }

  @Post("/{from}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "relate post", summary = "Relate", description = "Determine if the input Geometry and the other Geometry are related according to the DE-9IM intersection matrix or calculate the DE-9IM.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "DE-9IM intersection matrix") @QueryValue(value = "matrix", defaultValue = "") String intersectionMatrix
  ) throws Exception {
    return relate(from, geometryString, intersectionMatrix);
  }

  private HttpResponse relate(String from, String geometryString, String intersectionMatrix) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    Geometry geometry = reader.read(geometryString);
    if (Validator.hasRequiredNumberOfGeometries(geometry, 2)) {
      Geometry firstGeometry = geometry.getGeometryN(0);
      Geometry secondGeometry = geometry.getGeometryN(1);
      String result;
      if (intersectionMatrix != null && !intersectionMatrix.isEmpty()) {
        boolean related = firstGeometry.relate(secondGeometry, intersectionMatrix);
        result = String.valueOf(related);
      } else {
        result = firstGeometry.relate(secondGeometry).toString();
      }
      return HttpResponse.ok(String.valueOf(result));
    } else {
      return HttpResponse.badRequest("A Geometry Collection with two geometries is required!");
    }
  }

}