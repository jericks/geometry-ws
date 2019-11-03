package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.util.AffineTransformation;

@Controller("/rotate")
public class RotateController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "rotate get", summary = "Rotate", description = "Create a new geometry by rotating the input geometry")
  public HttpResponse getXY(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "theta") @QueryValue(value = "theta", defaultValue="NaN") double theta,
      @Parameter(description = "x") @QueryValue(value = "x", defaultValue="NaN") double x,
      @Parameter(description = "y") @QueryValue(value = "y", defaultValue = "NaN") double y,
      @Parameter(description = "sinTheta") @QueryValue(value = "sinTheta", defaultValue = "NaN") double sinTheta,
      @Parameter(description = "cosTheta") @QueryValue(value = "cosTheta", defaultValue = "NaN") double cosTheta
  ) throws Exception {
    return rotate(from, to, geometryString, theta, x, y, sinTheta, cosTheta);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "rotate post", summary = "Rotate", description = "Create a new geometry by rotating the input geometry")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "theta") @QueryValue(value = "theta", defaultValue="NaN") double theta,
      @Parameter(description = "x") @QueryValue(value = "x", defaultValue="NaN") double x,
      @Parameter(description = "y") @QueryValue(value = "y", defaultValue = "NaN") double y,
      @Parameter(description = "sinTheta") @QueryValue(value = "sinTheta", defaultValue = "NaN") double sinTheta,
      @Parameter(description = "cosTheta") @QueryValue(value = "cosTheta", defaultValue = "NaN") double cosTheta
  ) throws Exception {
     return rotate(from, to, geometryString, theta, x, y, sinTheta, cosTheta);
  }

  private HttpResponse rotate(String from, String to, String geometryString, double theta, double x, double y, double sinTheta, double cosTheta) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    AffineTransformation transformation;
    // theta, x, y
    if (!Double.isNaN(theta) && !Double.isNaN(x) && !Double.isNaN(y)) {
      transformation = AffineTransformation.rotationInstance(theta, x, y);
    } // theta
    else if (!Double.isNaN(theta)) {
      transformation = AffineTransformation.rotationInstance(theta);
    } // sinTheta, cosTheta, x, y
    else if (!Double.isNaN(sinTheta) && !Double.isNaN(cosTheta) && !Double.isNaN(x) && !Double.isNaN(y)) {
      transformation = AffineTransformation.rotationInstance(sinTheta, cosTheta, x, y);
    } // sinTheta, cosTheta
    else if (!Double.isNaN(sinTheta) && !Double.isNaN(cosTheta)) {
      transformation = AffineTransformation.rotationInstance(sinTheta, cosTheta);
    } else {
      return HttpResponse.badRequest("Illegal combination of arguments (theta | theta,x,y | sinTheta, cosTheta, x, y | sinTheta, cosTheta");
    }
    Geometry rotatedGeometry = transformation.transform(geometry);
    String content = writer.write(rotatedGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}