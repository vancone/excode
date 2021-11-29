package __groupId__.__artifact.id__.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ExCode
 * @since __date__
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    public static final String SWAGGER_SCAN_BASE_PACKAGE = "__groupId__.__artifact.id__";

    public static final String VERSION = "__version__";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .tags(__tags__)
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("__title__")
                .description("__description__")
                .version(VERSION)
                .termsOfServiceUrl("https://www.vancone.com")
                .build();
    }
}