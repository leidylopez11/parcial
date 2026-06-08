package com.uts_saberpro_platform.app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedireccionController {
    
    @GetMapping("/dashboard")
    public String redirigir(Authentication auth) {
        if (auth == null) {
            return "redirect:/login";
        }
        
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_COORDINACION"))) {
            return "redirect:/coordinacion/dashboard";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DOCENTE"))) {
            return "redirect:/docente/dashboard";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ESTUDIANTE"))) {
            return "redirect:/estudiante/dashboard";
        }
        
        return "redirect:/login";
    }
}