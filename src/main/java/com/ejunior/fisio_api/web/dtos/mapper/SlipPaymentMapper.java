package com.ejunior.fisio_api.web.dtos.mapper;

import com.ejunior.fisio_api.entities.SlipPayment;
import com.ejunior.fisio_api.web.dtos.SlipPaymentCreateDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SlipPaymentMapper {

    public static SlipPayment toSlip(SlipPaymentCreateDto dto){
        return new ModelMapper().map(dto, SlipPayment.class);
    }

}
