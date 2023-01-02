package com.example.bec.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import java.io.IOException;

@Configuration
public class ThymeleafConfiguration {
    private final PropertiesCustom propertiesCustom;

    public ThymeleafConfiguration(PropertiesCustom propertiesCustom) {
        this.propertiesCustom = propertiesCustom;
    }

    @Bean
    public SpringTemplateEngine templateEngine() throws IOException {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
        return templateEngine;
    }

    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver() throws IOException {
        SpringResourceTemplateResolver templateResolver
                = new SpringResourceTemplateResolver();
        templateResolver.setPrefix(propertiesCustom.getProperties().getProperty("url.config.template.email"));
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        return templateResolver;
    }
}
