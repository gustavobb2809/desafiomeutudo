package com.desafio.meutudo.wsbancario.dto.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDTO {
    @ApiModelProperty(value = "Número da conta.", example = "ac-20231")
    private String account;
    @ApiModelProperty(value = "Saldo da conta.", example = "300.00")
    private BigDecimal balance;
}
