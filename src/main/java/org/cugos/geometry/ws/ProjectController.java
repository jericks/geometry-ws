package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.*;

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller("/project")
public class ProjectController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "project get", summary = "Project", description = "Project the input geometry from one coordinate system to another.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Source Projection") @QueryValue("source") String source,
      @Parameter(description = "Target Projection") @QueryValue("target") String target

  ) throws Exception {
    return project(from, to, geometryString, source, target);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "project post", summary = "Project", description = "Project the input geometry from one coordinate system to another.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Source Projection") @QueryValue("source") String source,
      @Parameter(description = "Target Projection") @QueryValue("target") String target
  ) throws Exception {
     return project(from, to, geometryString, source, target);
  }

  private HttpResponse project(String from, String to, String geometryString, String source, String target) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);

    CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
    CRSFactory csFactory = new CRSFactory();

    CoordinateReferenceSystem sourceCrs;
    if (isName(source)) {
      sourceCrs = csFactory.createFromName(source);
    } else {
      sourceCrs = csFactory.createFromParameters(null, source);
    }
    CoordinateReferenceSystem targetCrs;
    if (isName(target)) {
      targetCrs = csFactory.createFromName(target);
    } else {
      targetCrs = csFactory.createFromParameters(null, target);
    }

    CoordinateTransform transform = ctFactory.createTransform(sourceCrs, targetCrs);
    Geometry projectedGeometry = transformGeometry(transform, geometry);
    
    String content = writer.write(projectedGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

  private boolean isName(String projString) {
    final List<String> authorities = new ArrayList<String>(Arrays.asList(new String[]{"world", "nad83", "nad27", "esri", "epsg"}));
    int i = projString.indexOf(":");
    if (i > -1) {
      String authority = projString.substring(0, i).toLowerCase();
      String code = projString.substring(i + 1);
      if (authority.trim().length() > 0 && code.trim().length() > 0) {
        return authorities.contains(authority);
      }
    }
    return false;
  }

  private Geometry transformGeometry(CoordinateTransform transform, Geometry g) {
    if (g instanceof Point) {
      return transformPoint(transform, (Point) g);
    } else if (g instanceof LinearRing) {
      return transformLinearRing(transform, (LinearRing) g);
    } else if (g instanceof LineString) {
      return transformLineString(transform, (LineString) g);
    } else if (g instanceof Polygon) {
      return transformPolygon(transform, (Polygon) g);
    } else if (g instanceof MultiPoint) {
      return transformMultiPoint(transform, (MultiPoint) g);
    } else if (g instanceof MultiLineString) {
      return transformMultiLineString(transform, (MultiLineString) g);
    } else if (g instanceof MultiPolygon) {
      return transformMultiPolygon(transform, (MultiPolygon) g);
    } else if (g instanceof GeometryCollection) {
      return transformGeometryCollection(transform, (GeometryCollection) g);
    } else {
      return g;
    }
  }

  private Coordinate transformCoordinate(CoordinateTransform transform, Coordinate c) {
    ProjCoordinate in = new ProjCoordinate(c.x, c.y);
    ProjCoordinate out = new ProjCoordinate();
    transform.transform(in, out);
    return new Coordinate(out.x, out.y);
  }

  private Coordinate[] transformCoordinates(CoordinateTransform transform, Coordinate[] coords) {
    Coordinate[] projectedCoords = new Coordinate[coords.length];
    for (int i = 0; i < coords.length; i++) {
      projectedCoords[i] = transformCoordinate(transform, coords[i]);
    }
    return projectedCoords;
  }

  private Point transformPoint(CoordinateTransform transform, Point pt) {
    Coordinate c = transformCoordinate(transform, pt.getCoordinate());
    return pt.getFactory().createPoint(c);
  }

  private MultiPoint transformMultiPoint(CoordinateTransform transform, MultiPoint mp) {
    int num = mp.getNumGeometries();
    Point[] points = new Point[num];
    for (int i = 0; i < num; i++) {
      points[i] = transformPoint(transform, (Point) mp.getGeometryN(i));
    }
    return mp.getFactory().createMultiPoint(points);
  }

  private LineString transformLineString(CoordinateTransform transform, LineString line) {
    Coordinate[] coords = line.getCoordinates();
    Coordinate[] projectedCoords = new Coordinate[coords.length];
    for (int i = 0; i < coords.length; i++) {
      projectedCoords[i] = transformCoordinate(transform, coords[i]);
    }
    return line.getFactory().createLineString(projectedCoords);
  }

  private LinearRing transformLinearRing(CoordinateTransform transform, LinearRing ring) {
    Coordinate[] coords = ring.getCoordinates();
    Coordinate[] projectedCoords = new Coordinate[coords.length];
    for (int i = 0; i < coords.length; i++) {
      projectedCoords[i] = transformCoordinate(transform, coords[i]);
    }
    return ring.getFactory().createLinearRing(projectedCoords);
  }

  private MultiLineString transformMultiLineString(CoordinateTransform transform, MultiLineString ml) {
    int num = ml.getNumGeometries();
    LineString[] lines = new LineString[num];
    for (int i = 0; i < num; i++) {
      lines[i] = transformLineString(transform, (LineString) ml.getGeometryN(i));
    }
    return ml.getFactory().createMultiLineString(lines);
  }

  private Polygon transformPolygon(CoordinateTransform transform, Polygon polygon) {
    GeometryFactory factory = polygon.getFactory();
    LinearRing shell = factory.createLinearRing(transformCoordinates(transform, polygon.getExteriorRing().getCoordinates()));
    int num = polygon.getNumInteriorRing();
    LinearRing[] holes = new LinearRing[num];
    for (int i = 0; i < num; i++) {
      holes[i] = factory.createLinearRing(transformCoordinates(transform, polygon.getInteriorRingN(i).getCoordinates()));
    }
    return polygon.getFactory().createPolygon(shell, holes);
  }

  private MultiPolygon transformMultiPolygon(CoordinateTransform transform, MultiPolygon ml) {
    int num = ml.getNumGeometries();
    Polygon[] polygons = new Polygon[num];
    for (int i = 0; i < num; i++) {
      polygons[i] = transformPolygon(transform, (Polygon) ml.getGeometryN(i));
    }
    return ml.getFactory().createMultiPolygon(polygons);
  }

  private GeometryCollection transformGeometryCollection(CoordinateTransform transform, GeometryCollection gc) {
    int num = gc.getNumGeometries();
    Geometry[] geometries = new Geometry[num];
    for (int i = 0; i < num; i++) {
      geometries[i] = transformGeometry(transform, gc.getGeometryN(i));
    }
    return gc.getFactory().createGeometryCollection(geometries);
  }

}