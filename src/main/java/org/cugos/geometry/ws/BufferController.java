package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;

@Controller("/buffer")
public class BufferController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "buffer get", summary = "Buffer a geometry", description = "Buffer a geometry with a given distance")
  public HttpResponse bufferGet(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Buffer Distance") @QueryValue("d") double distance) throws Exception {
    return buffer(from, to, geometryString, distance);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "buffer post", summary = "Buffer a geometry", description = "Buffer a geometry with a given distance")
  public HttpResponse bufferPost(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Buffer Distance") @QueryValue("d") double distance) throws Exception {
     return buffer(from, to, geometryString, distance);
  }

  private HttpResponse buffer(String from, String to, String geometryString, double distance) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    Geometry bufferedGeometry = geometry.buffer(distance);
    String content = writer.write(bufferedGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}