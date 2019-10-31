package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.util.AffineTransformation;

@Controller("/scale")
public class ScaleController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "scale get xy", summary = "Scale", description = "Create a new geometry by scaling the input geometry")
  public HttpResponse getXY(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "X Scale") @QueryValue("xScale") double xScale,
      @Parameter(description = "Y Scale") @QueryValue("yScale") double yScale,
      @Parameter(description = "X") @QueryValue(value = "x", defaultValue = "NaN") double x,
      @Parameter(description = "Y") @QueryValue(value = "y", defaultValue = "NaN") double y
  ) throws Exception {
    return scale(from, to, geometryString, xScale, yScale, x, y);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "scale post xy", summary = "Scale", description = "Create a new geometry by scaling the input geometry")
  public HttpResponse post(
          @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
          @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
          @Parameter(description = "Input Geometry") @Body String geometryString,
          @Parameter(description = "X Scale") @QueryValue("xScale") double xScale,
          @Parameter(description = "Y Scale") @QueryValue("yScale") double yScale,
          @Parameter(description = "X") @QueryValue(value = "x", defaultValue = "NaN") double x,
          @Parameter(description = "Y") @QueryValue(value = "y", defaultValue = "NaN") double y
  ) throws Exception {
    return scale(from, to, geometryString, xScale, yScale, x, y);
  }

  private HttpResponse scale(String from, String to, String geometryString, double xScale, double yScale, double x, double y) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    AffineTransformation transformation;
    if (!Double.isNaN(x) && !Double.isNaN(y)) {
      transformation = AffineTransformation.scaleInstance(xScale, yScale, x,y );
    } else {
      transformation = AffineTransformation.scaleInstance(xScale, yScale);
    }
    Geometry scaledGeometry = transformation.transform(geometry);
    String content = writer.write(scaledGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}