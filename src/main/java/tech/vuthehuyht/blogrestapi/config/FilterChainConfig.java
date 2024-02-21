package tech.vuthehuyht.blogrestapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tech.vuthehuyht.blogrestapi.exceptions.CustomAccessDeniedHandler;
import tech.vuthehuyht.blogrestapi.filter.CustomAuthenticationProvider;
import tech.vuthehuyht.blogrestapi.filter.JwtAuthenticationInternalFilter;
import tech.vuthehuyht.blogrestapi.filter.JwtFilter;
import tech.vuthehuyht.blogrestapi.security.JwtService;
import tech.vuthehuyht.blogrestapi.services.CustomUserDetailService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class FilterChainConfig {
    private final CustomUserDetailService customUserDetailService;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    public void userCustomAuthenticationProvider(AuthenticationManagerBuilder managerBuilder) {
        managerBuilder.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder managerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(customUserDetailService).passwordEncoder(bCryptPasswordEncoder());
        AuthenticationManager authenticationManager = managerBuilder.build();

        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/demo").permitAll()
                        .requestMatchers("/api/v1/user/register").permitAll()
                        .requestMatchers("/api/v1/auth").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationManager(authenticationManager)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handling -> handling.accessDeniedHandler(new CustomAccessDeniedHandler()))
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint(
                        (((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                ))
                .addFilterBefore(new JwtFilter(objectMapper, jwtService, jwtConfig, customUserDetailService, authenticationManager),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthenticationInternalFilter(jwtService, objectMapper, jwtConfig),
                        UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
