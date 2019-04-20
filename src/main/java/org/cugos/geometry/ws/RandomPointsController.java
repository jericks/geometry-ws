package org.cugos.geometry.ws;

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
  @Operation(summary = "Create the random points", description = "Create random points in a geometry")
  public String get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Number of Points") @QueryValue("number") int numberOfPoints) throws Exception {
    return randomPoints(from, to, geometryString, numberOfPoints, false, false, Double.NaN);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Calculate the randomPoints", description = "Calculate the randomPoints of a Geometry")
  public String post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Number of Points") @QueryValue("number") int numberOfPoints) throws Exception {
     return randomPoints(from, to, geometryString, numberOfPoints, false, false, Double.NaN);
  }

  private String randomPoints(String from, String to, String geometryString, int numberOfPoints, boolean isGridded, boolean isConstrainedToCircle, double gutterFraction) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);

    if (!(geometry instanceof Polygonal)) {
      geometry = geometry.getEnvelope();
    }
    if (!(geometry instanceof Polygonal)) {
      throw new IllegalArgumentException("Geometry must be a Polygon or MultiPolygon!");
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

    return writer.write(randomGeometry);
  }

}