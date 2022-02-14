package com.desafio.meutudo.wsbancario.dto.transfer;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {
    @ApiModelProperty(value = "Número da conta.", example = "ac-20222", required = true)
    @NotNull(message = "É necessário informar o número da conta.")
    private String account;

    @ApiModelProperty(value = "Número da agência.", example = "ag-20222", required = true)
    @NotNull(message = "É necessário informar o número da agência.")
    private String agency;

    @ApiModelProperty(value = "Valor da transferência.", example = "300.00", required = true)
    @NotNull(message = "É necessário informar um valor.")
    private BigDecimal value;

    @ApiModelProperty(value = "Quantidade de parcelas (Caso não seja informada o valor padrão é 1).", example = "1")
    private Integer installments;

    @ApiModelProperty(value = "Data para agendar uma transferência (Caso não seja informado não será feito uma transferência agendada).", example = "2022-02-08")
    private LocalDateTime scheduledDate;
}
