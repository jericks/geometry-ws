package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygonal;
import org.locationtech.jts.shape.random.RandomPointsBuilder;
import org.locationtech.jts.shape.random.RandomPointsInGridBuilder;

@Controller("/randomPoints")
public class RandomPointsController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "random points get", summary = "Create the random points", description = "Create random points in a geometry")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Number of Points") @QueryValue("number") int numberOfPoints,
      @Parameter(description = "Is Gridded") @QueryValue(value = "gridded", defaultValue = "false") boolean isGridded,
      @Parameter(description = "Is Constrained to Circle") @QueryValue(value = "constrainedToCircle", defaultValue = "false") boolean isConstrainedToCircle,
      @Parameter(description = "Gutter Fraction") @QueryValue(value = "gutterFraction", defaultValue = "NaN") double gutterFraction) throws Exception {
    return randomPoints(from, to, geometryString, numberOfPoints, isGridded, isConstrainedToCircle, gutterFraction);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "random points post", summary = "Calculate the randomPoints", description = "Calculate the randomPoints of a Geometry")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Number of Points") @QueryValue("number") int numberOfPoints,
      @Parameter(description = "Is Gridded") @QueryValue(value = "gridded", defaultValue = "false") boolean isGridded,
      @Parameter(description = "Is Constrained to Circle") @QueryValue(value = "constrainedToCircle", defaultValue = "false") boolean isConstrainedToCircle,
      @Parameter(description = "Gutter Fraction") @QueryValue(value = "gutterFraction", defaultValue = "NaN") double gutterFraction) throws Exception {
     return randomPoints(from, to, geometryString, numberOfPoints, isGridded, isConstrainedToCircle, gutterFraction);
  }

  private HttpResponse randomPoints(String from, String to, String geometryString, int numberOfPoints, boolean isGridded, boolean isConstrainedToCircle, double gutterFraction) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);

    if (!(geometry instanceof Polygonal)) {
      geometry = geometry.getEnvelope();
    }
    if (!(geometry instanceof Polygonal)) {
      return HttpResponse.badRequest("Geometry must be a Polygon or MultiPolygon!");
    }
    Geometry randomGeometry;
    if (isGridded) {
      RandomPointsInGridBuilder builder = new RandomPointsInGridBuilder();
      builder.setExtent(geometry.getEnvelopeInternal());
      builder.setNumPoints(numberOfPoints);
      builder.setConstrainedToCircle(isConstrainedToCircle);
      if (!Double.isNaN(gutterFraction)) {
        builder.setGutterFraction(gutterFraction);
      }
      randomGeometry = builder.getGeometry();
    } else {
      RandomPointsBuilder builder = new RandomPointsBuilder();
      builder.setExtent(geometry);
      builder.setNumPoints(numberOfPoints);
      randomGeometry = builder.getGeometry();
    }

    String content = writer.write(randomGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}