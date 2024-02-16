package tech.vuthehuyht.blogrestapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import tech.vuthehuyht.blogrestapi.config.JwtConfig;
import tech.vuthehuyht.blogrestapi.dto.request.AuthenticationRequest;
import tech.vuthehuyht.blogrestapi.dto.response.AuthenticationResponse;
import tech.vuthehuyht.blogrestapi.security.CustomUserDetail;
import tech.vuthehuyht.blogrestapi.security.JwtService;
import tech.vuthehuyht.blogrestapi.services.CustomUserDetailService;
import tech.vuthehuyht.blogrestapi.utils.CustomMessageExceptionUtil;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtFilter extends AbstractAuthenticationProcessingFilter {
    private final JwtService jwtService;

    private final ObjectMapper objectMapper;

    private final CustomUserDetailService customUserDetailService;

    public JwtFilter(ObjectMapper objectMapper, JwtService jwtService, JwtConfig jwtConfig, CustomUserDetailService customUserDetailService, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(jwtConfig.getUrl(), "POST"));
        setAuthenticationManager(authenticationManager);
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        log.info("Attempt Authentication");

        AuthenticationRequest authenticationRequest = objectMapper
                .readValue(request.getInputStream(), AuthenticationRequest.class);
        customUserDetailService.saveUserAttemptAuthentication(authenticationRequest.getUsername());
        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword(),
                        Collections.emptyList()
                ));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        CustomUserDetail customUserDetail = (CustomUserDetail) authResult.getPrincipal();
        var accessToken = jwtService.generateToken(customUserDetail);
        var refreshToken = jwtService.refreshToken(customUserDetail);

        customUserDetailService.updateLoginAttempt(customUserDetail.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(
                accessToken,
                refreshToken
        );

        var authJson = objectMapper.writeValueAsString(authenticationResponse);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(authJson);
        log.info("Successful Authentication {}", authJson);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        var msgException = CustomMessageExceptionUtil.handleUnauthorized();
        var msgJson = objectMapper.writeValueAsString(msgException);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(msgJson);
        log.warn("Unsuccessful Authentication {}", msgJson);
    }
}
