package re.edu.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import re.edu.exception.JwtExceptionCustom;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String path = request.getRequestURI();

            // ===================== PUBLIC ENDPOINTS =====================
            if (
                    path.equals("/api/auth/login") ||
                            path.equals("/api/auth/verify")
            ) {
                filterChain.doFilter(request, response);
                return;
            }

            // ===================== TOKEN EXTRACTION =====================
            String token = getTokenFromRequest(request);

            // Nếu không có token -> để Spring Security xử lý (403 ở SecurityConfig)
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // ===================== AUTHENTICATION =====================
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                jwtProvider.validateToken(token);

                String username = jwtProvider.getUsernameFromToken(token);

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (JwtExceptionCustom ex) {
            handlerExceptionResolver.resolveException(
                    request,
                    response,
                    null,
                    ex
            );
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.isBlank()) {
            return null; // không ném lỗi để tránh chặn public request
        }

        if (!authorization.startsWith("Bearer ")) {
            throw new JwtExceptionCustom("Authorization header không hợp lệ");
        }

        String token = authorization.substring(7).trim();

        if (token.isBlank()) {
            throw new JwtExceptionCustom("Token rỗng hoặc null");
        }

        return token;
    }
}