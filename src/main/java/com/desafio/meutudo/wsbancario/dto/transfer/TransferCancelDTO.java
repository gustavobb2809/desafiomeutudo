package com.desafio.meutudo.wsbancario.dto.transfer;

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
public class TransferCancelDTO {
    @ApiModelProperty(value = "UUID da transferência.", example = "5db23d01-c2a7-43e7-ab71-6df4c55478f5", required = true)
    @NotNull(message = "É necessário informar o uuid da transferência.")
    private String uuid;
}
