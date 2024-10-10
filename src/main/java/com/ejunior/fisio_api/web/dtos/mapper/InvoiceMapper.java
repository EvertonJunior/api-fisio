package com.ejunior.fisio_api.web.dtos.mapper;

import com.ejunior.fisio_api.entities.Invoice;
import com.ejunior.fisio_api.web.dtos.InvoiceResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvoiceMapper {

    public static InvoiceResponseDto toDto(Invoice invoice){
        return new ModelMapper().map(invoice, InvoiceResponseDto.class);
    }
}
