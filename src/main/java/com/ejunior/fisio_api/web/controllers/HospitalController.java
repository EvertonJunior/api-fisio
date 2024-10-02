package com.ejunior.fisio_api.web.controllers;

import com.ejunior.fisio_api.entities.Hospital;
import com.ejunior.fisio_api.entities.PhysicalTherapist;
import com.ejunior.fisio_api.services.HospitalService;
import com.ejunior.fisio_api.web.dtos.HospitalCreateDto;
import com.ejunior.fisio_api.web.dtos.HospitalResponseDto;
import com.ejunior.fisio_api.web.dtos.PhysicalTherapistResponseDto;
import com.ejunior.fisio_api.web.dtos.mapper.HospitalMapper;
import com.ejunior.fisio_api.web.exceptions.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hospitals")
public class HospitalController {

    private final HospitalService service;

    @Operation(summary= "Resource for create new Hospital", description = "Resource requires a bearer token", responses ={
            @ApiResponse(responseCode = "201", description = "Resource created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PhysicalTherapistResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "409", description = "Resource not processed, already registered PhysicalTherapist",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =  StandardError.class))),
            @ApiResponse(responseCode = "422", description = "Resource not processed,  invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HospitalResponseDto> create (@RequestBody @Valid HospitalCreateDto dto){
        Hospital hospital = HospitalMapper.toHospital(dto);
        return ResponseEntity.status(201).body(HospitalMapper.toDto(hospital));
    }


    @Operation(summary = "Resource for find hospital by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PhysicalTherapist.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FISIO')")
    public ResponseEntity<HospitalResponseDto> findById(@PathVariable long id){
        return ResponseEntity.ok(HospitalMapper.toDto(service.findById(id)));
    }

    @Operation(summary = "Resource for find hospital by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PhysicalTherapist.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @GetMapping("/cnpj/{cnpj}")
    @PreAuthorize("hasAnyRole('ADMIN','FISIO')")
    public ResponseEntity<HospitalResponseDto> findByCnpj(@PathVariable String cnpj){
        return ResponseEntity.ok(HospitalMapper.toDto(service.findByCnpj(cnpj)));
    }

    @Operation(summary = "Resource for find all hospitals", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PhysicalTherapist.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN','FISIO')")
    public ResponseEntity<List<HospitalResponseDto>> findAll(){
        List<HospitalResponseDto> hospitals = service.findAll().stream().map(HospitalMapper::toDto).toList();
        return ResponseEntity.ok(hospitals);
    }

    @Operation(summary = "Resource for delete hospital by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully deleted",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PhysicalTherapist.class))),
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
