package com.desafio.meutudo.wsbancario.dto.installments;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstallmentsListDTO {

    @ApiModelProperty(value = "Uuid da transferência", example = "4f813156-6670-444d-b6da-784cbe1b634f")
    private String transfer;

    @ApiModelProperty(value = "Conta de origem", example = "ac-20222")
    private String account;

    @ApiModelProperty(value = "Conta referênciada para transferência", example = "ac-20222")
    private String toAccount;

    @ApiModelProperty(value = "Valor da parcela", example = "100.00")
    private BigDecimal value;

    @ApiModelProperty(value = "Status", example = "PAGO")
    private String status;

    @ApiModelProperty(value = "Data de agendamento", example = "2022-02-10")
    private String scheduledDate;
}
