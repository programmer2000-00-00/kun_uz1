package com.example.config;

import com.example.dto.JwtDTO;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static com.example.util.JwtTokenUtil.decode;



@Component
public class TokenFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Salom iwladimi");
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null && !authHeader.startsWith("Bearer")) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Message", "Token not found");
            return;
        }

        String token = authHeader.substring(7);
        JwtDTO jwtDTO;
        try {
            jwtDTO = decode(token);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Message", "Token not valid");
            return;
        }

        request.setAttribute("phone", jwtDTO.getPhone());
        request.setAttribute("role", jwtDTO.getRole());
        filterChain.doFilter(request, response);
    }
}
