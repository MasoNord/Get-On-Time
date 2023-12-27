package org.masonord.delivery.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
       info = @Info(
               contact = @Contact(
                       name = "Diemid",
                       email = "trortf233@gmail.com",
                       url = ""
               ),
               description = "Open API documentation for backend delivery service",
               title = "Get on Time",
               version = "1.0",
               termsOfService = "Terms of service"
       ),
        servers = {
               @Server(
                       description = "Local ENV",
                       url = "http://localhost:8080"
               ),
               @Server(
                       description = "PROD ENV",
                       url = ""
               )
        },
        security = {
               @SecurityRequirement(
                       name = "bearerAuth"
               )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
