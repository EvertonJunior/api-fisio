package com.ejunior.fisio_api.infra.model.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DescontoInput {

    private Integer tipo;
    private String dataExpiracao;
    private Long porcentagem;
    private Double valor;
}
