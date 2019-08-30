package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.shape.fractal.MortonCurveBuilder;

@Controller("/mortonCurve")
public class MortonCurveController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Morton Curve", description = "Calculate a Morton Curve around a Geometry")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Number of Points") @QueryValue("number") int numberOfPoints) throws Exception {
    return mortonCurve(from, to, geometryString, numberOfPoints);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Morton Curve", description = "Calculate a Morton Curve around a Geometry")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Number of Points") @QueryValue("number") int numberOfPoints) throws Exception {
     return mortonCurve(from, to, geometryString, numberOfPoints);
  }

  private HttpResponse mortonCurve(String from, String to, String geometryString, int numberOfPoints) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);

    MortonCurveBuilder builder = new MortonCurveBuilder(new GeometryFactory());
    builder.setExtent(geometry.getEnvelopeInternal());
    builder.setNumPoints(numberOfPoints);
    Geometry outputGeometry = builder.getGeometry();
    
    String content = writer.write(outputGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}