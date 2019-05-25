package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.util.UniqueCoordinateArrayFilter;

@Controller("/coordinates")
public class CoordinatesController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Get Coordinates", description = "Get the coordinates of the geometry.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Only include unique coordinates") @QueryValue("unique") Boolean unique) throws Exception {
    return coordinates(from, to, geometryString, unique == null ? Boolean.FALSE : unique);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Get Coordinates", description = "Get the coordinates of the geometry.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Only include unique coordinates") @QueryValue("unique") Boolean unique) throws Exception {
     return coordinates(from, to, geometryString, unique == null ? Boolean.FALSE : unique);
  }

  private HttpResponse coordinates(String from, String to, String geometryString, boolean isUnique) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);

    Coordinate[] coords = geometry.getCoordinates();
    Geometry outputGeometry = geometry.getFactory().createMultiPointFromCoords(coords);
    if (isUnique)  {
      UniqueCoordinateArrayFilter coordinateFilter = new UniqueCoordinateArrayFilter();
      outputGeometry.apply(coordinateFilter);
      outputGeometry = geometry.getFactory().createMultiPoint(coordinateFilter.getCoordinates());
    }

    String content = writer.write(outputGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}