package com.akuma.tech.auth.config;


import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.akuma.tech.auth.repository.UserRepository;
import com.akuma.tech.auth.service.JwtService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
	public SecurityFilterChain securityFilterChain(
		HttpSecurity http,
		JwtService jwtService,
		UserRepository userRepository
	) throws Exception {
		return http
			.csrf(csrf -> csrf.disable())
    		.cors(cors -> {})
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/auth/users").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/auth/delete/**").hasRole("ADMIN")
				.anyRequest().permitAll())
			.addFilterBefore(
				new JwtAuthenticationFilter(jwtService, userRepository),
				UsernamePasswordAuthenticationFilter.class
			)
			.build();
	}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
    	CorsConfiguration configuration = new CorsConfiguration();
    	configuration.setAllowedOrigins(List.of(
    		"http://localhost:5173",
    		"http://127.0.0.1:5173",
    		"http://localhost:4173",
    		"http://127.0.0.1:4173"
    	));
    	configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    	configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    	configuration.setAllowCredentials(true);

    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	source.registerCorsConfiguration("/**", configuration);
    	return source;
    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}



}