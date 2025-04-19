package com.diplomado.tienda.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Component
@ControllerAdvice
public class GlobalAttributes {

    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication authentication) {
        return authentication != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
