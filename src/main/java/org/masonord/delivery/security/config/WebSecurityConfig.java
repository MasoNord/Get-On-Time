package org.masonord.delivery.security.config;

import lombok.RequiredArgsConstructor;
import org.masonord.delivery.config.ContextConfig;
import org.masonord.delivery.enums.UserRoles;
import org.masonord.delivery.security.filter.CustomAuthenticationFilter;
import org.masonord.delivery.security.filter.CustomAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/",
            "/home",
            "/logout",
            "/api/v1/user/signup",
            "/api/v1/auth/login/**",
            "/api/v1/user/token/refresh/**",
    };

    private final LogoutHandler logoutHandler;
    private final UserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/api/v1/order/**").hasAnyAuthority(UserRoles.COURIER.getValue(), UserRoles.ADMIN.getValue(), UserRoles.CUSTOMER.getValue(), UserRoles.RESTAURANT_WORKER.getValue())
                                .requestMatchers("/api/v1/courier/**").hasAnyAuthority(UserRoles.COURIER.getValue(), UserRoles.ADMIN.getValue())
                                .requestMatchers("/api/v1/customer/**").hasAnyAuthority(UserRoles.CUSTOMER.getValue(), UserRoles.ADMIN.getValue())
                                .requestMatchers("/api/v1/user/**").hasAuthority(UserRoles.ADMIN.getValue())
                                .requestMatchers("/api/v1/completedOrders/**").hasAnyAuthority(UserRoles.COURIER.getValue(), UserRoles.ADMIN.getValue())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                .maximumSessions(1)
                                .maxSessionsPreventsLogin(true)

                )
                .addFilter(new CustomAuthenticationFilter(authenticationManagerBean(userDetailsService, ContextConfig.bCryptPasswordEncoder())))
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())

                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

}
