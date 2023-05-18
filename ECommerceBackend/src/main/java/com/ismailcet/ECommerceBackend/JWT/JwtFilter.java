package com.ismailcet.ECommerceBackend.JWT;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomerUsersDetailsService service;

    public JwtFilter(JwtUtil jwtUtil, CustomerUsersDetailsService service) {
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    Claims claims = null;
    private String userName = null;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(httpServletRequest.getServletPath().matches("/api/users/login|/api/users/register|/api/users/forgorPassword")){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }else{
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String token = null;
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                token = authorizationHeader.substring(7);
                userName = jwtUtil.extractUsername(token);
                claims = jwtUtil.extractAllClaims(token);
            }

            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = service.loadUserByUsername(userName);
                if(jwtUtil.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }
    }

    public boolean isAdmin(){
        if(claims.get("role").equals("admin")){
            return true;
        }
        return false;
    }

    public boolean isUser(){
        if(claims.get("role").equals("user")){
            return true;
        }
        return false;
    }

    public String getCurrentUser(){
        return userName;
    }
    public Claims getClaims(){
        return claims;
    }
}
