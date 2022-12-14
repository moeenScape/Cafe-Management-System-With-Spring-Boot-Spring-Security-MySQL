package com.moeen.Newcafe.JWT;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private CustomerUserDetailsService service;
    Claims claims =null;
    public Claims newClaims;
    private String userName =null;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(httpServletRequest.getServletPath().matches("/user/login|/user/signup|/user/forgetPassword"))
        {
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }else {
            String authorizationHeader=httpServletRequest.getHeader("Authorization");
            String token=null;
            if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer "))
            {
                token=authorizationHeader.substring(7);
                userName=jwtUtil.extractUserName(token);
                claims= jwtUtil.extractAllClaim(token);
                newClaims=claims;

            }
            if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null)
            {
                UserDetails userDetails=service.loadUserByUsername(userName);
                if(jwtUtil.validateToken(token,userDetails))
                {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
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
    public boolean isAdmin()
    {

        return "admin".equalsIgnoreCase((String) newClaims.get("role"));
    }
    public boolean isUser()
    {
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }
    public String getCurrentUser()
    {
        return userName;
    }
}
