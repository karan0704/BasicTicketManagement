package basicticketmanagement.configure;

import basicticketmanagement.model.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays; // Import Arrays for list creation

/**
 * Spring Security configuration for the Basic Ticket Management application.
 * This class defines security policies, authentication providers, and authorization rules.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Enable @PreAuthorize and @PostAuthorize annotations
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     * Defines which requests require authentication and which roles are allowed for specific endpoints.
     *
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity, typically enabled for web apps
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Use the CorsConfigurationSource bean
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**", "/").permitAll() // Allow public access to auth endpoints and home
                        .requestMatchers("/customers").hasRole(UserRole.ENGINEER.name()) // Only engineers can register customers
                        .requestMatchers("/tickets").hasRole(UserRole.CUSTOMER.name()) // Only customers can create tickets
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Use sessions
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login")  // REST login endpoint
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(200);
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(401);
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(200);
                        })
                        .permitAll());

        // .formLogin(AbstractHttpConfigurer::disable) // Disable default form login
                //.httpBasic(AbstractHttpConfigurer::disable); // Disable default HTTP Basic authentication

        return http.build();
    }

    /**
     * Provides a BCryptPasswordEncoder bean for password hashing.
     *
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the AuthenticationManager.
     * Uses DaoAuthenticationProvider with a custom UserDetailsService and password encoder.
     *
     * @param userDetailsService The custom UserDetailsService.
     * @param passwordEncoder The password encoder.
     * @return An AuthenticationManager instance.
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    /**
     * Configures CORS settings as a CorsConfigurationSource bean.
     * This bean is directly used by Spring Security's CORS configuration.
     *
     * @return A CorsConfigurationSource instance.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // IMPORTANT: For local file system access, 'null' origin is required.
        // In production, replace 'null' with your actual frontend domain(s).
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "null")); // Added "null" for file:// access
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies for sessions)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply this CORS config to all paths
        return source;
    }
}