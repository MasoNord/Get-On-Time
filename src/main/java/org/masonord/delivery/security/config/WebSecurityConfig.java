package org.masonord.delivery.config;

import lombok.RequiredArgsConstructor;
import org.masonord.delivery.enums.UserRoles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
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
            "/login",
//            "/api/**"
    };

    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
//                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.configure(http))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/api/v1/order/**").hasAnyRole(UserRoles.COURIER.getValue(), UserRoles.ADMIN.getValue(), UserRoles.CUSTOMER.getValue(), UserRoles.RESTAURANT_WORKER.getValue())
                                .requestMatchers("/api/v1/courier/**").hasAnyRole(UserRoles.COURIER.getValue(), UserRoles.ADMIN.getValue())
                                .requestMatchers("/api/v1/customer/**").hasAnyRole(UserRoles.CUSTOMER.getValue(), UserRoles.ADMIN.getValue())
                                .requestMatchers("/api/v1/user/**").hasRole(UserRoles.ADMIN.getValue())
                                .requestMatchers("/api/v1/completedOrders/**").hasAnyRole(UserRoles.COURIER.getValue(), UserRoles.ADMIN.getValue())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                .maximumSessions(1)
                                .maxSessionsPreventsLogin(true)

                )
                .authenticationProvider(authenticationProvider)
                .logout(logout ->
                        logout.logoutUrl("api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())

                );
        return http.build();
    }


//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("password")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }



//    @Order(1)
//    @Configuration
//    public static class HttpSecurityConfiguration {
//        @Bean
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            http
//                    .authorizeHttpRequests((request) ->
//                        request
//                                .requestMatchers("/api/**", "/api/v1/user/signup").permitAll()
//                                .anyRequest().authenticated()
//                    )
//                    .exceptionHandling((exception) ->
//                        exception
//                                .authenticationEntryPoint((res, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                    )
//                    .sessionManagement((session) ->
//                        session
//                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//                                .invalidSessionUrl("/invalidSession.htm")
//                                .maximumSessions(1)
//                                .maxSessionsPreventsLogin(true)
//                    );
//
//            return http.build();
//        }
//
//        @Bean
//        public PasswordEncoder passwordEncoder() {
//            return new BCryptPasswordEncoder();
//        }
//
//        @Bean
//        CustomUserDetailsService customUserDetailsService() {
//            return new CustomUserDetailsService();
//        }
//    }
//
//    @Order(2)
//    @Configuration
//    public static class LoginWebSecurityConfiguration {
//
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            http
//                    .
//        }
//    }
//
}
