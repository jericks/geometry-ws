package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.util.AffineTransformation;

@Controller("/shear")
public class ShearController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "shear get", summary = "Shear", description = "Create a new geometry by apply a shear affine transformation to the input geometry")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "X Distance") @QueryValue("x") double x,
      @Parameter(description = "Y Distance") @QueryValue("y") double y

  ) throws Exception {
    return shear(from, to, geometryString, x, y);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "shear post", summary = "Shear", description = "Create a new geometry by apply a shear affine transformation to the input geometry")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "X Distance") @QueryValue("x") double x,
      @Parameter(description = "Y Distance") @QueryValue("y") double y
  ) throws Exception {
     return shear(from, to, geometryString, x, y);
  }

  private HttpResponse shear(String from, String to, String geometryString, double x, double y) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    Geometry shearGeometry = AffineTransformation.shearInstance(x, y).transform(geometry);
    String content = writer.write(shearGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}