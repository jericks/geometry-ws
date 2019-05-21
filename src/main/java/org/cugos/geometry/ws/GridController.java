package org.cugos.geometry.ws;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

@Controller("/grid")
public class GridController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Calculate a Grid", description = "Calculate a grid around the input geometry.")
  public String get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Number of columns") @QueryValue("columns")int columns,
      @Parameter(description = "Number of rows") @QueryValue("rows")int rows,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString) throws Exception {
    return grid(from, to, geometryString, columns, rows);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Calculate a Grid", description = "Calculate a grid around the input geometry.")
  public String post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Number of columns") @QueryValue("columns") int columns,
      @Parameter(description = "Number of rows") @QueryValue("rows") int rows,
      @Parameter(description = "Input Geometry") @Body String geometryString) throws Exception {
     return grid(from, to, geometryString, columns, rows);
  }

  private String grid(String from, String to, String geometryString, int columns, int rows) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);

    // Get the input Geometry's Envelope
    Envelope env = geometry.getEnvelopeInternal();

    // Extract the min X and Y
    double minX = env.getMinX();
    double minY = env.getMinY();

    // Calculate hte envelop's width and height
    double w = env.getWidth();
    double h = env.getHeight();

    // Calculate the cell width and height
    double cellWidth = w / columns;
    double cellHeight = h / rows;

    // Create the cells
    GeometryFactory geometryFactory = new GeometryFactory();
    Geometry[] geoms = new Geometry[rows * columns];
    int i = 0;
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < columns; c++) {
        double x = minX + (c * cellWidth);
        double y = minY + (r * cellHeight);
        Envelope e = new Envelope(x, x + cellWidth, y, y + cellHeight);
        geoms[i] = geometryFactory.toGeometry(e);
        i++;
      }
    }

    // Create the Geometry grid
    Geometry grid = geometryFactory.createGeometryCollection(geoms);

    return writer.write(grid);
  }

}