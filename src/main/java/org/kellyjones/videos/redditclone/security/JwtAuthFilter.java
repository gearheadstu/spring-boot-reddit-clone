package org.kellyjones.videos.redditclone.security;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String HEADER_NAME = "Authorization";
    private static final String PREFIX_BEARER = "Bearer ";

    @Autowired
    private JwtProvider jwtProvider; // FIXME why not final and use lombok constructor injection?

    @Autowired
    private UserDetailsService userDetailsService; // FIXME Why not lombok constructor injection?

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String jwt = getJwtFromRequest(request);

        if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
            String username = jwtProvider.getUsernameFromJwt(jwt);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_NAME);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(PREFIX_BEARER)) {
            return bearerToken.substring(PREFIX_BEARER.length());
        }
        return bearerToken;
    }
}
