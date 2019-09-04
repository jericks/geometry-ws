package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.algorithm.MinimumDiameter;
import org.locationtech.jts.geom.Geometry;

@Controller("/minimumDiameter")
public class MinimumDiameterController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Calculate Minimum Diameter", description = "Calculate Minimum Diameter of a Geometry")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString) throws Exception {
    return minimumDiameter(from, to, geometryString);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Calculate Minimum Diameter", description = "Calculate Minimum Diameter of a Geometry")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString) throws Exception {
     return minimumDiameter(from, to, geometryString);
  }

  private HttpResponse minimumDiameter(String from, String to, String geometryString) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    MinimumDiameter minimumDiameter = new MinimumDiameter(geometry);
    Geometry outputGeometry = minimumDiameter.getDiameter();
    String content = writer.write(outputGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}