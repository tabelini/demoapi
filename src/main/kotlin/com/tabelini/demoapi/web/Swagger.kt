package com.tabelini.demoapi.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
    val apiInfo = ApiInfo(
            "Demo API",
            "API for demo purposes",
            "1",
            "",
            Contact("Guilherme Tabelini", "https://www.linkedin.com/in/tabelini/",
                    "guilhermbh@gmail.com"),
            "reserved", "reserved"
    )

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.tabelini.demoapi.web"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo)
    }
}
