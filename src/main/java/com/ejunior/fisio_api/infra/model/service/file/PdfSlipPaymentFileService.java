package com.ejunior.fisio_api.infra.model.service.file;

import br.com.caelum.stella.boleto.transformer.*;
import com.ejunior.fisio_api.entities.Invoice;
import com.ejunior.fisio_api.infra.model.input.InvoiceInput;
import com.ejunior.fisio_api.repositories.InvoiceRegisteredRepository;
import com.ejunior.fisio_api.services.utils.files.SlipPaymentGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class PdfSlipPaymentFileService implements SlipPaymentGeneratorService {

    private final InputStreamQrCodeService qrGenService;

    private final InvoiceRegisteredRepository invoiceRegisteredRepository;

    @Override
    public byte[] generate(Invoice invoice, InvoiceInput invoiceInput) {

        var boleto = createBoleto(invoice, invoiceInput);

        var pathBoletoPersonalizado = this.getClass().getResourceAsStream("/relatorios/boleto-sem-sacador-avalista.jasper");
        var pathLogo = this.getClass().getResourceAsStream("/relatorios/logo.png");
        var invoiceRegistered = invoiceRegisteredRepository.findByInvoiceId(invoice.getId());

        var parameters = new HashMap<String, Object>();
        parameters.put("QRCODE", qrGenService.generate(invoiceRegistered.getQrcodeEmv()));
        parameters.put("LOGO", pathLogo);

        var generator = new GeradorDeBoleto(pathBoletoPersonalizado, parameters, boleto);

        return generator.geraPDF();
    }


}
