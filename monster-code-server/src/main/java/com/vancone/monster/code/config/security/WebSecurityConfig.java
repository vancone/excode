package com.vancone.monster.code.config.security;

import com.vancone.monster.code.service.web.impl.UserServiceImpl;
import com.vancone.monster.code.config.property.CookieConfig;
import com.vancone.monster.code.config.property.SystemConfig;
import com.vancone.monster.code.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

/*
 * Author: Tenton Lien
 * Date: 10/1/2020
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SystemConfig systemConfig;

    @Autowired
    private CustomLoginUrlAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private UserServiceImpl formDetailsService;

    @Autowired
    private CustomSimpleUrlAuthenticationSuccessHandler customSimpleUrlAuthenticationSuccessHandler;

    @Autowired
    private CustomSimpleUrlAuthenticationFailureHandler customSimpleUrlAuthenticationFailureHandler;

    @Autowired
    private CustomSimpleUrlLogoutSuccessHandler customSimpleUrlLogoutSuccessHandler;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();

        List<String> securityIgnoreUrls = systemConfig.getSecurityIgnoreUrls();
        String[] ignores = new String[securityIgnoreUrls.size()];
        http
                .addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
                .and().authenticationProvider(customAuthenticationProvider)
                .authorizeRequests()
                .antMatchers(securityIgnoreUrls.toArray(ignores)).permitAll()
                .antMatchers("/api/admin/**").hasRole(RoleEnum.ADMIN.getName())
                .anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
                .and().formLogin().successHandler(customSimpleUrlAuthenticationSuccessHandler).failureHandler(customSimpleUrlAuthenticationFailureHandler)
                .and().logout().logoutUrl("/api/user/logout").logoutSuccessHandler(customSimpleUrlLogoutSuccessHandler).invalidateHttpSession(true)
                .and().rememberMe().key(CookieConfig.getName()).tokenValiditySeconds(CookieConfig.getInterval()).userDetailsService(formDetailsService)
                .and().csrf().disable()
                .cors();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setMaxAge(3600L);
        configuration.setAllowedOrigins(Collections.singletonList("localhost:8080"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    @Bean
    public CustomAuthenticationProcessingFilter authenticationFilter() throws Exception {
        CustomAuthenticationProcessingFilter authenticationFilter = new CustomAuthenticationProcessingFilter();
        authenticationFilter.setAuthenticationSuccessHandler(customSimpleUrlAuthenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(customSimpleUrlAuthenticationFailureHandler);
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        authenticationFilter.setUserDetailsService(formDetailsService);
        return authenticationFilter;
    }
}
