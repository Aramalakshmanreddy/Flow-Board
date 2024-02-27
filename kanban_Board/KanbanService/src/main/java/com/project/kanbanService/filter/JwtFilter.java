package com.project.kanbanService.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        System.out.println("came here Filter" +httpServletRequest.getRequestURI());
        if (httpServletRequest.getRequestURI().endsWith("/user/save"))
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        else {
            String authHeader = httpServletRequest.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer"))
                throw new ServletException("Missing or invalid Token ");

            String token = authHeader.substring(7);
            //we have implemented the token expiry, if token expires then catch block will gets message
            try {
                Claims claims = Jwts.parser().setSigningKey("PrivateKey").parseClaimsJws(token).getBody();
                httpServletRequest.setAttribute("claims", claims);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
              }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED , "Token Expired, Please login again");
            }
        }

    }
}
