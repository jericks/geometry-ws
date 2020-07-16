package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.algorithm.construct.MaximumInscribedCircle;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.operation.buffer.VariableBuffer;

import java.util.Arrays;

@Controller("/variableBuffer")
public class VariableBufferController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "variable buffer get", summary = "Calculate the variable buffer", description = "Create a variable distance buffer")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Distances") @QueryValue("distances") String distances) throws Exception {
    return variableBuffer(from, to, geometryString, distances);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "variable buffer post", summary = "Calculate the variable buffer", description = "Calculate a variable distance buffer")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Distances") @QueryValue("distances") String distances) throws Exception {
     return variableBuffer(from, to, geometryString, distances);
  }

  private HttpResponse variableBuffer(String from, String to, String geometryString, String distancesStr) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    Geometry outputGeometry;
    double[] distances = Arrays.asList(distancesStr.split(","))
            .stream()
            .mapToDouble(Double::parseDouble).toArray();
    if (distances.length == 2) {
      outputGeometry = VariableBuffer.buffer(geometry, distances[0], distances[1]);
    } else if (distances.length == 3) {
      outputGeometry = VariableBuffer.buffer(geometry, distances[0], distances[1], distances[2]);
    }  else {
      outputGeometry = VariableBuffer.buffer(geometry, distances);
    }
    String content = writer.write(outputGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}