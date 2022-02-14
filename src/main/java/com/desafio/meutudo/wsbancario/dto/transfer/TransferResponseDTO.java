package com.desafio.meutudo.wsbancario.dto.transfer;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponseDTO {
    @ApiModelProperty(value = "Mensagem de resposta.", example = "Resposta da api.")
    private String message;
}
