package org.cugos.geometry.ws;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
    info = @Info(
        title = "Geometry Web Services",
        version = "0.1.0",
        description = "Geometry Web Services",
        license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT"),
        contact = @Contact(url = "https://github.com/jericks", name = "Jared Erickson")
    )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }

}