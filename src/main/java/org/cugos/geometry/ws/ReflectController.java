package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.util.AffineTransformation;

@Controller("/reflect")
public class ReflectController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "reflect get xy", summary = "Reflect", description = "Create a new geometry by apply a reflect affine transformation to the input geometry")
  public HttpResponse getXY(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "X0") @QueryValue("x0") double x0,
      @Parameter(description = "Y0") @QueryValue("y0") double y0,
      @Parameter(description = "X1") @QueryValue(value = "x1", defaultValue = "NaN") double x1,
      @Parameter(description = "Y1") @QueryValue(value = "y1", defaultValue = "NaN") double y1
  ) throws Exception {
    return reflect(from, to, geometryString, x0, y0, x1, y1);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "reflect post xy", summary = "Reflect", description = "Create a new geometry by apply a reflect affine transformation to the input geometry")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "X0") @QueryValue("x0") double x0,
      @Parameter(description = "Y0") @QueryValue("y0") double y0,
      @Parameter(description = "X1") @QueryValue(value = "x1", defaultValue = "NaN") double x1,
      @Parameter(description = "Y1") @QueryValue(value = "y1", defaultValue = "NaN") double y1
  ) throws Exception {
     return reflect(from, to, geometryString, x0, y0, x1, y1);
  }

  private HttpResponse reflect(String from, String to, String geometryString, double x0, double y0, double x1, double y1) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    AffineTransformation transformation;
    if (!Double.isNaN(x1) && !Double.isNaN(y1)) {
      transformation = AffineTransformation.reflectionInstance(x0, y0, x1, y1);
    } else {
      transformation = AffineTransformation.reflectionInstance(x0, y0);
    }
    Geometry reflectedGeometry = transformation.transform(geometry);
    String content = writer.write(reflectedGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}