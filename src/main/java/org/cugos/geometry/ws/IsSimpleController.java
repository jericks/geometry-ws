package org.cugos.geometry.ws;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;

@Controller("/isSimple")
public class IsSimpleController {
  
  @Get("/{from}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "is simple get", summary = "Is Simple", description = "Determine if the input geometry is simple or not.")
  public String get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString) throws Exception {
    return isSimple(from, geometryString);
  }

  @Post("/{from}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "is simple post", summary = "Is Simple", description = "Determine if the input geometry is simple or not.")
  public String post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @Body String geometryString) throws Exception {
     return isSimple(from, geometryString);
  }

  private String isSimple(String from, String geometryString) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    Geometry geometry = reader.read(geometryString);
    boolean isSimple = geometry.isSimple();
    return String.valueOf(String.valueOf(isSimple));
  }

}