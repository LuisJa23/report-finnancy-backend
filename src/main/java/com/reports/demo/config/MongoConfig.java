package com.reports.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import com.reports.demo.enums.ExpenseCategory;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.logging.Logger;

@Configuration
public class MongoConfig {
    
    private static final Logger logger = Logger.getLogger(MongoConfig.class.getName());
    
    @PostConstruct
    public void init() {
        logger.info("Initializing MongoDB configuration...");
    }

    @Bean
    public MongoCustomConversions customConversions() {
        // Comentando los convertidores automáticos que están causando problemas
        // con otros campos String como description y userId
        return new MongoCustomConversions(Arrays.asList(
            // new StringToExpenseCategoryConverter(),
            // new ExpenseCategoryToStringConverter()
        ));
    }

    // Conversor de String a ExpenseCategory para lectura de MongoDB
    @ReadingConverter
    public static class StringToExpenseCategoryConverter implements Converter<String, ExpenseCategory> {
        @Override
        public ExpenseCategory convert(String source) {
            if (source == null || source.trim().isEmpty()) {
                return ExpenseCategory.OTRO;
            }
            
            System.out.println("MongoDB Reading Converter: Converting string '" + source + "' to ExpenseCategory");
            
            // Usar el método fromString del enum que maneja todos los casos
            ExpenseCategory result = ExpenseCategory.fromString(source);
            System.out.println("MongoDB Reading Converter: Result is " + result);
            return result;
        }
    }

    // Conversor de ExpenseCategory a String para escritura en MongoDB
    @WritingConverter
    public static class ExpenseCategoryToStringConverter implements Converter<ExpenseCategory, String> {
        @Override
        public String convert(ExpenseCategory source) {
            if (source == null) {
                return ExpenseCategory.OTRO.getDisplayName();
            }
            
            System.out.println("MongoDB Writing Converter: Converting " + source + " to string '" + source.getDisplayName() + "'");
            return source.getDisplayName();
        }
    }
}
