package com.aerowavegroup.aerowave.config;

import jakarta.annotation.security.PermitAll;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

import static org.springframework.transaction.TransactionDefinition.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {



    @Bean
    public UserDetailsService getUserDetailsServices() {
        return new UserDetailsServiceImpl();
    };

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider getDaoAuthProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getUserDetailsServices());
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());

        return daoAuthenticationProvider;
    }


    protected void configure (AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getDaoAuthProvider());
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authConfig -> {
                    authConfig.requestMatchers("resources/**", "css/**.css","img/**.jpg","img/**.png").permitAll();
                    authConfig.requestMatchers("/register", "/createUser").permitAll();
                    authConfig.requestMatchers("/signin").permitAll();
                    authConfig.requestMatchers("/").permitAll();
                    authConfig.requestMatchers("/promo=**").permitAll();
                    authConfig.requestMatchers("/informations").permitAll();
                    authConfig.requestMatchers("/regulation").permitAll();
                    authConfig.requestMatchers("/availability").permitAll();
                    authConfig.requestMatchers("/search-result").permitAll();
                    authConfig.requestMatchers("/user/**", "/createRoute").authenticated();
                    authConfig.requestMatchers("/pay").authenticated();
                    //authConfig.requestMatchers("/user/**").hasRole("USER");

                })
                .csrf(AbstractHttpConfigurer::disable)

                .logout((logout) ->
                        logout
                                .invalidateHttpSession(false)
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/signin")
                )

                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/")) // Hozzáférés megtagadása esetén átirányítás az index-re
                                .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/")) // Autentikációs hiba esetén is átirányítás az index-re
                )

                .formLogin((formLogin) ->
                formLogin
                        .loginPage("/signin")
                        .usernameParameter("a")
                        .passwordParameter("b")
                        .loginProcessingUrl("/belepes")
                        .defaultSuccessUrl("/")
                        .failureUrl("/signin?error=true")

                        .failureHandler(new AuthenticationFailureHandler() {
                            @Override
                            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                response.sendRedirect("/signin");
                                System.out.println("Rossz belepesi adatok--> nem sikerult a login");

                                String error = exception.getMessage();
                                System.out.println(error);

                            }
                        })


                );

        return http.build();
    }


};

