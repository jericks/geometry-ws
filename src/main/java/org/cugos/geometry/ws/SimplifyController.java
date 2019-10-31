package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;
import org.locationtech.jts.simplify.VWSimplifier;

@Controller("/simplify")
public class SimplifyController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "simplify get", summary = "Simplify", description = "Simplify the input geometry.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Algorithm") @QueryValue("algorithm") String algorithm,
      @Parameter(description = "Tolerance") @QueryValue("tolerance") double tolerance
  ) throws Exception {
    return simplify(from, to, geometryString, algorithm, tolerance);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "simplify post", summary = "Simplify", description = "Simplify the input geometry.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Algorithm") @QueryValue("algorithm") String algorithm,
      @Parameter(description = "Tolerance") @QueryValue("tolerance") double tolerance
  ) throws Exception {
     return simplify(from, to, geometryString, algorithm, tolerance);
  }

  private HttpResponse simplify(String from, String to, String geometryString, String algorithm, double tolerance) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    Geometry outputGeometry;
    if (algorithm.equalsIgnoreCase("douglaspeucker")
            || algorithm.equalsIgnoreCase("dp")) {
      outputGeometry = DouglasPeuckerSimplifier.simplify(geometry, tolerance);
    } else if (algorithm.equalsIgnoreCase("topologypreserving")
            || algorithm.equalsIgnoreCase("tp")) {
      outputGeometry = TopologyPreservingSimplifier.simplify(geometry, tolerance);
    } else if (algorithm.equalsIgnoreCase("visvalingamwhyat")
            || algorithm.equalsIgnoreCase("vw")) {
      outputGeometry = VWSimplifier.simplify(geometry, tolerance);
    } else {
      return HttpResponse.badRequest("Unknown simplifier algorithm!");
    }
    String content = writer.write(outputGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}