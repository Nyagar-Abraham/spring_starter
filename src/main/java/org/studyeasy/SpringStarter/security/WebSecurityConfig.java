package org.studyeasy.SpringStarter.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.studyeasy.SpringStarter.utils.constants.Privilages;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] PUBLIC_ROUTES = {
        "/", "/register", "/login", "/forgot-password","/reset-password","/db-console/**", 
        "/css/**", "/fonts/**", "/images/**", "/js/**"
    };

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("Allowed public routes: " + Arrays.toString(PUBLIC_ROUTES));

        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for development
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_ROUTES).permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")  // ✅ Admin only
                .requestMatchers("/editor/**").hasAnyRole("ADMIN", "EDITOR")  // ✅ Editor & Admin
                .requestMatchers("/test").hasAuthority(Privilages.ACCESS_TO_ADMIN_PANEL.getPrivilage()) // ✅ Custom authority
                .anyRequest().authenticated()  // ✅ Everything else requires authentication
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Allow H2 Console
            .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true) // ✅ Redirect after login
                .failureUrl("/login?error")
                .permitAll()
            )
            .rememberMe(rememberMe -> rememberMe
                .key("unique-and-secret")  // Set a secret key for token generation
                .rememberMeParameter("remember-me")  // ✅ Attribute for the form
                .tokenValiditySeconds(7 * 24 * 60 * 60)  // ✅ 7 days validity
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }
}
