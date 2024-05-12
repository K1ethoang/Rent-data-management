package com.example.demo.config;

import com.example.demo.service.interfaces.TokenService;
import com.example.demo.service.interfaces.UserService;
import com.example.demo.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@Log4j2
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final TokenService tokenService;

    public boolean isBypassToken(@NonNull HttpServletRequest request) {
        String[] bypassToken = {
                "/auth/**",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-ui/index.html",
                "/webjars/swagger-ui/**",
                "/logout"
        };

        String requestPath = request.getServletPath();

        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) return true;

        for (String bypass : bypassToken) {

            if (bypass.contains("**")) {
                String regexPath = bypass.replace("**", ".*");
                // Create a pattern to match request path
                Pattern pattern = Pattern.compile(regexPath);
                Matcher matcher = pattern.matcher(requestPath);

                if (matcher.matches()) return true;
            } else if (requestPath.equals(bypass)) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws IOException {

        try {
            if (isBypassToken(request)) {
                chain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);

            if (authHeader == null || !authHeader.startsWith(JwtUtil.BEARER_PREFIX)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            final String token = authHeader.substring(JwtUtil.BEARER_PREFIX.length());
            final String username = JwtUtil.extractUsername(token);

            if (username != null) {
                UserDetails userDetails = userService.loadUserByUsername(username);
                String tokenFromDb = tokenService.getAccessToken(username);

                if (JwtUtil.isValidAccessToken(token, userDetails) && token.equals(tokenFromDb)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
        }
    }
}
