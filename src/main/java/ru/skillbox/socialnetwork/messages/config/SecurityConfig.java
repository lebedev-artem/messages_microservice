package ru.skillbox.socialnetwork.messages.config;


import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(@NonNull HttpSecurity http) throws Exception {
        http
//                .headers().frameOptions().disable()
//                .and()
//                .addFilterBefore(new CorsFilter(), org.springframework.web.filter.CorsFilter.class)
                .csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("http://localhost:8081/api/v1/dialogs/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        return http.build();
    }
}
