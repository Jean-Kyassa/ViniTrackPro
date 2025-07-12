// package com.maison.vinitrackpro.security;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// import java.util.Arrays;

// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class SecurityConfig {

//     private final UserDetailsServiceImpl userDetailsService;
//     private final AuthEntryPointJwt unauthorizedHandler;
//     private final JwtUtils jwtUtils;

//     public SecurityConfig(UserDetailsServiceImpl userDetailsService,
//                          AuthEntryPointJwt unauthorizedHandler,
//                          JwtUtils jwtUtils) {
//         this.userDetailsService = userDetailsService;
//         this.unauthorizedHandler = unauthorizedHandler;
//         this.jwtUtils = jwtUtils;
//     }

//     @Bean
//     public AuthTokenFilter authenticationJwtTokenFilter() {
//         return new AuthTokenFilter(jwtUtils, userDetailsService);
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public DaoAuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//         authProvider.setUserDetailsService(userDetailsService);
//         authProvider.setPasswordEncoder(passwordEncoder());
//         return authProvider;
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//         return authConfig.getAuthenticationManager();
//     }

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration configuration = new CorsConfiguration();
//         configuration.setAllowedOriginPatterns(Arrays.asList("*"));
//         configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         configuration.setAllowedHeaders(Arrays.asList("*"));
//         configuration.setAllowCredentials(true);
        
//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", configuration);
//         return source;
//     }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//             .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//             .csrf(csrf -> csrf.disable())
//             .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/api/auth/**").permitAll()
//                 .requestMatchers("/api/test/**").permitAll()
//                 .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                 .anyRequest().authenticated()
//             );

//         http.authenticationProvider(authenticationProvider());
//         http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }
// }

// package com.maison.vinitrackpro.security;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class SecurityConfig {

//     private final UserDetailsServiceImpl userDetailsService;
//     private final AuthEntryPointJwt unauthorizedHandler;
//     private final JwtUtils jwtUtils;

//     public SecurityConfig(UserDetailsServiceImpl userDetailsService,
//                          AuthEntryPointJwt unauthorizedHandler,
//                          JwtUtils jwtUtils) {
//         this.userDetailsService = userDetailsService;
//         this.unauthorizedHandler = unauthorizedHandler;
//         this.jwtUtils = jwtUtils;
//     }

//     @Bean
//     public AuthTokenFilter authenticationJwtTokenFilter() {
//         return new AuthTokenFilter(jwtUtils, userDetailsService);
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public DaoAuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//         authProvider.setUserDetailsService(userDetailsService);
//         authProvider.setPasswordEncoder(passwordEncoder());
//         return authProvider;
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//         return authConfig.getAuthenticationManager();
//     }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//             .cors(cors -> cors.disable()) // CORS is now handled by CorsConfig
//             .csrf(csrf -> csrf.disable())
//             .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/api/auth/**").permitAll()
//                 .requestMatchers("/api/test/**").permitAll()
//                 .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                 .anyRequest().authenticated()
//             );

//         http.authenticationProvider(authenticationProvider());
//         http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }
// }

// package com.maison.vinitrackpro.security;

// import java.util.Arrays;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class SecurityConfig {

//     private final UserDetailsServiceImpl userDetailsService;
//     private final AuthEntryPointJwt unauthorizedHandler;
//     private final JwtUtils jwtUtils;

//     public SecurityConfig(
//         UserDetailsServiceImpl userDetailsService,
//         AuthEntryPointJwt unauthorizedHandler,
//         JwtUtils jwtUtils
//     ) {
//         this.userDetailsService = userDetailsService;
//         this.unauthorizedHandler = unauthorizedHandler;
//         this.jwtUtils = jwtUtils;
//     }

//     @Bean
//     public AuthTokenFilter authenticationJwtTokenFilter() {
//         return new AuthTokenFilter(jwtUtils, userDetailsService);
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public DaoAuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//         authProvider.setUserDetailsService(userDetailsService);
//         authProvider.setPasswordEncoder(passwordEncoder());
//         return authProvider;
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//         return authConfig.getAuthenticationManager();
//     }

// //     @Bean
// //    public CorsConfigurationSource corsConfigurationSource() {
// //     CorsConfiguration configuration = new CorsConfiguration();
// //     configuration.setAllowedOriginPatterns(Arrays.asList("*"));
// //     configuration.setAllowedOrigins(Arrays.asList(
// //         "http://localhost:3000",
// //         "http://localhost:3001", 
// //         "http://localhost:5173",
// //         "http://127.0.0.1:3000",
// //         "http://127.0.0.1:3001",
// //         "http://127.0.0.1:5173"
// //     ));
// //     configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
// //     configuration.setAllowedHeaders(Arrays.asList("*"));
// //     configuration.setAllowCredentials(true);
// //     configuration.setMaxAge(3600L);
    
// //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
// //     source.registerCorsConfiguration("/**", configuration);
// //     return source;
// // }
// @Bean
// public CorsConfigurationSource corsConfigurationSource() {
//     CorsConfiguration configuration = new CorsConfiguration();
//     configuration.setAllowedOriginPatterns(Arrays.asList("*"));
//     configuration.setAllowedOrigins(Arrays.asList(
//         "http://localhost:3000",
//         "http://localhost:3001", 
//         "http://localhost:5173",
//         "http://127.0.0.1:3000",
//         "http://127.0.0.1:3001",
//         "http://127.0.0.1:5173"
//     ));
//     configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
//     configuration.setAllowedHeaders(Arrays.asList("*"));
//     configuration.setAllowCredentials(true);
//     configuration.setMaxAge(3600L);
    
//     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//     source.registerCorsConfiguration("/**", configuration);
//     return source;
// }



    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http
    //         .csrf(csrf -> csrf.disable())  // Disable CSRF (not needed for stateless APIs)
    //         .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
    //         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/api/auth/**").permitAll()
    //             .requestMatchers("/api/test/**").permitAll()
    //             .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
    //             .anyRequest().authenticated()
    //         );

    //     http.authenticationProvider(authenticationProvider());
    //     http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    //     return http.build();
    // }
//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//     http
//         .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//         .csrf(csrf -> csrf.disable())
//         .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//         .authorizeHttpRequests(auth -> auth
//             .requestMatchers("/api/auth/**").permitAll()
//             .requestMatchers("/api/test/**").permitAll()
//             .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow preflight requests
//             .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//             .anyRequest().authenticated()
//         );

//     http.authenticationProvider(authenticationProvider());
//     http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

//     return http.build();
//    }

// @Bean
// public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//     http
//         .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//         .csrf(csrf -> csrf.disable())
//         .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//         .authorizeHttpRequests(auth -> auth
//             .requestMatchers("/api/auth/**").permitAll()
//             .requestMatchers("/api/test/**").permitAll()
//             .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow preflight requests
//             .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//             .anyRequest().authenticated()
//         );

//     http.authenticationProvider(authenticationProvider());
//     http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

//     return http.build();
// }
// }

package com.maison.vinitrackpro.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final JwtUtils jwtUtils;

    public SecurityConfig(
        UserDetailsServiceImpl userDetailsService,
        AuthEntryPointJwt unauthorizedHandler,
        JwtUtils jwtUtils
    ) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Use allowedOriginPatterns instead of allowedOrigins when allowCredentials is true
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:3000",
            "http://localhost:3001", 
            "http://localhost:5173",
            "http://127.0.0.1:3000",
            "http://127.0.0.1:3001",
            "http://127.0.0.1:5173"
        ));
        
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/test/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            );
        
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
