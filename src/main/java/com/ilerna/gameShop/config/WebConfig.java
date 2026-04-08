package com.ilerna.gameShop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración MVC: registra interceptores y recursos estáticos.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Registra el interceptor de seguridad para /admin/**
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminSecurityInterceptor())
                .addPathPatterns("/admin/**");
    }

    /**
     * Sirve las imágenes subidas desde uploads/images/ como /uploads/**
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}

