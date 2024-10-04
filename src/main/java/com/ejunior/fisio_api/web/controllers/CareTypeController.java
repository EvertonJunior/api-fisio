package com.ejunior.fisio_api.web.controllers;

import com.ejunior.fisio_api.entities.CareType;
import com.ejunior.fisio_api.entities.Hospital;
import com.ejunior.fisio_api.services.CareTypeService;
import com.ejunior.fisio_api.services.HospitalService;
import com.ejunior.fisio_api.web.dtos.*;
import com.ejunior.fisio_api.web.dtos.mapper.CareTypeMapper;
import com.ejunior.fisio_api.web.dtos.mapper.HospitalMapper;
import com.ejunior.fisio_api.web.exceptions.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/care-types")
@Tag(name = "CareType Endpoint")
@RequiredArgsConstructor
public class CareTypeController {

    private final CareTypeService service;

    @Operation(summary= "Resource for create new CareTye", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses ={
                    @ApiResponse(responseCode = "201", description = "Resource created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CareTypeResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "409", description = "Resource not processed, already registered CareType",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation =  StandardError.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed,  invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CareTypeResponseDto> create (@RequestBody @Valid CareTypeCreateDto dto){
        CareType careType = CareTypeMapper.toCareType(dto);
        service.save(careType);
        return ResponseEntity.status(201).body(CareTypeMapper.toDto(careType));
    }


    @Operation(summary = "Resource for find CareType by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CareTypeResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FISIO')")
    public ResponseEntity<CareTypeResponseDto> findById(@PathVariable long id){
        return ResponseEntity.ok(CareTypeMapper.toDto(service.findById(id)));
    }

    @Operation(summary = "Resource for find all CareTypes", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CareTypeResponseDto.class)))})
    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN','FISIO')")
    public ResponseEntity<List<CareTypeResponseDto>> findAll(){
        List<CareTypeResponseDto> hospitals = service.findAll().stream().map(CareTypeMapper::toDto).toList();
        return ResponseEntity.ok(hospitals);
    }

    @Operation(summary = "Resource for update careType price by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Resource successfully updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updatePriceById(@PathVariable long id, @RequestBody @Valid CareTypeUpdateDto updateDto){
        service.updatePriceById(id, updateDto.getNewPrice());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Resource for delete CareType by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Resource successfully deleted",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
