package com.desafio.meutudo.wsbancario.controllers;

import com.desafio.meutudo.wsbancario.dto.user.LoginFormDTO;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Operation(summary = "Endpoint para realizar autenticação do usuário.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna o token."),
            @ApiResponse(code = 403, message = "Acesso proibido."),
            @ApiResponse(code = 401, message = "Usuário não autorizado."),
            @ApiResponse(code = 500, message = "Error interno.")
    })
    @PostMapping(value = "/login")
    public void login(@Valid @RequestBody LoginFormDTO loginFormDTO) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

}
