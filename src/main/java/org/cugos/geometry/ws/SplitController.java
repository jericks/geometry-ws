package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.operation.polygonize.Polygonizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller("/split")
public class SplitController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "split get", summary = "Split", description = "Split a geometry by another geometry")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString) throws Exception {
    return split(from, to, geometryString);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "split post", summary = "Split", description = "Split a geometry by another geometry")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString) throws Exception {
    return split(from, to, geometryString);
  }

  private HttpResponse split(String from, String to, String geometryString) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    if (Validator.hasRequiredNumberOfGeometries(geometry, 2)) {
      Geometry firstGeometry = geometry.getGeometryN(0);
      Geometry secondGeometry = geometry.getGeometryN(1);

      Geometry polygons = polygonize(firstGeometry.getBoundary().union(secondGeometry));
      List<Polygon> polygonsToKeep = new ArrayList<Polygon>();
      int num = polygons.getNumGeometries();
      for(int i=0; i<num; i++) {
        Geometry g = polygons.getGeometryN(i);
        if (polygons.contains(g.getInteriorPoint())) {
          polygonsToKeep.add((Polygon) g);
        }
      }
      Geometry splitGeom = polygonsToKeep.size() > 1 ? geometry.getFactory().createMultiPolygon(toPolygonArray(polygonsToKeep)) : (Geometry) polygonsToKeep.get(0);

      return HttpResponse.ok(writer.write(splitGeom)).contentType(new MediaType(writer.getMediaType()));
    } else {
      return HttpResponse.badRequest("A Geometry Collection with two geometries is required!");
    }
  }

  private MultiPolygon polygonize(Geometry lineString) {
    Polygonizer polygonizer = new Polygonizer();
    int num = lineString.getNumGeometries();
    for(int i=0; i<num; i++) {
      polygonizer.add(lineString.getGeometryN(i));
    }
    Collection<Polygon> polygons = polygonizer.getPolygons();
    return lineString.getFactory().createMultiPolygon(toPolygonArray(polygons));
  }

  private Polygon[] toPolygonArray(Collection<Polygon> polygons) {
    return polygons.toArray(new Polygon[polygons.size()]);
  }

}