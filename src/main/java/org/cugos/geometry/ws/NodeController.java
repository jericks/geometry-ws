package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.noding.snapround.GeometryNoder;

import java.util.ArrayList;
import java.util.List;

@Controller("/node")
public class NodeController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "node get", summary = "Node", description = "Node the linestrings from the input geometry.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Number of Decimal Places") @QueryValue("number") int numberOfDecimalPlaces
  ) throws Exception {
    return node(from, to, geometryString, numberOfDecimalPlaces);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "node post", summary = "Node", description = "Node the linestrings from the input geometry.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Number of Decimal Places") @QueryValue("number") int numberOfDecimalPlaces
  ) throws Exception {
     return node(from, to, geometryString, numberOfDecimalPlaces);
  }

  private HttpResponse node(String from, String to, String geometryString, int numberOfDecimalPlaces) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);

    PrecisionModel precisionModel = new PrecisionModel(numberOfDecimalPlaces);
    GeometryNoder noder = new GeometryNoder(precisionModel);
    List<Geometry> geometries = new ArrayList<Geometry>();
    for (int i = 0; i < geometry.getNumGeometries(); i++) {
      geometries.add(geometry.getGeometryN(i));
    }
    List lineStrings = noder.node(geometries);
    Geometry nodedGeometry = geometry.getFactory().createMultiLineString((LineString[]) lineStrings.toArray(new LineString[lineStrings.size()]));

    String content = writer.write(nodedGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}