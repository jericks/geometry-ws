package org.cugos.geometry.ws;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;

@Controller("/type")
public class TypeController {
  
  @Get("/{from}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "type get", summary = "Get Type", description = "Get the geometry type of a Geometry")
  public String get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString) throws Exception {
    return type(from, geometryString);
  }

  @Post("/{from}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "type post", summary = "Get type", description = "Get the type of a Geometry")
  public String post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @Body String geometryString) throws Exception {
     return type(from, geometryString);
  }

  private String type(String from, String geometryString) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    Geometry geometry = reader.read(geometryString);
    String value = geometry.getGeometryType();
    return String.valueOf(value);
  }

}