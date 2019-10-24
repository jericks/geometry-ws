package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.*;

@Controller("/pointAtAngle")
public class PointAtAngleController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "point at angle get", summary = "Point At Angle", description = "Get a Point at Angle")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Angle") @QueryValue("angle") double angle,
      @Parameter(description = "Distance") @QueryValue("distance") double distance
  ) throws Exception {
    return pointAtAngle(from, to, geometryString, angle, distance);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "point at angle post", summary = "Point At Angle", description = "Get a Point at Angle")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Angle") @QueryValue("angle") double angle,
      @Parameter(description = "Distance") @QueryValue("distance") double distance
  ) throws Exception {
     return pointAtAngle(from, to, geometryString, angle, distance);
  }

  private HttpResponse pointAtAngle(String from, String to, String geometryString, double angle, double distance) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    Coordinate start = geometry.getCoordinate();
    double angleAsRadians = Math.toRadians(angle);
    double x = start.x + (distance * Math.cos(angleAsRadians));
    double y = start.y + (distance * Math.sin(angleAsRadians));
    Coordinate newCoordinate = new Coordinate(x, y);
    Point point = geometry.getFactory().createPoint(newCoordinate);
    String content = writer.write(point);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }


}