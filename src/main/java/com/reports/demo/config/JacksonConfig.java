package com.reports.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Configuración de Jackson para manejo correcto de fechas y zona horaria
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Registrar módulo de tiempo de Java 8+
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        // Configurar formato personalizado para LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        javaTimeModule.addSerializer(new LocalDateTimeSerializer(formatter));
        
        mapper.registerModule(javaTimeModule);
        
        // Deshabilitar escritura de fechas como timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Establecer zona horaria de Colombia
        mapper.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
        
        return mapper;
    }
}
