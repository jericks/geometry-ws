package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.linearref.LengthIndexedLine;

import java.util.ArrayList;
import java.util.List;

@Controller("/pointsAlong")
public class PointsAlongController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Points Along", description = "Create points along a linestring")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Distance") @QueryValue("distance") double distance
  ) throws Exception {
    return pointAlong(from, to, geometryString, distance);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Points Along", description = "Create points along a linestring")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Distance") @QueryValue("distance") double distance
  ) throws Exception {
     return pointAlong(from, to, geometryString, distance);
  }

  private HttpResponse pointAlong(String from, String to, String geometryString, double distance) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    MultiPoint multiPoint = geometry.getFactory().createMultiPoint(createPointsAlongGeometry(geometry, distance).toArray(new Point[]{}));
    String content = writer.write(multiPoint);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

  private List<Point> createPointsAlongGeometry(Geometry geometry, double distance) {
    if (geometry instanceof MultiLineString) {
      List<Point> points = new ArrayList<>();
      MultiLineString multiLineString = (MultiLineString) geometry;
      for(int i = 0; i < multiLineString.getNumGeometries(); i++) {
        Geometry subGeometry = multiLineString.getGeometryN(i);
        points.addAll(createPointsAlongGeometry(subGeometry, distance));
      }
      return points;
    } else if (geometry instanceof LineString) {
      return createPointsAlongLineString((LineString) geometry, distance);
    } else {
      throw new IllegalArgumentException("Unsupported Geometry Type!");
    }
  }

  private List<Point> createPointsAlongLineString(LineString lineString, double distance) {
    double lineLength = lineString.getLength();
    LengthIndexedLine lengthIndexedLine = new LengthIndexedLine(lineString);
    List<Point> points = new ArrayList<>();
    points.add(lineString.getStartPoint());
    double distanceAlongLine = distance;
    while(distanceAlongLine < lineLength) {
      Coordinate coordinate = lengthIndexedLine.extractPoint(distanceAlongLine);
      Point point = lineString.getFactory().createPoint(coordinate);
      points.add(point);
      distanceAlongLine = distanceAlongLine + distance;
    }
    return points;
  }

}