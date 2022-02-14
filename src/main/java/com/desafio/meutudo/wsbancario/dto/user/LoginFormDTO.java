package com.desafio.meutudo.wsbancario.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginFormDTO {
    @ApiModelProperty(value = "Informe o e-mail", example = "gustavo.test@test.com", required = true)
    @NotNull(message = "É necessário informar o e-mail.")
    private String email;

    @ApiModelProperty(value = "Informe a senha", example = "12345678", required = true)
    @NotNull(message = "É necessário informar a senha.")
    private String password;
}
