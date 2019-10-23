package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.util.SineStarFactory;

@Controller("/sineStar")
public class SineStarController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "sinestar get", summary = "Sine Star", description = "Create a sine star from the input geometry.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Width") @QueryValue("width") int width,
      @Parameter(description = "Height") @QueryValue("height") int height,
      @Parameter(description = "Number of Points") @QueryValue("numberOfPoints") int numberOfPoints,
      @Parameter(description = "Rotation") @QueryValue("rotation") double rotation,
      @Parameter(description = "Center") @QueryValue("center") boolean center,
      @Parameter(description = "Number of Arms") @QueryValue("numberOfArms") int numberOfArms,
      @Parameter(description = "Arm Length Ratio") @QueryValue("armLengthRatio") double armLengthRatio
  ) throws Exception {
    return createSineStar(from, to, geometryString,
            width, height, numberOfPoints, rotation, center, numberOfArms, armLengthRatio);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "sinestar post", summary = "Sine Star", description = "Create a sine star from the input geometry.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Width") @QueryValue("width") int width,
      @Parameter(description = "Height") @QueryValue("height") int height,
      @Parameter(description = "Number of Points") @QueryValue("numberOfPoints") int numberOfPoints,
      @Parameter(description = "Rotation") @QueryValue("rotation") double rotation,
      @Parameter(description = "Center") @QueryValue("center") boolean center,
      @Parameter(description = "Number of Arms") @QueryValue("numberOfArms") int numberOfArms,
      @Parameter(description = "Arm Length Ratio") @QueryValue("armLengthRatio") double armLengthRatio
  ) throws Exception {
     return createSineStar(from, to, geometryString,
             width, height, numberOfPoints, rotation, center, numberOfArms, armLengthRatio);
  }

  private HttpResponse createSineStar(String from, String to, String geometryString,
                                      int width, int height, int numberOfPoints, double rotation, boolean center,
                                      int numberOfArms, double armLengthRatio
  ) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    SineStarFactory sineStarFactory = new SineStarFactory();
    sineStarFactory.setArmLengthRatio(armLengthRatio);
    sineStarFactory.setNumArms(numberOfArms);
    Geometries.prepareGeometricShapeFactory(sineStarFactory, geometry, width, height, numberOfPoints, rotation, center);
    Geometry sineStar = sineStarFactory.createSquircle();
    String content = writer.write(sineStar);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}