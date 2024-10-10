package com.ejunior.fisio_api.services;

import com.ejunior.fisio_api.entities.Hospital;
import com.ejunior.fisio_api.entities.Invoice;
import com.ejunior.fisio_api.entities.InvoiceRegistered;
import com.ejunior.fisio_api.entities.SlipPayment;
import com.ejunior.fisio_api.infra.model.SlipRegistered;
import com.ejunior.fisio_api.infra.model.controller.AccessTokenController;
import com.ejunior.fisio_api.infra.model.controller.CobrancaController;
import com.ejunior.fisio_api.infra.model.input.*;
import com.ejunior.fisio_api.repositories.InvoiceRegisteredRepository;
import com.ejunior.fisio_api.repositories.SlipPaymentRepository;
import com.ejunior.fisio_api.services.utils.files.SlipPaymentGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SlipPaymentService {

    private final SlipPaymentRepository repository;
    private final InvoiceService invoiceService;
    private final AccessTokenController tokenController;
    private final CobrancaController cobrancaController;
    private final InvoiceRegisteredRepository invoiceRegisteredRepository;
    private final SlipPaymentGeneratorService generatorService;

    private String clientId = "eyJpZCI6ImVmMGMyMmYtZWZiYi00YWQ2LWFmIiwiY29kaWdvUHVibGljYWRvciI6MCwiY29kaWdvU29mdHdhcmUiOjExMjE4Miwic2VxdWVuY2lhbEluc3RhbGFjYW8iOjF9";
    private String clientSecret= "eyJpZCI6ImVlMDE4ZjAtMDdhNS00YTk4LWIiLCJjb2RpZ29QdWJsaWNhZG9yIjowLCJjb2RpZ29Tb2Z0d2FyZSI6MTEyMTgyLCJzZXF1ZW5jaWFsSW5zdGFsYWNhbyI6MSwic2VxdWVuY2lhbENyZWRlbmNpYWwiOjEsImFtYmllbnRlIjoiaG9tb2xvZ2FjYW8iLCJpYXQiOjE3Mjg0MDIzNjgwNTV9";
    private String appKey = "a3e5023a7f80f719ef1cc0e9676020d7";


    public byte[] generatePdf(long invoiceId){
        var invoice = invoiceService.findById(invoiceId);
        var slipPayment = generateSlipPayment(invoiceId);
        return generatorService.generate(invoice, slipPayment);
    }

    public SlipRegistered register(Long invoiceId, SlipPayment slipPayment) {
        Invoice invoice = invoiceService.findById(invoiceId);
        invoice.setSlipPayment(slipPayment);
        var token = tokenController.requestToken(clientId, clientSecret);
        var invoiceInput = generateSlipPayment(invoiceId);
        var slipRegistered = cobrancaController.register(invoiceInput, token, appKey);
        var invoiceRegistered = new InvoiceRegistered().create(invoice, slipRegistered.getLinhaDigitavel(),
                slipRegistered.getQrCode().getUrl(), slipRegistered.getQrCode().getEmv());
        slipPayment.setNossoNumero(invoiceInput.getNumeroTituloCliente().substring(3));
        repository.save(slipPayment);
        invoiceService.save(invoice);
        invoiceRegisteredRepository.save(invoiceRegistered);
        return slipRegistered;
    }

    public InvoiceInput generateSlipPayment(long invoiceId){
        Invoice invoice = invoiceService.findById(invoiceId);
        return create(invoice);
    }

    public InvoiceInput create(Invoice invoice){
        Hospital hospital = invoice.getHospital();
        SlipPayment slipPayment = invoice.getSlipPayment();
        DescontoInput descontoInput = new DescontoInput();
        descontoInput.setTipo(0);
        JurosInput juros = new JurosInput(2, Double.valueOf(slipPayment.getJurosPorcentagem()), 0.0);
        MultaInput multa = new MultaInput(2, convertDate(invoice.getDueDate().plusDays(1)), Double.valueOf(slipPayment.getMultaPorcentagem()), 0.0);
        PagadorInput pagador = new PagadorInput(2, hospital.getCnpj(), hospital.getName(), hospital.getAddress().getStreet()+ ", " + hospital.getAddress().getNumber(),
                Long.valueOf(hospital.getAddress().getCep().replace("-","")), hospital.getAddress().getCity(),
                hospital.getAddress().getDistrict(), hospital.getAddress().getUf(), null);
        InvoiceInput invoiceInput = new InvoiceInput();
        invoiceInput.setNumeroConvenio(Long.valueOf(slipPayment.getNumeroContrato()));
        invoiceInput.setNumeroCarteira(Integer.valueOf(slipPayment.getCarteira()));
        invoiceInput.setNumeroVariacaoCarteira(Integer.valueOf(slipPayment.getVariacaoCarteira()));
        invoiceInput.setDataVencimento(convertDate(invoice.getDueDate()));
        invoiceInput.setDataEmissao(convertDate(LocalDate.now()));
        invoiceInput.setValorOriginal(invoice.getTotalValue());
        invoiceInput.setIndicadorAceiteTituloVencido("S");
        invoiceInput.setCodigoAceite('N');
        invoiceInput.setCodigoTipoTitulo(2);
        invoiceInput.setDescricaoTipoTitulo("DM");
        invoiceInput.setIndicadorPermissaoRecebimentoParcial('N');
        invoiceInput.setNumeroTituloBeneficiario(slipPayment.getNumeroDocumento());
        invoiceInput.setNumeroTituloCliente(createNossoNumero(invoice));
        invoiceInput.setIndicadorPix("S");
        invoiceInput.setDesconto(descontoInput);
        invoiceInput.setJurosMora(juros);
        invoiceInput.setMulta(multa);
        invoiceInput.setPagador(pagador);
        return invoiceInput;
    }

    private String convertDate(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private String createNossoNumero(Invoice invoice){
        return String.format("%010d", Long.valueOf(invoice.getSlipPayment().getNumeroContrato()))
                .concat(String.format("%010d", Long.valueOf(invoice.getSlipPayment().getNumeroDocumento())));
    }
}
