package com.ejunior.fisio_api.infra.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlipRegistered {

    private Long codigoCliente;
    private Long numeroCarteira;
    private Long numeroVariacaoCarteira;
    private String numero;
    private String linhaDigitavel;
    private String codigoBarraNumerico;
    private Long numeroContratoCobranca;
    private QrCodeModel qrCode;
}
