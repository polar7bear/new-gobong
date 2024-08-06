package com.sns.gobong.config;

import com.sns.gobong.config.security.jwt.JwtFilter;
import com.sns.gobong.config.security.jwt.TokenAccessDeniedHandler;
import com.sns.gobong.config.security.jwt.TokenAuthenticationEntryPoint;
import com.sns.gobong.config.security.jwt.TokenProvider;
import com.sns.gobong.service.user.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint;
    private final JwtFilter jwtFilter;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)


                /*.sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))*/

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/sign-up", "/users/sign-in", "/oauth/login/**").permitAll()
                        .requestMatchers("/users/sign-out").hasRole("USER")
                        .anyRequest().authenticated()
                )

                /*.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(tokenAccessDeniedHandler)
                        .authenticationEntryPoint(tokenAuthenticationEntryPoint)
                )*/
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService)));
        //.oauth2Client(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
