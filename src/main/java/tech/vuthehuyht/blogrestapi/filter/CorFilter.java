package tech.vuthehuyht.blogrestapi.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.*;

@Configuration
@Slf4j
public class CorFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("ServletRequest {}", servletRequest);

        final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        httpServletResponse.setHeader(ACCESS_CONTROL_ALLOW_METHODS, "*");
        httpServletResponse.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, "Authorization, Content-Type");
        httpServletResponse.setHeader(ACCESS_CONTROL_MAX_AGE, "3600");

        if(HttpMethod.OPTIONS.name().equalsIgnoreCase(((HttpServletRequest) servletRequest).getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }

        log.info("ServletResponse {}", httpServletResponse);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
