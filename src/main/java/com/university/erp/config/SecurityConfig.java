package com.university.erp.config;

import com.university.erp.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Teacher - Full CRUD access
        UserDetails teacher = User.builder()
                .username("teacher")
                .password(passwordEncoder().encode("password123"))
                .roles("TEACHER")
                .build();

        // Student - View only + edit own profile
        UserDetails student = User.builder()
                .username("student")
                .password(passwordEncoder().encode("password123"))
                .roles("STUDENT")
                .build();

        return new InMemoryUserDetailsManager(teacher, student);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public pages
                        .requestMatchers("/", "/home", "/login", "/css/**", "/js/**").permitAll()

                        // Student endpoints - Both roles can view, only TEACHER can CRUD
                        .requestMatchers(HttpMethod.GET, "/students").hasAnyRole("TEACHER", "STUDENT")
                        .requestMatchers("/students/new", "/students/edit/**", "/students/update", "/students/delete/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.POST, "/students").hasRole("TEACHER")

                        // Teacher endpoints - Both roles can view, only TEACHER can CRUD
                        .requestMatchers(HttpMethod.GET, "/teachers").hasAnyRole("TEACHER", "STUDENT")
                        .requestMatchers("/teachers/new", "/teachers/edit/**", "/teachers/update", "/teachers/delete/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.POST, "/teachers").hasRole("TEACHER")

                        // Department endpoints - Both roles can view, only TEACHER can CRUD
                        .requestMatchers(HttpMethod.GET, "/departments").hasAnyRole("TEACHER", "STUDENT")
                        .requestMatchers("/departments/new", "/departments/edit/**", "/departments/update", "/departments/delete/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.POST, "/departments").hasRole("TEACHER")

                        // Course endpoints - Both roles can view, only TEACHER can CRUD
                        .requestMatchers(HttpMethod.GET, "/courses").hasAnyRole("TEACHER", "STUDENT")
                        .requestMatchers("/courses/new", "/courses/edit/**", "/courses/update", "/courses/delete/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.POST, "/courses").hasRole("TEACHER")

                        // Student's own profile - STUDENT can edit their own
                        .requestMatchers("/students/profile/**").hasRole("STUDENT")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .permitAll()
                        .defaultSuccessUrl("/", true) // Redirect after login
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .userDetailsService(customUserDetailsService);

        return http.build();
    }
}