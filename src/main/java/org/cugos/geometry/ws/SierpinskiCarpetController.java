package org.cugos.geometry.ws;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.shape.fractal.SierpinskiCarpetBuilder;

@Controller("/sierpinskiCarpet")
public class SierpinskiCarpetController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Calculate Sierpinski Carpet", description = "Calculate a Sierpinski Carpet around a Geometry")
  public String get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Number of Points") @QueryValue("number") int numberOfPoints) throws Exception {
    return sierpinskiCarpet(from, to, geometryString, numberOfPoints);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Calculate Sierpinski Carpet", description = "Calculate a Sierpinski Carpet around a Geometry")
  public String post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Number of Points") @QueryValue("number") int numberOfPoints) throws Exception {
     return sierpinskiCarpet(from, to, geometryString, numberOfPoints);
  }

  private String sierpinskiCarpet(String from, String to, String geometryString, int numberOfPoints) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);

    SierpinskiCarpetBuilder builder = new SierpinskiCarpetBuilder(new GeometryFactory());
    builder.setExtent(geometry.getEnvelopeInternal());
    builder.setNumPoints(numberOfPoints);
    Geometry outputGeometry = builder.getGeometry();
    
    return writer.write(outputGeometry);
  }

}