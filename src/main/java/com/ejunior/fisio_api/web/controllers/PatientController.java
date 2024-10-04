package com.ejunior.fisio_api.web.controllers;

import com.ejunior.fisio_api.entities.Patient;
import com.ejunior.fisio_api.repositories.projections.PatientProjection;
import com.ejunior.fisio_api.repositories.projections.PhysicalTherapistProjection;
import com.ejunior.fisio_api.services.PatientService;
import com.ejunior.fisio_api.web.dtos.*;
import com.ejunior.fisio_api.web.dtos.mapper.PageableMapper;
import com.ejunior.fisio_api.web.dtos.mapper.PatientMapper;
import com.ejunior.fisio_api.web.exceptions.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Patient Endpoint")
@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService service;
    private final PatientMapper mapper;

    @Operation(summary= "Resource for create new Patient", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses ={
            @ApiResponse(responseCode = "201", description = "Resource created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Resource not processed, already registered Patient",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =  StandardError.class))),
            @ApiResponse(responseCode = "422", description = "Resource not processed,  invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FISIO')")
    public ResponseEntity<PatientResponseDto> create(@RequestBody @Valid PatientCreateDto patientDto){
        Patient patient = service.save(mapper.toPatient(patientDto));
       return ResponseEntity.status(201).body(PatientMapper.toDto(patient));
    }


    @Operation(summary= "Resource for find Patient by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses ={

            @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FISIO')")
    public ResponseEntity<PatientResponseDto> findById(@PathVariable long id){
        return ResponseEntity.ok(PatientMapper.toDto(service.findById(id)));
    }

    @Operation(summary= "Resource for find Patient by cpf", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses ={

                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FISIO')")
    public ResponseEntity<PatientResponseDto> findByCpf(@PathVariable String cpf){
        return ResponseEntity.ok(PatientMapper.toDto(service.findByCpf(cpf)));
    }

    @Operation(summary = "Resource for find patients by Hospital id", description = "This resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Actual Page"
                    ),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")),
                            description = "total elements for page"
                    ),
                    @Parameter(in = QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "nome,asc")),
                            description = "Represents the ordering of results. Several sorting criteria are supported.")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource find successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageableDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @GetMapping("/hospital/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FISIO')")
    public ResponseEntity<PageableDto> findByHospitalId(@PathVariable long id, @Parameter(hidden = true) @PageableDefault(size = 5, sort = {"name"}) Pageable pageable){
        Page<PatientProjection> patients = service.findByHospitalId(id, pageable);
        return ResponseEntity.ok(PageableMapper.toDto(patients));
    }


    @Operation(summary = "Resource for delete Patient by id", description = "Resource requires a bearer token",
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
