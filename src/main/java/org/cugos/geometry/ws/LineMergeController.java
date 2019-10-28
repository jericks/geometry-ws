package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.dissolve.LineDissolver;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.operation.linemerge.LineMerger;

import java.util.Collection;

@Controller("/lineMerge")
public class LineMergeController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "line merge get", summary = "Line Merge", description = "Merge lines of the input geometry together.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString) throws Exception {
    return mergeLines(from, to, geometryString);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "line merge post", summary = "Line Merge", description = "Merge lines of the input geometry together.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString) throws Exception {
     return mergeLines(from, to, geometryString);
  }

  private HttpResponse mergeLines(String from, String to, String geometryString) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    LineMerger lineMerger = new LineMerger();
    int n = geometry.getNumGeometries();
    for (int i = 0; i < n; i++) {
      Geometry g = geometry.getGeometryN(i);
      lineMerger.add(g);
    }
    Collection lines = lineMerger.getMergedLineStrings();
    MultiLineString multiLineString = geometry.getFactory().createMultiLineString((LineString[]) lines.toArray(new LineString[lines.size()]));
    String content = writer.write(multiLineString);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}