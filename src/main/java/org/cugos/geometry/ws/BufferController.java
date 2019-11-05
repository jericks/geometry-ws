package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;

@Controller("/buffer")
public class BufferController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "buffer get", summary = "Buffer a geometry", description = "Buffer a geometry with a given distance")
  public HttpResponse bufferGet(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Buffer Distance") @QueryValue("d") double distance,
      @Parameter(description = "Quadrant Segments") @QueryValue(value = "q", defaultValue = "8") int quadrantSegements,
      @Parameter(description = "End Cap Style") @QueryValue(value = "c", defaultValue = "round") String endCapStyle,
      @Parameter(description = "Single Sided") @QueryValue(value = "s", defaultValue = "false") boolean singleSided,
      @Parameter(description = "Simplify Factor") @QueryValue(value = "f", defaultValue = "0.01") double simplifyFactor,
      @Parameter(description = "Mitre Limit") @QueryValue(value = "m", defaultValue = "5.0") double mitreLimit,
      @Parameter(description = "Join Style") @QueryValue(value = "j", defaultValue = "round") String joinStyle
  ) throws Exception {
    return buffer(from, to, geometryString, distance, quadrantSegements, endCapStyle, singleSided, simplifyFactor, mitreLimit, joinStyle);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "buffer post", summary = "Buffer a geometry", description = "Buffer a geometry with a given distance")
  public HttpResponse bufferPost(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Buffer Distance") @QueryValue("d") double distance,
      @Parameter(description = "Quadrant Segments") @QueryValue(value = "q", defaultValue = "8") int quadrantSegements,
      @Parameter(description = "End Cap Style") @QueryValue(value = "c", defaultValue = "round") String endCapStyle,
      @Parameter(description = "Single Sided") @QueryValue(value = "s", defaultValue = "false") boolean singleSided,
      @Parameter(description = "Simplify Factor") @QueryValue(value = "f", defaultValue = "0.01") double simplifyFactor,
      @Parameter(description = "Mitre Limit") @QueryValue(value = "m", defaultValue = "5.0") double mitreLimit,
      @Parameter(description = "Join Style") @QueryValue(value = "j", defaultValue = "round") String joinStyle
  ) throws Exception {
     return buffer(from, to, geometryString, distance, quadrantSegements, endCapStyle, singleSided, simplifyFactor, mitreLimit, joinStyle);
  }

  private HttpResponse buffer(String from, String to, String geometryString, double distance,
    int quadrantSegements, String endCapStyle, boolean singleSided, double simplifyFactor,
    double mitreLimit, String joinStyleName
  ) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);

      int capStyle;
      if (endCapStyle.equalsIgnoreCase("butt")
              || endCapStyle.equalsIgnoreCase("flat")) {
          capStyle = BufferParameters.CAP_FLAT;
      } else if (endCapStyle.equalsIgnoreCase("square")) {
          capStyle = BufferParameters.CAP_SQUARE;
      } else {
          capStyle = BufferParameters.CAP_ROUND;
      }

      int joinStyle;
      if (joinStyleName.equalsIgnoreCase("mitre")) {
          joinStyle = BufferParameters.JOIN_MITRE;
      } else if (joinStyleName.equalsIgnoreCase("bevel")) {
          joinStyle = BufferParameters.JOIN_BEVEL;
      } else {
          joinStyle = BufferParameters.JOIN_ROUND;
      }

      BufferParameters params = new BufferParameters();
      params.setSingleSided(singleSided);
      params.setQuadrantSegments(quadrantSegements);
      params.setEndCapStyle(capStyle);
      params.setSimplifyFactor(simplifyFactor);
      params.setMitreLimit(mitreLimit);
      params.setJoinStyle(joinStyle);

      BufferOp bufferOp = new BufferOp(geometry, params);
      Geometry bufferedGeometry = bufferOp.getResultGeometry(distance);

    String content = writer.write(bufferedGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}