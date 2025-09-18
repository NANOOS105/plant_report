package project.plant_report.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Plant Report API")
                        .description("식물 물주기 기록 관리 API")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Plant Report Team")
                                .email("contact@plantreport.com")));
    }
}
