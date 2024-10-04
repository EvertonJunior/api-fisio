package com.ejunior.fisio_api.web.controllers;

import com.ejunior.fisio_api.entities.PhysicalTherapist;
import com.ejunior.fisio_api.entities.User;
import com.ejunior.fisio_api.jwt.JwtUserDetails;
import com.ejunior.fisio_api.repositories.projections.PhysicalTherapistProjection;
import com.ejunior.fisio_api.services.PhysicalTherapistService;
import com.ejunior.fisio_api.services.UserService;
import com.ejunior.fisio_api.web.dtos.PageableDto;
import com.ejunior.fisio_api.web.dtos.PhysicalTherapistCreateDto;
import com.ejunior.fisio_api.web.dtos.PhysicalTherapistResponseDto;
import com.ejunior.fisio_api.web.dtos.mapper.PageableMapper;
import com.ejunior.fisio_api.web.dtos.mapper.PhysicalTherapistsMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "PhysicalTherapist endpoint")
@RestController
@RequestMapping("/api/v1/physical-therapists")
@RequiredArgsConstructor
public class PhysicalTherapistController {

    private final PhysicalTherapistService service;
    private final UserService userService;


    @Operation(summary= "Resource for create new PhysicalTherapist, linked to a registered user", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses ={
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
    @PreAuthorize("hasRole('FISIO')")
    public ResponseEntity<PhysicalTherapistResponseDto> create(@RequestBody @Valid PhysicalTherapistCreateDto dto,
                                                               @AuthenticationPrincipal JwtUserDetails userDetails){
        PhysicalTherapist physicalTherapist = PhysicalTherapistsMapper.toPhysicalTherapist(dto);
        physicalTherapist.setUser(userService.findById(userDetails.getId()));
        service.save(physicalTherapist);
        return ResponseEntity.status(HttpStatus.CREATED).body(PhysicalTherapistsMapper.toDto(physicalTherapist));
    }

    @Operation(summary = "Resource for find PhysicalTherapist by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PhysicalTherapistResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PhysicalTherapistResponseDto> findById(@PathVariable long id){
        PhysicalTherapist physicalTherapist = service.findById(id);
        return ResponseEntity.ok(PhysicalTherapistsMapper.toDto(physicalTherapist));
    }

    @Operation(summary = "Resource for find PhysicalTherapist by cpf", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PhysicalTherapistResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PhysicalTherapistResponseDto> findByCpf(@PathVariable String cpf){
        PhysicalTherapist physicalTherapist = service.findByCpf(cpf);
        return ResponseEntity.ok(PhysicalTherapistsMapper.toDto(physicalTherapist));
    }


    @Operation(summary = "Resource for find all clients", description = "This resource requires a bearer token",
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
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> findAll(@Parameter(hidden = true) @PageableDefault(size = 5, sort = {"name"}) Pageable pageable){
            Page<PhysicalTherapistProjection> physicalTherapists = service.findAll(pageable);
            return ResponseEntity.ok(PageableMapper.toDto(physicalTherapists));
    }


    @Operation(summary = "Resource for delete by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "No content",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Resource for find details fisio authenticated", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource find successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PhysicalTherapistResponseDto.class)))})
    @GetMapping("/details")
    @PreAuthorize("hasRole('FISIO')")
    public ResponseEntity<PhysicalTherapistResponseDto> getDetails(@AuthenticationPrincipal JwtUserDetails userDetails){
        PhysicalTherapist physicalTherapist = service.findByUserId(userDetails.getId());
        return ResponseEntity.ok(PhysicalTherapistsMapper.toDto(physicalTherapist));
    }

}
