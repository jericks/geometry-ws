package org.cugos.geometry.ws;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.OctagonalEnvelope;

@Controller("/octagonalEnvelope")
public class OctagonalEnvelopeController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Get Octagonal Envelope", description = "Get the Octagonal Envelope of a Geometry")
  public String get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString) throws Exception {
    return octagonalEnvelope(from, to, geometryString);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Get Octagonal Envelope", description = "Get the Octagonal Envelope of a Geometry")
  public String post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString) throws Exception {
     return octagonalEnvelope(from, to, geometryString);
  }

  private String octagonalEnvelope(String from, String to, String geometryString) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    OctagonalEnvelope octagonalEnvelope = new OctagonalEnvelope(geometry);
    Geometry outputGeometry = octagonalEnvelope.toGeometry(geometry.getFactory());
    return writer.write(outputGeometry);
  }

}