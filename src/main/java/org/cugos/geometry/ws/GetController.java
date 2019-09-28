package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;

@Controller("/get")
public class GetController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Get", description = "Get a sub geometry from a geometry collection by index")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Index") @QueryValue("index") int index) throws Exception {
    return getAtIndex(from, to, geometryString, index);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Get", description = "Get a sub geometry from a geometry collection by index")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Index") @QueryValue("index") int index) throws Exception {
    return getAtIndex(from, to, geometryString, index);
  }

  private HttpResponse getAtIndex(String from, String to, String geometryString, int index) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    if (geometry.getNumGeometries() >= (index + 1)) {
      Geometry result = geometry.getGeometryN(index);
      return HttpResponse.ok(writer.write(result)).contentType(new MediaType(writer.getMediaType()));
    } else {
      return HttpResponse.badRequest(String.format("The Geometry needs at least %s geometries", index + 1));
    }
  }

}