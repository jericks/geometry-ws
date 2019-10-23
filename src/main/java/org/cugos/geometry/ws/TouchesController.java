package org.cugos.geometry.ws;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.locationtech.jts.geom.Geometry;

@Controller("/touches")
public class TouchesController {

    @Get("/{from}")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(operationId = "touches get", summary = "Touches", description = "Whether one geometry touches another")
    public HttpResponse get(
        @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
        @Parameter(description = "Input Geometry") @QueryValue("geom") String geometryString) throws Exception {
        return touches(from, geometryString);
    }

    @Post("/{from}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(operationId = "touches post", summary = "Touches", description = "Whether one geometry touches another")
    public HttpResponse post(
        @Parameter(description = "Input Geometry Format (wkt, geojson, kml, gml2)")  String from,
        @Parameter(description = "Input Geometry") @Body String geometryString) throws Exception {
        return touches(from, geometryString);
    }

    private HttpResponse touches(String from, String geometryString) throws Exception {
        GeometryReader reader = GeometryReaders.find(from);
        Geometry geometry = reader.read(geometryString);
        if (Validator.hasRequiredNumberOfGeometries(geometry, 2)) {
            Geometry firstGeometry = geometry.getGeometryN(0);
            Geometry secondGeometry = geometry.getGeometryN(1);
            boolean result = firstGeometry.touches(secondGeometry);
            return HttpResponse.ok(String.valueOf(result));
        } else {
            return HttpResponse.badRequest("A Geometry Collection with two geometries is required!");
        }
    }

}