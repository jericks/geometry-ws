package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.*;

@Controller("/closeLineString")
public class CloseLineStringController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "close linestring get", summary = "Close LineString", description = "Close a LineString to create a LinearRing")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString) throws Exception {
    return closeLineString(from, to, geometryString);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "close linestring post", summary = "Close LineString", description = "Close a LineString to create a LinearRing")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString) throws Exception {
     return closeLineString(from, to, geometryString);
  }

  private HttpResponse closeLineString(String from, String to, String geometryString) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);

    if (!(geometry instanceof LineString)) {
      return HttpResponse.badRequest("Input geometry must be a LineString!");
    }
    LineString lineString = (LineString)geometry;
    if (lineString.getCoordinates().length < 3) {
      return HttpResponse.badRequest("You need at least three points to close a LineString!");
    }
    GeometryFactory geometryFactory = new GeometryFactory();
    LinearRing linearRing;
    if (lineString.isClosed()) {
      linearRing = geometryFactory.createLinearRing(lineString.getCoordinates());
    } else {
      Coordinate[] coords = lineString.getCoordinates();
      Coordinate[] closedCoords = new Coordinate[coords.length + 1];
      CoordinateArrays.copyDeep(coords, 0, closedCoords, 0, coords.length);
      closedCoords[coords.length] = coords[0];
      linearRing = geometryFactory.createLinearRing(closedCoords);
    }
    String content = writer.write(linearRing);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}