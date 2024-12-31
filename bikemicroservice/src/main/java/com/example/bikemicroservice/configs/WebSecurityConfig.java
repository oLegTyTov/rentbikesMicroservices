package com.example.bikemicroservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;  // Внедрення вашого фільтру

    // Бін для шифрування паролів
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Конфігурація безпеки для дозволу доступу до всіх ендпоінтів
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Налаштовуємо правила доступу
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()  // Дозволяємо доступ до /login та публічних ендпоінтів
                .anyRequest().permitAll()  // Дозволяємо доступ до всіх запитів без автентифікації
            )
            // Вимикаємо захист від CSRF (потрібно для API або тестування)
            .csrf(csrf -> csrf.disable())
            // Вимикаємо стандартну форму логіну, якщо не використовуєте її
            .formLogin(form -> form.disable())
            // Вимикаємо базову автентифікацію (HTTP Basic)
            .httpBasic(httpBasic -> httpBasic.disable())
            // Вимикаємо логаут, якщо вам це не потрібно
            .logout(logout -> logout.disable())
            // Додаємо свій JWT фільтр до ланцюга фільтрів Spring Security
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);  // Додаємо перед стандартним фільтром
         
        return http.build();
    }
}
