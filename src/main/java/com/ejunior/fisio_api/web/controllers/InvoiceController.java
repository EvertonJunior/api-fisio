package com.ejunior.fisio_api.web.controllers;

import com.ejunior.fisio_api.entities.Invoice;
import com.ejunior.fisio_api.services.InvoiceService;
import com.ejunior.fisio_api.services.utils.InvoiceUtil;
import com.ejunior.fisio_api.web.dtos.InvoiceCreateDto;
import com.ejunior.fisio_api.web.dtos.InvoiceResponseDto;
import com.ejunior.fisio_api.web.dtos.mapper.InvoiceMapper;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/invoices")
@Tag(name = "Invoice endpoint")
public class InvoiceController {

    private final InvoiceService service;
    private final InvoiceUtil util;

    @Operation(summary= "Resource for create new Invoice", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),responses ={
            @ApiResponse(responseCode = "201", description = "Resource created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "422", description = "Resource not processed,  invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InvoiceResponseDto> create(@RequestBody @Valid InvoiceCreateDto dto){
        Invoice invoice = util.createInvoice(dto);
        service.save(invoice);
        return ResponseEntity.status(201).body(InvoiceMapper.toDto(invoice));
    }

    @Operation(summary = "Resource for find Invoice by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InvoiceResponseDto> findById(@PathVariable long id){
        return ResponseEntity.ok(InvoiceMapper.toDto(service.findById(id)));
    }

    @Operation(summary = "Resource for find invoice by hospital id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @GetMapping("/hospital/{hospitalId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InvoiceResponseDto>> findByHospitalId(@PathVariable long hospitalId){
        List<InvoiceResponseDto> invoices = service.findByHospitalId(hospitalId).stream().map(InvoiceMapper::toDto).toList();
        return ResponseEntity.ok(invoices);
    }

    @Operation(summary = "Resource for find all invoices", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InvoiceResponseDto>> findAll(){
        List<InvoiceResponseDto> hospitals = service.findAll().stream().map(InvoiceMapper::toDto).toList();
        return ResponseEntity.ok(hospitals);
    }

    @Operation(summary = "Resource for delete invoice by id", description = "Resource requires a bearer token",
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
