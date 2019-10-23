package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;

@Controller("/symDifference")
public class SymDifferenceController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "symdifference get", summary = "SymDifference", description = "Calculate the symetric symDifference between two geometries")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString) throws Exception {
    return symDifference(from, to, geometryString);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "symdifference post", summary = "SymDifference", description = "Calculate the symetric symDifference between two geometries")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString) throws Exception {
    return symDifference(from, to, geometryString);
  }

  private HttpResponse symDifference(String from, String to, String geometryString) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    if (Validator.hasRequiredNumberOfGeometries(geometry, 2)) {
      Geometry firstGeometry = geometry.getGeometryN(0);
      Geometry secondGeometry = geometry.getGeometryN(1);
      Geometry result = firstGeometry.symDifference(secondGeometry);
      return HttpResponse.ok(writer.write(result)).contentType(new MediaType(writer.getMediaType()));
    } else {
      return HttpResponse.badRequest("A Geometry Collection with two geometries is required!");
    }
  }

}