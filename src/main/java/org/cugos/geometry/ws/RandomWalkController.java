package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller("/randomWalk")
public class RandomWalkController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "random walk get", summary = "Random Walk", description = "Generate a random walk")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Number of Walks") @QueryValue("number") int numberOfWalks,
      @Parameter(description = "Distance") @QueryValue("distance") double distance,
      @Parameter(description = "Change of Direction") @QueryValue("change") double changeOfDirectionProbability,
      @Parameter(description = "Angle Increment") @QueryValue("angle") int angleIncrement
  ) throws Exception {
    return randomWalk(from, to, geometryString, numberOfWalks, distance, changeOfDirectionProbability, angleIncrement);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "random walk post", summary = "Random Walk", description = "Generate a random walk")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Number of Walks") @QueryValue("number") int numberOfWalks,
      @Parameter(description = "Distance") @QueryValue("distance") double distance,
      @Parameter(description = "Change of Direction") @QueryValue("change") double changeOfDirectionProbability,
      @Parameter(description = "Angle Increment") @QueryValue("angle") int angleIncrement
  ) throws Exception {
     return randomWalk(from, to, geometryString, numberOfWalks, distance, changeOfDirectionProbability, angleIncrement);
  }

  private HttpResponse randomWalk(String from, String to, String geometryString,
    int numberOfWalks, double distance, double changeOfDirectionProbability, int angleIncrement
  ) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);

    Coordinate start = geometry.getCoordinate();

    List<Coordinate> coordinates = new ArrayList<Coordinate>();
    coordinates.add(start);

    int previousAngle = -1;
    Coordinate previousCoord = start;
    int angleRange = 360 / angleIncrement;

    for (int i = 0; i < numberOfWalks; i++) {
      Random random = new Random();
      double r = random.nextDouble();
      boolean changeDirection = r <= changeOfDirectionProbability;
      int angle;
      if (previousAngle > -1 || changeDirection) {
        while ((angle = random.nextInt(angleRange) * angleIncrement) == getOpposite(previousAngle)) {
          // Don't go back
        }
      } else {
        angle = random.nextInt(angleRange) * angleIncrement;
      }
      previousAngle = angle;
      previousCoord = getCoordinateAtAngle(previousCoord, angle, distance);
      coordinates.add(previousCoord);
    }
    LineString lineString = geometry.getFactory().createLineString(coordinates.toArray(new Coordinate[coordinates.size()]));

    String content = writer.write(lineString);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

  /**
   * Get a new Coordinate at the given angle and distance from the input Coordinate
   * @param coord The input Coordinate
   * @param angle The angle (in degrees)
   * @param distance The distance
   * @return A new Coordinate
   */
  private Coordinate getCoordinateAtAngle(Coordinate coord, double angle, double distance) {
    double angleAsRadians = Math.toRadians(angle);
    double x = coord.x + (distance * Math.cos(angleAsRadians));
    double y = coord.y + (distance * Math.sin(angleAsRadians));
    return new Coordinate(x, y);
  }

  /**
   * Get the opposite of the given angle
   * @param angle The angle (in degrees)
   * @return The opposite angle (+/- 180)
   */
  private double getOpposite(double angle) {
    if (angle >= 180) {
      return angle - 180;
    } else {
      return 180 + angle;
    }
  }

}