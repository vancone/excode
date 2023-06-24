package ${template.properties.project.groupId}.${template.properties.project.artifactId}.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author ExCode
 * @since ${date}
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    public static final String SWAGGER_SCAN_BASE_PACKAGE = "${template.properties.project.groupId}.${template.properties.project.artifactId}";

    public static final String VERSION = "${project.version}";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("${swagger.title}")
                .description("${swagger.description}")
                .version(VERSION)
                .termsOfServiceUrl("https://www.vancone.com")
                .build();
    }
}