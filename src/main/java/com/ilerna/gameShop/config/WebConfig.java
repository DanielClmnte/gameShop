package com.ilerna.gameShop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración MVC: registra interceptores y recursos estáticos.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads/images}")
    private String uploadDir;

    /**
     * Registra el interceptor de seguridad para /admin/**
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminSecurityInterceptor())
                .addPathPatterns("/admin/**");
        // Inyecta carritoCount en todas las vistas
        registry.addInterceptor(new CartCountInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/uploads/**");
    }

    /**
     * Sirve las imágenes subidas desde uploads/images/ como /uploads/images/**
     * Usa ruta absoluta para que funcione independientemente del directorio de trabajo.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // La BD guarda rutas como /uploads/images/archivo.png
        // El handler coge /uploads/** y lo resuelve desde la carpeta uploads/
        // Resultado: uploads/ + images/archivo.png → correcto
        String absolutePath = "file:" + System.getProperty("user.dir") + "/uploads/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(absolutePath);
    }
}

