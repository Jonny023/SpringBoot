package com.example.springbootshiro.configuration;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
public class Knife4jConfiguration {

    @Bean
    public Docket defaultApi1() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //.groupName("2.X版本")
                .globalOperationParameters(getParameters())
                .select()
                //.apis(RequestHandlerSelectors.basePackage("com.example.springbootshiro.controller"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("demo系统")
                .description("简易描述")
                //.termsOfServiceUrl("http://www.xx.com/")
                .contact(new Contact("", "", ""))
                .version("1.0")
                .build();
    }

    /**
     *  全局请求头
     * @return
     */
    private List<Parameter> getParameters() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder()
                .name("Authorization")
                .description("token请求头：44f0d54c-fc11-4743-88a8-f25ea5bd5f4a")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build());
        return parameters;
    }
}
