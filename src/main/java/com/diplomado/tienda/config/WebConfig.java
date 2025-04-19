package com.diplomado.tienda.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        // Registra un manejador de recursos para las imágenes de productos
        // El patrón "/images/productosImg/**" se usará para capturar todas las solicitudes a esa URL
        registry.addResourceHandler("/images/productosImg/**")
                // Indica la ubicación real donde se encuentran las imágenes en el sistema de archivos local
                .addResourceLocations("file:src/main/resources/static/images/productosImg/");

    }
}
