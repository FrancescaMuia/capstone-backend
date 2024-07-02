package it.epicode.viniEVinili.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Aggiungi le origini consentite
        configuration.addAllowedOrigin("http://localhost:4200");

        // Aggiungi i metodi HTTP consentiti
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("OPTIONS");

        // Aggiungi le intestazioni consentite
        configuration.addAllowedHeader("*");

        // Consenti le credenziali (cookie, autenticazioni)
        configuration.setAllowCredentials(true);

        // Aggiungi le intestazioni esposte
        configuration.addExposedHeader("Authorization");

        // Configura l'origine basata su URL
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }
}
