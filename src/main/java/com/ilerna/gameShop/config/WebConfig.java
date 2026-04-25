package com.ilerna.gameShop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        try {
            // target/classes es el classpath raíz al ejecutar desde IntelliJ
            // Subiendo 2 niveles llegamos a la raíz del proyecto (gameShop/)
            Path classesDir = Paths.get(getClass().getResource("/").toURI());
            Path projectRoot = classesDir.getParent().getParent(); // target/classes -> target -> gameShop/
            String uploadsRoot = "file:" + projectRoot.resolve("uploads").toString() + "/";
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations(uploadsRoot);
        } catch (URISyntaxException e) {
            // Fallback: ruta relativa al working directory
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("file:uploads/");
        }
    }
}

