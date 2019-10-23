package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.util.GeometricShapeFactory;

@Controller("/arc")
public class ArcController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "arc get", summary = "Arc", description = "Creates an arc linestring from a start angle and an angle extent.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Width") @QueryValue("width") int width,
      @Parameter(description = "Height") @QueryValue("height") int height,
      @Parameter(description = "Number of Points") @QueryValue("numberOfPoints") int numberOfPoints,
      @Parameter(description = "Rotation") @QueryValue("rotation") double rotation,
      @Parameter(description = "Center") @QueryValue("center") boolean center,
      @Parameter(description = "Start Angle") @QueryValue("startAngle") double startAngle,
      @Parameter(description = "End Angle") @QueryValue("angleExtent") double angleExtent,
      @Parameter(description = "Degrees") @QueryValue("degrees") boolean degrees
  ) throws Exception {
    return createArc(from, to, geometryString, 
            width, height, numberOfPoints, rotation, center, 
            startAngle, angleExtent, degrees);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "arc post", summary = "Arc", description = "Creates an arc linestring from a start angle and an angle extent.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Width") @QueryValue("width") int width,
      @Parameter(description = "Height") @QueryValue("height") int height,
      @Parameter(description = "Number of Points") @QueryValue("numberOfPoints") int numberOfPoints,
      @Parameter(description = "Rotation") @QueryValue("rotation") double rotation,
      @Parameter(description = "Center") @QueryValue("center") boolean center,
      @Parameter(description = "Start Angle") @QueryValue("startAngle") double startAngle,
      @Parameter(description = "End Angle") @QueryValue("angleExtent") double angleExtent,
      @Parameter(description = "Degrees") @QueryValue("degrees") boolean degrees
  ) throws Exception {
     return createArc(from, to, geometryString,
             width, height, numberOfPoints, rotation, center,
             startAngle, angleExtent, degrees);
  }

  private HttpResponse createArc(String from, String to, String geometryString, 
    int width, int height, int numberOfPoints, double rotation, boolean center, 
    double startAngle, double angleExtent, boolean degrees
  ) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    GeometricShapeFactory geometricShapeFactory = new GeometricShapeFactory();
    Geometries.prepareGeometricShapeFactory(geometricShapeFactory, geometry, width, height, numberOfPoints, rotation, center);
    if (degrees) {
      startAngle = Math.toRadians(startAngle);
      angleExtent = Math.toRadians(angleExtent);
    }
    Geometry arc = geometricShapeFactory.createArc(startAngle, angleExtent);
    String content = writer.write(arc);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}