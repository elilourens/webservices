package Web_Services_Assignment.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// security config for the app
// customers can use the app without logging in
// operators need to login to manage orders and products
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // let anyone access these without login
                .requestMatchers("/", "/customer", "/api/customers/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/products/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/orders").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/orders/customer/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/orders/**").permitAll()

                // swagger docs should be public so we can test
                .requestMatchers("/docs", "/docs/**", "/api-docs", "/api-docs/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // operator stuff needs login
                .requestMatchers("/operator", "/api/orders/**", "/api/products/**").authenticated()

                // everything else needs auth
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/operator", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            // make API return 401 instead of redirecting to login page
            .exceptionHandling(exceptions -> exceptions
                .defaultAuthenticationEntryPointFor(
                    new org.springframework.security.web.authentication.HttpStatusEntryPoint(
                        org.springframework.http.HttpStatus.UNAUTHORIZED
                    ),
                    request -> request.getRequestURI().startsWith("/api/")
                )
            )
            // turn off CSRF for APIs so we can send JSON
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
            );

        return http.build();
    }

    // password encoder for hashing passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // hardcoded user for testing
    // username: operator
    // password: operator123
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails operator = User.builder()
            .username("operator")
            .password(passwordEncoder().encode("operator123"))
            .roles("OPERATOR")
            .build();

        return new InMemoryUserDetailsManager(operator);
    }
}
