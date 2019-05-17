package org.cugos.geometry.ws;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.triangulate.ConformingDelaunayTriangulationBuilder;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;

@Controller("/delaunayTriangulation")
public class DelaunayTriangulationController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Generate Delaunay Triangulation", description = "Generate a delaunay triangulation of the input geometry")
  public String get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Whether to use the conforming algorithm or not") @QueryValue("conforming") boolean isConforming) throws Exception {
    return delaunayTriangulation(from, to, geometryString, isConforming);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Generate Delaunay Triangulation", description = "Generate a delaunay triangulation of the input geometry")
  public String post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Whether to use the conforming algorithm or not") @QueryValue("conforming") boolean isConforming) throws Exception {
     return delaunayTriangulation(from, to, geometryString, isConforming);
  }

  private String delaunayTriangulation(String from, String to, String geometryString, boolean isConforming) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);

    Geometry outputGeometry;
    if (isConforming) {
      ConformingDelaunayTriangulationBuilder builder = new ConformingDelaunayTriangulationBuilder();
      builder.setSites(geometry);
      outputGeometry = builder.getTriangles(geometry.getFactory());
    } else {
      DelaunayTriangulationBuilder builder = new DelaunayTriangulationBuilder();
      builder.setSites(geometry);
      outputGeometry = builder.getTriangles(geometry.getFactory());
    }

    return writer.write(outputGeometry);
  }

}