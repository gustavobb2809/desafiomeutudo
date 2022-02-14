package com.desafio.meutudo.wsbancario.security.constants;

public class ConstantsJwt {
    public static final String PASSWORD_TOKEN = "7X3xuPSXqUHTZ4TS4UxYdezwzhWeej6cWP3wBg55b";
    public static final int EXPIRATION_TOKEN = 10000_000;
    public static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/login"
    };
}
