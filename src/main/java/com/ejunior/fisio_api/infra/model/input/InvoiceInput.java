package com.ejunior.fisio_api.infra.model.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceInput {

    private Long numeroConvenio;
    private Integer numeroCarteira;
    private Integer numeroVariacaoCarteira;
    private Integer codigoModalidade;
    private String dataEmissao; // dd.mm.aaaa
    private String dataVencimento; // dd.mm.aaaa
    private Double valorOriginal;
    private Double valorAbatimento;
    private Long quantidadeDiasProtesto;
    private Integer quantidadeDiasNegativacao;
    private Integer orgaoNegativador;
    private String indicadorAceiteTituloVencido;
    private Integer numeroDiasLimiteRecebimento; // informar se o valor anterior for S
    private char codigoAceite; // A ou N
    private Integer codigoTipoTitulo;
    private String descricaoTipoTitulo;
    private char indicadorPermissaoRecebimentoParcial; // S ou N
    private String numeroTituloBeneficiario;
    private String campoUtilizacaoBeneficiario;
    private String numeroTituloCliente;
    private String MensagemBloquetoOcorrencia;
    private String indicadorPix;
    private DescontoInput desconto;
    private JurosInput jurosMora;
    private MultaInput multa;
    private PagadorInput pagador;
}
