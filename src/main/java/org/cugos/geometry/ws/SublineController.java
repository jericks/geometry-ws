package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Lineal;
import org.locationtech.jts.linearref.LengthIndexedLine;

@Controller("/subline")
public class SublineController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Subline", description = "Extract a sub-line from a lineal geometry.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Start Position") @QueryValue("start") double startPosition,
      @Parameter(description = "End Position") @QueryValue("end") double endPosition) throws Exception {
    return subLine(from, to, geometryString, startPosition, endPosition);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Subline", description = "Extract a sub-line from a lineal geometry.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Start Position") @QueryValue("start") double startPosition,
      @Parameter(description = "End Position") @QueryValue("end") double endPosition) throws Exception {
     return subLine(from, to, geometryString, startPosition, endPosition);
  }

  private HttpResponse subLine(String from, String to, String geometryString, double startPosition, double endPosition) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    if (!(geometry instanceof Lineal)) {
      return HttpResponse.badRequest("The input geometry must be a LineString or a MultiLineString!");
    }
    if (startPosition > endPosition) {
      return HttpResponse.badRequest("The start position must be less than the end position!");
    }
    LengthIndexedLine indexedLine = new LengthIndexedLine(geometry);
    double length = geometry.getLength();
    Geometry subLine = indexedLine.extractLine(startPosition * length, endPosition * length);
    return HttpResponse.ok(writer.write(subLine)).contentType(new MediaType(writer.getMediaType()));
  }

}