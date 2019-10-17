package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.precision.GeometryPrecisionReducer;

@Controller("/reducePrecision")
public class ReducePrecisionController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Reduce Precision", description = "Reduce the precision of the coordinates of a Geometry")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Precision Type") @QueryValue("type") String type,
      @Parameter(description = "Precision Scale") @QueryValue("scale") double scale,
      @Parameter(description = "Pointwise") @QueryValue("pointwise") boolean pointwise,
      @Parameter(description = "Remove Collapsed") @QueryValue("removeCollapsed") boolean removeCollapsed
  ) throws Exception {
    return reducePrecision(from, to, geometryString, type, scale, pointwise, removeCollapsed);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Reduce Precision", description = "Reduce the precision of the coordinates of a Geometry")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Precision Type") @QueryValue("type") String type,
      @Parameter(description = "Precision Scale") @QueryValue("scale") double scale,
      @Parameter(description = "Pointwise") @QueryValue("pointwise") boolean pointwise,
      @Parameter(description = "Remove Collapsed") @QueryValue("removeCollapsed") boolean removeCollapsed
  ) throws Exception {
     return reducePrecision(from, to, geometryString, type, scale, pointwise, removeCollapsed);
  }

  private HttpResponse reducePrecision(String from, String to, String geometryString,
    String type, double scale, boolean pointwise, boolean removeCollapsed
  ) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    PrecisionModel precisionModel;
    if (type.equalsIgnoreCase("fixed")) {
      precisionModel = new PrecisionModel(scale);
    } else if (type.equalsIgnoreCase("floating")) {
      precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
    } else if (type.equalsIgnoreCase("floating_single")) {
      precisionModel = new PrecisionModel(PrecisionModel.FLOATING_SINGLE);
    } else {
      return HttpResponse.badRequest("Unsupported Precision Model Type: '" + type + "'!");
    }
    GeometryPrecisionReducer reducer = new GeometryPrecisionReducer(precisionModel);
    reducer.setPointwise(pointwise);
    reducer.setRemoveCollapsedComponents(removeCollapsed);
    Geometry reducedGeometry = reducer.reduce(geometry);
    String content = writer.write(reducedGeometry);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}