package com.ejunior.fisio_api.services.utils.files;

import br.com.caelum.stella.boleto.*;
import br.com.caelum.stella.boleto.bancos.BancoDoBrasil;
import com.ejunior.fisio_api.entities.Invoice;
import com.ejunior.fisio_api.infra.model.input.InvoiceInput;

public interface SlipPaymentGeneratorService {

    public byte[] generate(Invoice invoice, InvoiceInput invoiceInput);


    default Boleto createBoleto (Invoice invoice, InvoiceInput invoiceInput){
        var instrucao1 = String.format("Apos o vencimento cobrar Juros de %s%% por mes. ", invoiceInput.getJurosMora().getPorcentagem());
        var instrucao2 = String.format("Apos o vencimento cobrar Multa de %s%% por mes. ", invoiceInput.getMulta().getPorcentagem());
        var instrucao3 = String.format("Referente a Fatura: %d", invoice.getId());


        var slipPayment = invoice.getSlipPayment();

        return Boleto.novoBoleto()
                .comBanco(new BancoDoBrasil())
                .comDatas(createDatas(invoice))
                .comBeneficiario(createBeneficiario(invoice))
                .comPagador(createPagador(invoice))
                .comValorBoleto(invoice.getTotalValue())
                .comNumeroDoDocumento(slipPayment.getNumeroDocumento())
                .comEspecieDocumento("DM")
                .comAceite(false)
                .comLocaisDePagamento("Pagável em qualquer banco até o vencimento")
                .comInstrucoes(instrucao1, instrucao2, instrucao3);
    }

    default Datas createDatas(Invoice invoice){
        var dueDate = invoice.getDueDate();
        var createdDate = invoice.getCreatedDate();
        var modifiedDate = invoice.getModifiedDate();

        return Datas.novasDatas().comDocumento(createdDate.getDayOfMonth(), createdDate.getMonthValue(), createdDate.getYear())
                .comProcessamento(modifiedDate.getDayOfMonth(), modifiedDate.getMonthValue(), modifiedDate.getYear())
                .comVencimento(dueDate.getDayOfMonth(), dueDate.getMonthValue(), dueDate.getYear());
    }

    default Beneficiario createBeneficiario(Invoice invoice){
        var slipPayment = invoice.getSlipPayment();
        var address = Endereco.novoEndereco().
                comLogradouro(slipPayment.getAddress().getStreet().concat(", ").concat(slipPayment.getAddress().getNumber()))
                .comBairro(slipPayment.getAddress().getDistrict())
                .comCep(slipPayment.getAddress().getCep())
                .comCidade(slipPayment.getAddress().getCity())
                .comUf(slipPayment.getAddress().getUf());

        return Beneficiario.novoBeneficiario()
                .comNomeBeneficiario(slipPayment.getRazaoSocialNossaEmpresa())
                .comDocumento(slipPayment.getCnpjNossaEmpresa())
                .comNossoNumero(slipPayment.getNossoNumero())
                .comDigitoNossoNumero("0")
                .comAgencia(slipPayment.getNumeroAgencia())
                .comDigitoAgencia(slipPayment.getDigitoAgencia())
                .comCodigoBeneficiario(slipPayment.getNumeroConta())
                .comDigitoCodigoBeneficiario(slipPayment.getDigitoConta())
                .comNumeroConvenio(slipPayment.getNumeroContrato())
                .comCarteira(slipPayment.getCarteira())
                .comEndereco(address);
    }

    default Pagador createPagador(Invoice invoice){
        var hospital = invoice.getHospital();

        var address = Endereco.novoEndereco().
                comLogradouro(hospital.getAddress().getStreet().concat(", ").concat(hospital.getAddress().getNumber()))
                .comBairro(hospital.getAddress().getDistrict())
                .comCep(hospital.getAddress().getCep())
                .comCidade(hospital.getAddress().getCity())
                .comUf(hospital.getAddress().getUf());

        return Pagador.novoPagador()
                .comNome(hospital.getName())
                .comDocumento(hospital.getCnpj())
                .comEndereco(address);
    }
}
