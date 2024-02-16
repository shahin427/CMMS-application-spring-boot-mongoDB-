package org.sayar.net.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.sayar.net.Security.Service.JwtUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUserService userDetailsService;

    @Value("${jwt.header.key}")
    private String tokenHeaderKey;

    private static Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    public JwtAuthenticationFilter(JwtUserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        long start = System.currentTimeMillis();
        String token = null;
        if (httpServletRequest.getHeader(tokenHeaderKey) != null) {
            token = httpServletRequest.getHeader(tokenHeaderKey);
        } else if (httpServletRequest.getParameter(tokenHeaderKey) != null) {
            token = httpServletRequest.getParameter(tokenHeaderKey);
        }
        if (token != null) {
            try {
                String userName = JwtTokenUtil.getUserNameFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

                if (userDetails != null && JwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.info("Invalid Token");
                }
            } catch (ExpiredJwtException e) {
                logger.info("Token Expired");
            } catch (MalformedJwtException e2) {
                logger.info("Malformed Done");
            }
        } else {
            System.out.println("Token Null");
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            logger.info("User Log => Credentials : " + authentication.getCredentials() + " Authorities : " + authentication.getAuthorities() + " isAuthenticated : " + authentication.isAuthenticated() + " Details :  " + authentication.getDetails());
        }
        long end = System.currentTimeMillis();
        logger.info("Time For Url : " + httpServletRequest.getRequestURL() + " is " + (end - start) + " .mil");

    }
}
