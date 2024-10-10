package com.ejunior.fisio_api.services.utils;

import com.ejunior.fisio_api.entities.Care;
import com.ejunior.fisio_api.entities.Invoice;
import com.ejunior.fisio_api.services.CareService;
import com.ejunior.fisio_api.services.HospitalService;
import com.ejunior.fisio_api.web.dtos.InvoiceCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InvoiceUtil {

    private final CareService careService;
    private final HospitalService hospitalService;

    public Invoice createInvoice(InvoiceCreateDto dto){
        Invoice invoice = new Invoice();
        List<Care> cares = careService.findByDateRangeAndHospitalId(LocalDate.parse(dto.getDateFirstCare()), LocalDate.parse(dto.getDateLastCare()), dto.getHospitalId());
        Double totalValue = calculateTotalValue(cares);
        invoice.setDateFirstCare(LocalDate.parse(dto.getDateFirstCare()));
        invoice.setDateLastCare(LocalDate.parse(dto.getDateLastCare()));
        invoice.setHospital(hospitalService.findById(dto.getHospitalId()));
        invoice.setTotalValue(totalValue);
        invoice.setCode(codeGenerate(dto));
        return invoice;
    }

    public Double calculateTotalValue(List<Care> cares){
        return cares.stream().map(Care::getPrice).reduce(0.0, Double::sum);
    }

    public String codeGenerate(InvoiceCreateDto dto){
        return dto.getDateFirstCare().replace("-","") +
                dto.getDateLastCare().replace("-","") + "-" +
                hospitalService.findById(dto.getHospitalId()).getCnpj();
    }

}
