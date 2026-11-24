//OAuth 2 and JWT token
package com.example.taskManager;

import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails john = User.builder()
                .username("john")
                .password(encoder.encode("john123"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(john, admin);
    }

    @Bean
    AuthenticationManager authenticationManager(
            UserDetailsService users,
            PasswordEncoder encoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(users);

        provider.setPasswordEncoder(encoder);

        return new ProviderManager(provider);
    }

    @Bean
    SecretKey jwtKey(@Value("${app.jwt.secret}") String secret) {
        byte[] bytes = Base64.getDecoder().decode(secret);

        if (bytes.length < 32) {
            throw new IllegalArgumentException(
                    "JWT secret must be at least 32 bytes");
        }

        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    @Bean
    JwtEncoder jwtEncoder(SecretKey key) {
        return NimbusJwtEncoder.withSecretKey(key)
                .algorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    JwtDecoder jwtDecoder(SecretKey key) {
        NimbusJwtDecoder decoder =
                NimbusJwtDecoder.withSecretKey(key)
                        .macAlgorithm(MacAlgorithm.HS256)
                        .build();

        decoder.setJwtValidator(
                JwtValidators.createDefaultWithIssuer("task-manager")
        );

        return decoder;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        JwtGrantedAuthoritiesConverter roles =
                new JwtGrantedAuthoritiesConverter();

        roles.setAuthoritiesClaimName("roles");
        roles.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(roles);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/tasks", "/tasks/**")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/tasks")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/tasks/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/tasks/**")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(converter)));

        return http.build();
    }
}











///CSRF token authentication and Authorization
//package com.example.taskManager;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories
//                .createDelegatingPasswordEncoder();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(
//            PasswordEncoder encoder) {
//
//        UserDetails john = User.builder()
//                .username("john")
//                .password(encoder.encode("john123"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(encoder.encode("admin123"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(john, admin);
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(
//            HttpSecurity http) throws Exception {
//
//        http
//                .csrf(Customizer.withDefaults())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.GET, "/tasks", "/tasks/{id}")
//                        .hasAnyRole("USER", "ADMIN")
//
//                        .requestMatchers(HttpMethod.POST, "/tasks")
//                        .hasRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.PUT, "/tasks/{id}")
//                        .hasRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.DELETE, "/tasks/{id}")
//                        .hasRole("ADMIN")
//
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }
//}