package com.desafio.meutudo.wsbancario.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import com.desafio.meutudo.wsbancario.models.User;
import com.desafio.meutudo.wsbancario.security.constants.ConstantsJwt;
import com.desafio.meutudo.wsbancario.security.data.UserDetailData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JwtAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            User user = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    user.getPassword(),
                    new ArrayList<>()
            ));

        } catch (IOException e) {
            throw new RuntimeException("Unauthenticated user", e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserDetailData userDetailData = (UserDetailData) authResult.getPrincipal();

        String token = JWT.create().
                withSubject(userDetailData.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ConstantsJwt.EXPIRATION_TOKEN))
                .sign(Algorithm.HMAC512(ConstantsJwt.PASSWORD_TOKEN));

        response.getWriter().write(token);
        response.getWriter().flush();
    }
}
