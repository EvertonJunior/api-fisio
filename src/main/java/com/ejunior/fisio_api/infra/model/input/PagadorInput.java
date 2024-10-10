package com.ejunior.fisio_api.infra.model.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagadorInput {

    private Integer tipoInscricao;
    private String numeroInscricao;
    private String nome;
    private String endereco;
    private Long cep;
    private String cidade;
    private String bairro;
    private String uf;
    private String telefone;
}
