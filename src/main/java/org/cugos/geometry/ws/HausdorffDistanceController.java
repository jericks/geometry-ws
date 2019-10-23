package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.algorithm.distance.DiscreteHausdorffDistance;
import org.locationtech.jts.geom.Geometry;

@Controller("/hausdorffDistance")
public class HausdorffDistanceController {
  
  @Get("/{from}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "hausdorff distance get", summary = "Hausdorff Distance", description = "Get the hausdorff distance between two geometries")
  public HttpResponse<String> get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString) throws Exception {
    return distance(from, geometryString);
  }

  @Post("/{from}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "hausdorff distance post", summary = "Hausdorff Distance", description = "Get the hausdorff distance between two geometries")
  public HttpResponse<String> post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @Body String geometryString) throws Exception {
     return distance(from, geometryString);
  }

  private HttpResponse<String> distance(String from, String geometryString) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    Geometry geometry = reader.read(geometryString);
    if (Validator.hasRequiredNumberOfGeometries(geometry, 2)) {
      Geometry firstGeometry = geometry.getGeometryN(0);
      Geometry secondGeometry = geometry.getGeometryN(1);
      double result = DiscreteHausdorffDistance.distance(firstGeometry, secondGeometry);
      return HttpResponse.ok(String.valueOf(result));
    } else {
      return HttpResponse.badRequest("A Geometry Collection with two geometries is required!");
    }
  }

}