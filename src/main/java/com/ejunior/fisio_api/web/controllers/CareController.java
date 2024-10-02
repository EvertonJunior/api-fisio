package com.ejunior.fisio_api.web.controllers;

import com.ejunior.fisio_api.entities.Care;
import com.ejunior.fisio_api.entities.PhysicalTherapist;
import com.ejunior.fisio_api.jwt.JwtUserDetails;
import com.ejunior.fisio_api.repositories.projections.CareProjection;
import com.ejunior.fisio_api.services.CareService;
import com.ejunior.fisio_api.services.PhysicalTherapistService;
import com.ejunior.fisio_api.web.dtos.*;
import com.ejunior.fisio_api.web.dtos.mapper.CareMapper;
import com.ejunior.fisio_api.web.dtos.mapper.PageableMapper;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Care endpoint")
@RestController
@RequestMapping("/api/v1/cares")
@RequiredArgsConstructor
public class CareController {

    private final CareService service;
    private final PhysicalTherapistService physicalTherapistService;
    private final CareMapper mapper;


    @Operation(summary= "Resource for create new Care, linked to a registered user", description = "Resource requires a bearer token", responses ={
            @ApiResponse(responseCode = "201", description = "Resource created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PhysicalTherapistResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "422", description = "Resource not processed,  invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('FISIO')")
     public ResponseEntity<CareResponseDto> create(@RequestBody @Valid CareCreateDto careCreateDto,
                                                   @AuthenticationPrincipal JwtUserDetails userDetails){
         Care care = service.create(careCreateDto,userDetails);
         return ResponseEntity.status(201).body(mapper.careToDto(care));
    }

    @Operation(summary = "Resource for find Care by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CareResponseDto> findById(@PathVariable long id){
        Care care = service.findById(id);
        return ResponseEntity.ok(mapper.careToDto(care));
    }

    @Operation(summary = "Resource for find Care by Patient id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @GetMapping("/patient/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FISIO')")
    public ResponseEntity<List<CareResponseDto>> findByPatientId(@PathVariable long id){
        List<CareResponseDto> cares = service.findByPatientId(id).stream().map(mapper::careToDto).toList();
        return ResponseEntity.ok(cares);
    }

    @Operation(summary = "Resource for find Care by PhysicalTherapist id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @GetMapping("/physical-therapist/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CareResponseDto>> findByPhysicalTherapistId(@PathVariable long id){
        List<CareResponseDto> cares = service.findByPhysicalTherapistId(id).stream().map(mapper::careToDto).toList();
        return ResponseEntity.ok(cares);
    }

    @Operation(summary = "Resource for find Care by Hospital id", description = "Resource requires a bearer token",
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
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @GetMapping("/hospital/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> findByHospitalId(@PathVariable long id, @Parameter(hidden = true)
                                                        @PageableDefault(size = 5, sort = {"patientName"}) Pageable pageable){
        Page<CareProjection> cares = service.findByHospitalId(id, pageable);
        return ResponseEntity.ok(PageableMapper.toDto(cares));
    }

    @Operation(summary = "Resource for find Cares by user id", description = "Resource requires a bearer token",
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
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @GetMapping("/my-cares")
    @PreAuthorize("hasRole('FISIO')")
    public ResponseEntity<PageableDto> findByUserId(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                    @Parameter(hidden = true)
                                                    @PageableDefault(size = 5, sort = {"patientName"}) Pageable pageable){
        Page<CareProjection> cares = service.findByUserId(userDetails.getId(), pageable);
        return ResponseEntity.ok(PageableMapper.toDto(cares));
    }

    @Operation(summary = "Resource for delete Care by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Resource successfully deleted",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}