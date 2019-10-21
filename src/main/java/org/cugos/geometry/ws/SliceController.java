package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.ArrayList;
import java.util.List;

@Controller("/slice")
public class SliceController {
  
  @Get("/{from}/{to}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Slice", description = "Get a subset of geometries using a start and end index.")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Start Index") @QueryValue("start") Integer start,
      @Parameter(description = "End Index")  @QueryValue("end") Integer end
  ) throws Exception {
    return slice(from, to, geometryString, start, end);
  }

  @Post("/{from}/{to}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(summary = "Slice", description = "Get a subset of geometries using a start and end index.")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Output Geometry Format (wkt, geojson, kml, gml2)") String to,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Start Index") @QueryValue("start") Integer start,
      @Parameter(description = "End Index")  @QueryValue("end") Integer end
  ) throws Exception {
     return slice(from, to, geometryString, start, end);
  }

  private HttpResponse slice(String from, String to, String geometryString, Integer start, Integer end) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    GeometryWriter writer = GeometryWriters.find(to);
    Geometry geometry = reader.read(geometryString);
    int len = geometry.getNumGeometries();
    if (start < 0) {
      start = len + start;
    }
    if (Math.abs(start) > len) {
      return HttpResponse.badRequest("Start index can not be more than the number of items!");
    }
    end = (end == null) ? len : end;
    if (end < 0) {
      end = len + end;
    }
    if (Math.abs(end) > len) {
      return HttpResponse.badRequest("End index can not be more than the number of items!");
    }
    List<Geometry> geoms = new ArrayList<Geometry>();
    for(int i = start; i < end; i++) {
      geoms.add(geometry.getGeometryN(i));
    }
    Geometry geom = new GeometryFactory().buildGeometry(geoms);
    String content = writer.write(geom);
    return HttpResponse.ok(content).contentType(new MediaType(writer.getMediaType()));
  }

}