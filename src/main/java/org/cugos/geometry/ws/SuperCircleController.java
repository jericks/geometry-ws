package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.util.GeometricShapeFactory;

@Controller("/superCircle")
public class SuperCircleController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "super circle get", summary = "Super Circle", description = "Create a super circle from the input geometry.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Width") @QueryValue("width") int width,
      @Parameter(description = "Height") @QueryValue("height") int height,
      @Parameter(description = "Number of Points") @QueryValue("numberOfPoints") int numberOfPoints,
      @Parameter(description = "Rotation") @QueryValue("rotation") double rotation,
      @Parameter(description = "Center") @QueryValue("center") boolean center,
      @Parameter(description = "Power") @QueryValue("power") double power
  ) throws Exception {
    return createSuperCircle(from, to, geometryString,
            width, height, numberOfPoints, rotation, center, power);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "super circle post", summary = "Super Circle", description = "Create a super circle from the input geometry.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Width") @QueryValue("width") int width,
      @Parameter(description = "Height") @QueryValue("height") int height,
      @Parameter(description = "Number of Points") @QueryValue("numberOfPoints") int numberOfPoints,
      @Parameter(description = "Rotation") @QueryValue("rotation") double rotation,
      @Parameter(description = "Center") @QueryValue("center") boolean center,
      @Parameter(description = "Power") @QueryValue("power") double power
  ) throws Exception {
     return createSuperCircle(from, to, geometryString,
             width, height, numberOfPoints, rotation, center, power);
  }

  private HttpResponse createSuperCircle(String from, String to, String geometryString,
    int width, int height, int numberOfPoints, double rotation, boolean center, double power
  ) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    GeometricShapeFactory geometricShapeFactory = new GeometricShapeFactory();
    Geometries.prepareGeometricShapeFactory(geometricShapeFactory, geometry, width, height, numberOfPoints, rotation, center);
    if (geometry instanceof Point && !center) {
      Point point = (Point) geometry;
      Coordinate coord = new Coordinate(point.getX() + width / 2, point.getY() + height / 2);
      geometricShapeFactory.setCentre(coord);
    }
    Geometry rectangle = geometricShapeFactory.createSupercircle(power);
    String content = writer.write(rectangle);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}