package com.billing.app.config;

import java.util.Arrays;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Hafiz
 */
@Profile("!prod")
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Value("${context.path}")
	private String contextPath;

	/**
    *
    * @return Docket
    */
   @Bean
   public Docket ebppBillingApi(ServletContext servletContext) {
		Parameter jwtTokenHeader = new ParameterBuilder()
                .name("Authorization")
                .description("'Bearer <token>'")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
	   
       return new Docket(DocumentationType.SWAGGER_2)
				.pathProvider(new RelativePathProvider(servletContext) {
					@Override
					public String getApplicationBasePath() {
						return contextPath;
					}
				})
				.groupName("Billing Webservice")
               .select()
               .apis(RequestHandlerSelectors.basePackage("com.ewx.ebpp.api"))
               .paths(PathSelectors.any())
               .build()
               .globalOperationParameters(Arrays.asList(jwtTokenHeader))
               .apiInfo(getMetadata());
   }

   /**
    *
    * @return ApiInfoBuilder
    */
   private ApiInfo getMetadata() {
       return new ApiInfoBuilder().title("EBPP Billing Webservice").description("REST API with Springboot, Swagger and JPA")
               .termsOfServiceUrl("")
               .contact(new Contact("Developers", "muhammad.alhafiz00@gmail.com", ""))
               .license("Open Source")
               .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
               .version("1.0.0")
               .build();
   }
}
