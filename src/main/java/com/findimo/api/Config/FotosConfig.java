package com.findimo.api.Config;

import com.findimo.api.Service.AlmacenFotos;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FotosConfig implements WebMvcConfigurer {
    private final AlmacenFotos fotos;

    public FotosConfig(AlmacenFotos fotos) {
        this.fotos = fotos;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String ubicacion = fotos.getDirectorio().toUri().toString();
        registry.addResourceHandler(AlmacenFotos.RUTA_WEB + "**")
                .addResourceLocations(ubicacion.endsWith("/") ? ubicacion : ubicacion + "/");
    }
}
