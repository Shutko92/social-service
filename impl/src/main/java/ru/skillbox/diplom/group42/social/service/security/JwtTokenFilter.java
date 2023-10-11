package ru.skillbox.diplom.group42.social.service.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain
    ) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String access = null;
        if (request.getCookies() != null) {
            Cookie cookie = Arrays.stream(request.getCookies())
                    .filter(header -> header.getName().equals("jwt"))
                    .findFirst().orElse(null);
            if (cookie != null) {
                access = cookie.getValue();
            }
        }
        if (access != null && !access.equals("") && jwtTokenProvider.validateToken(access)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(access);

            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
