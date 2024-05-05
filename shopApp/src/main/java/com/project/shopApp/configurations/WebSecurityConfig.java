package com.project.shopApp.configurations;

import com.project.shopApp.filters.JwtTokenFilter;
import com.project.shopApp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    //Pair.of(String.format("%s/products", apiPrefix), "GET"),
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception{
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix),
                                    String.format("%s/healthcheck/**", apiPrefix),
                                    String.format("%s/users/refresh_token", apiPrefix)


                            )
                            .permitAll()
                            .requestMatchers(GET,
                                    String.format("%s/roles**", apiPrefix)).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/categories/**", apiPrefix)).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/products/**", apiPrefix)).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/products/images/*", apiPrefix)).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/orders/**", apiPrefix)).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/order_details/**", apiPrefix)).permitAll()

                            .anyRequest()
                            .authenticated();
                    //.anyRequest().permitAll();
                })
                .csrf(AbstractHttpConfigurer::disable);
        http.securityMatcher(String.valueOf(EndpointRequest.toAnyEndpoint()));

//        http.cors(httpSecurityCorsConfigurer -> {
//            CorsConfiguration configuration = new CorsConfiguration();
//            configuration.setAllowedOrigins(List.of("*"));
//            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//            configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
//            configuration.setExposedHeaders(List.of("x-auth-token"));
//            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//            source.registerCorsConfiguration("/**", configuration);
//            httpSecurityCorsConfigurer.configurationSource(source);
//        });

        return http.build();
    }
}