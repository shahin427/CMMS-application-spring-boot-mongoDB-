package org.sayar.net.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final Contact DEFAULT_CONTACT = new Contact("sayar","farzad-g.com", "f.gilaki9876@gmail.com");
    private static Collection<VendorExtension> vendorExtensions= new ArrayList<>();
    public static final ApiInfo DEFAUL_API_INFO = new ApiInfo(
            "net-swagger",
            "It's swagger",
            "sayar.co",
            "sayar-termsOfServicesUrl",
            DEFAULT_CONTACT,
            "licens",

            "license-url",
            vendorExtensions);


    @Bean
    public Docket api(){
        Set<String> stringSet = new HashSet<>();
        stringSet.add("application/json");
//        stringSet.add("application/xml");
        Tag tag = new Tag("country","it's tag description");
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(DEFAUL_API_INFO).consumes(stringSet);
    }
}
