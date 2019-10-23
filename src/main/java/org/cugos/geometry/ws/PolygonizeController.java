package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.polygonize.Polygonizer;

import java.util.Collection;

@Controller("/polygonize")
public class PolygonizeController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "polygonize get", summary = "Polygonize", description = "Creates polygons from lines.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Report") @QueryValue("report") boolean report

  ) throws Exception {
    return polygonize(from, to, geometryString, report);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "polygonize post", summary = "Polygonize", description = "Creates polygons from lines.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Report") @QueryValue("report") boolean report
  ) throws Exception {
     return polygonize(from, to, geometryString, report);
  }

  private HttpResponse polygonize(String from, String to, String geometryString, boolean report) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);

    Polygonizer polygonizer = new Polygonizer();
    int n = geometry.getNumGeometries();
    for (int i = 0; i < n; i++) {
      Geometry g = geometry.getGeometryN(i);
      polygonizer.add(g);
    }
    Geometry geomToWrite;
    Collection polys = polygonizer.getPolygons();
    MultiPolygon multiPolygon = geometry.getFactory().createMultiPolygon((Polygon[]) polys.toArray(new Polygon[polys.size()]));
    if (!report) {
      geomToWrite = multiPolygon;
    } else {
      MultiLineString cutEdges = geometry.getFactory().createMultiLineString(convert(polygonizer.getCutEdges()));
      MultiLineString dangles = geometry.getFactory().createMultiLineString(convert(polygonizer.getDangles()));
      MultiLineString invalidRingLines = geometry.getFactory().createMultiLineString(convert(polygonizer.getInvalidRingLines()));
      geomToWrite = geometry.getFactory().createGeometryCollection(new Geometry[]{
              multiPolygon, cutEdges, dangles, invalidRingLines
      });
    }

    String content = writer.write(geomToWrite);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

  /**
   * Convert a Collection of LinesStrings to an Array of LineStrings
   * @param lines The Collection of LineStrings
   * @return An Array of LineStrings
   */
  private LineString[] convert(Collection<LineString> lines) {
    return lines.toArray(new LineString[lines.size()]);
  }


}