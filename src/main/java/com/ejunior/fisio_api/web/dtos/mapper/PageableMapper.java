package com.ejunior.fisio_api.web.dtos.mapper;

import com.ejunior.fisio_api.web.dtos.PageableDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableMapper {

    public static PageableDto toDto(Page page){
        return new ModelMapper().map(page, PageableDto.class);
    }

}
