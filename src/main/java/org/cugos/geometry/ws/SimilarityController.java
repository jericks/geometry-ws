package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.algorithm.match.AreaSimilarityMeasure;
import org.locationtech.jts.algorithm.match.HausdorffSimilarityMeasure;
import org.locationtech.jts.algorithm.match.SimilarityMeasure;
import org.locationtech.jts.geom.Geometry;

@Controller("/similarity")
public class SimilarityController {
  
  @Get("/{from}")
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "similarity get", summary = "Similarity", description = "Calculate the similarity between two geometries")
  public HttpResponse get(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString,
      @Parameter(description = "Algorithm") @QueryValue("algorithm") String algorithm
  ) throws Exception {
    return similarity(from, geometryString, algorithm);
  }

  @Post("/{from}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(operationId = "similarity post", summary = "Similarity", description = "Calculate the similarity between two geometries")
  public HttpResponse post(
      @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
      @Parameter(description = "Input Geometry") @Body String geometryString,
      @Parameter(description = "Algorithm") @QueryValue("algorithm") String algorithm
  ) throws Exception {
    return similarity(from, geometryString, algorithm);
  }

  private HttpResponse similarity(String from, String geometryString, String algorithm) throws Exception {
    GeometryReader reader = GeometryReaders.find(from);
    Geometry geometry = reader.read(geometryString);
    if (Validator.hasRequiredNumberOfGeometries(geometry, 2)) {
      Geometry firstGeometry = geometry.getGeometryN(0);
      Geometry secondGeometry = geometry.getGeometryN(1);

      SimilarityMeasure similarityMeasure;
      if (algorithm.equalsIgnoreCase("area") || algorithm.equalsIgnoreCase("a")) {
        similarityMeasure = new AreaSimilarityMeasure();
      } else if (algorithm.equalsIgnoreCase("hausdorff") || algorithm.equalsIgnoreCase("h")) {
        similarityMeasure = new HausdorffSimilarityMeasure();
      } else {
        return HttpResponse.badRequest("Unknown similarity measure algorithm!");
      }
      double similarity = similarityMeasure.measure(firstGeometry, secondGeometry);

      return HttpResponse.ok(String.valueOf(similarity));
    } else {
      return HttpResponse.badRequest("A Geometry Collection with two geometries is required!");
    }
  }

}