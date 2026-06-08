package com.uts_saberpro_platform.app.enums;

public enum TipoCarrera {
    TECNOLOGIA,
    INGENIERIA;
    
    public static TipoCarrera fromValue(String value) {
        if (value == null) return null;
        String upper = value.toUpperCase().trim();
        if (upper.equals("TECNOLOGIA") || upper.equals("TECNOLOGIA")) {
            return TECNOLOGIA;
        }
        if (upper.equals("INGENIERIA") || upper.equals("INGENIERIA")) {
            return INGENIERIA;
        }
        return null;
    }
}