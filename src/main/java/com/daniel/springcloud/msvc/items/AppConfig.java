package com.daniel.springcloud.msvc.items;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

/**
 * AppConfig
 * Clase de configuracion de parametros para circuit braker
 * Para este caso se deja configura para el caso "items"
 * Para el caso de otro circuit braker que se dispare se deja la configuracion por defecto
 */
@Configuration
public class AppConfig {

    /**
     * Configuracion de parametros para el caso "items" de circuit braker usado en la case "ItemsController.java"
     * @return
     */
    @Bean
    Customizer<Resilience4JCircuitBreakerFactory> customizerCircuitBraker(){
        return (factory) -> factory.configureDefault(id -> {
            if (id.equals("items")) {
                return new Resilience4JConfigBuilder(id).circuitBreakerConfig(CircuitBreakerConfig
                    //.ofDefaults()
                    .custom()
                    .slidingWindowSize(10) //Muestreo de solicitudes: Por defecto son 100, se configura para 10
                    .failureRateThreshold(50)//Porcentaje de fallo: Por defecto son 50%, se deja en 50%
                    .waitDurationInOpenState(Duration.ofSeconds(10L))//Duracion en segundos de espera en estado abierto: Por defecto son 60s, se configura a 10s
                    .permittedNumberOfCallsInHalfOpenState(5)//Numero de llamadas permitidas en estado semi abierto: Por defecto son 10, se configuran 5
                    .build()
                )
                .build();
            }            
            return new Resilience4JConfigBuilder(id).circuitBreakerConfig(CircuitBreakerConfig.ofDefaults()).build();
        });
    }
    
}
