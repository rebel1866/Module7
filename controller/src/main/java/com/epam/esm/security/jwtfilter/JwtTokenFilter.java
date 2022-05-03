package com.epam.esm.security.jwtfilter;

import com.epam.esm.errorhandler.ErrorHandler;
import com.epam.esm.security.exception.JwtAuthenticationException;
import com.epam.esm.security.provider.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            ErrorHandler.ErrorMessage errorMessage = new ErrorHandler.ErrorMessage("Token is expired or incorrect",
                    "errorCode=3", HttpStatus.BAD_REQUEST, "Invalid token");
            ObjectMapper objectMapper = new ObjectMapper();
            servletResponse.getWriter().write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorMessage));
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
