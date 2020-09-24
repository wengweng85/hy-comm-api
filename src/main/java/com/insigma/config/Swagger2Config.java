package com.insigma.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Component			  // ���Ӷ�ҳ�涨λ
@Configuration         // ��Spring�����ظ�������
@EnableWebMvc       // ����Mvc����springboot�����Ҫ����ע��@EnableWebMvc
@EnableSwagger2     // ����Swagger2
@ComponentScan("com.insigma.mvc.controller")
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        List<Parameter> pars = new ArrayList<>();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
                //ɨ��ָ�����е�swaggerע��
                //ɨ��������ע���api�������ַ�ʽ�����
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("����ƽ̨ RESTful APIs")
                .description("����ƽ̨ RESTful ���Ľӿ��ĵ���������ϸ������ļ�����ǰ��˵Ĺ�ͨ�ɱ���ͬʱȷ���������ĵ����ָ߶�һ�£�����ļ���ά���ĵ���ʱ�䡣")
                .version("1.0.0")
                .build();
    }
}
