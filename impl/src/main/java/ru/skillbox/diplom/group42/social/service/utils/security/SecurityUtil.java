package ru.skillbox.diplom.group42.social.service.utils.security;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.skillbox.diplom.group42.social.service.security.JwtUser;

@UtilityClass
public class SecurityUtil {

    public static Long getJwtUserIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
            JwtUser jwtUser = (JwtUser) auth.getPrincipal();
            return jwtUser.getId();
        }
        throw new IllegalStateException("An anonymous user attempted to access protected resources.");
    }

    public static JwtUser getJwtUserFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
            return (JwtUser) auth.getPrincipal();
        }
        throw new IllegalStateException("An anonymous user attempted to access protected resources.");
    }
}
