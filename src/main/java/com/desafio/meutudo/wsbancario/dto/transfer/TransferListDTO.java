package com.desafio.meutudo.wsbancario.dto.transfer;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferListDTO {

    @ApiModelProperty(value = "número da conta de origem", example = "ac-20222")
    private String account;

    @ApiModelProperty(value = "número da conta para transferência", example = "ac-20222")
    private String toAccount;

    @ApiModelProperty(value = "Uuid da transferência", example = "4f813156-6670-444d-b6da-784cbe1b634f")
    private String uuid;

    @ApiModelProperty(value = "Valor total", example = "100.00")
    private BigDecimal value;

    @ApiModelProperty(value = "Status", example = "PAGO")
    private String status;
}
