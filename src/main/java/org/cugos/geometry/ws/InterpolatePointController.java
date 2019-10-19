package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.linearref.LengthIndexedLine;

@Controller("/interpolatePoint")
public class InterpolatePointController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Interpolate Point", description = "Interpolate the location of a point on the input linear geometry given a percentage position.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Position") @QueryValue("position") double position
  ) throws Exception {
    return interpolatePoint(from, to, geometryString, position);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Interpolate Point", description = "Interpolate the location of a point on the input linear geometry given a percentage position.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Position") @QueryValue("position") double position
  ) throws Exception {
     return interpolatePoint(from, to, geometryString, position);
  }

  private HttpResponse interpolatePoint(String from, String to, String geometryString, double position) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    if (!(geometry instanceof Lineal)) {
      return HttpResponse.badRequest("The input geometry must be a LineString or a MultiLineString!");
    }
    LengthIndexedLine indexedLine = new LengthIndexedLine(geometry);
    double length = geometry.getLength();
    Coordinate coord = indexedLine.extractPoint(position * length);
    GeometryFactory factory = new GeometryFactory();
    Point point = factory.createPoint(coord);
    String content = writer.write(point);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}