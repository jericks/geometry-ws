package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.util.GeometricShapeFactory;

@Controller("/ellipse")
public class EllipseController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "ellipse get", summary = "Ellipse", description = "Create a ellipse from the input geometry.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Width") @QueryValue("width") int width,
      @Parameter(description = "Height") @QueryValue("height") int height,
      @Parameter(description = "Number of Points") @QueryValue("numberOfPoints") int numberOfPoints,
      @Parameter(description = "Rotation") @QueryValue("rotation") double rotation,
      @Parameter(description = "Center") @QueryValue("center") boolean center
  ) throws Exception {
    return createEllipse(from, to, geometryString,
            width, height, numberOfPoints, rotation, center);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "ellipse post", summary = "Ellipse", description = "Create a ellipse from the input geometry.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Width") @QueryValue("width") int width,
      @Parameter(description = "Height") @QueryValue("height") int height,
      @Parameter(description = "Number of Points") @QueryValue("numberOfPoints") int numberOfPoints,
      @Parameter(description = "Rotation") @QueryValue("rotation") double rotation,
      @Parameter(description = "Center") @QueryValue("center") boolean center
  ) throws Exception {
     return createEllipse(from, to, geometryString,
             width, height, numberOfPoints, rotation, center);
  }

  private HttpResponse createEllipse(String from, String to, String geometryString,
    int width, int height, int numberOfPoints, double rotation, boolean center
  ) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    GeometricShapeFactory geometricShapeFactory = new GeometricShapeFactory();
    Geometries.prepareGeometricShapeFactory(geometricShapeFactory, geometry, width, height, numberOfPoints, rotation, center);
    Geometry rectangle = geometricShapeFactory.createEllipse();
    String content = writer.write(rectangle);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}