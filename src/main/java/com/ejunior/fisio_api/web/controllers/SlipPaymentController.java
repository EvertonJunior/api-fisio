package com.ejunior.fisio_api.web.controllers;

import com.ejunior.fisio_api.infra.model.SlipRegistered;
import com.ejunior.fisio_api.infra.model.input.InvoiceInput;
import com.ejunior.fisio_api.services.SlipPaymentService;
import com.ejunior.fisio_api.web.dtos.PhysicalTherapistResponseDto;
import com.ejunior.fisio_api.web.dtos.SlipPaymentCreateDto;
import com.ejunior.fisio_api.web.dtos.mapper.SlipPaymentMapper;
import com.ejunior.fisio_api.web.exceptions.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/slips-payments")
@RequiredArgsConstructor
public class SlipPaymentController {

    private final SlipPaymentService service;


    @Operation(summary= "Resource for create new Invoice registered using api Banco do Brasil", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses ={
                    @ApiResponse(responseCode = "201", description = "Resource created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SlipRegistered.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "409", description = "Resource not processed, already registered numeroDocumento",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation =  StandardError.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed,  invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{invoiceId}")
    public ResponseEntity<SlipRegistered> register (@PathVariable long invoiceId, @RequestBody @Valid SlipPaymentCreateDto dto){
        return ResponseEntity.status(201).body(service.register(invoiceId, SlipPaymentMapper.toSlip(dto)));
    }

    @Operation(summary= "Resource for create pdf Invoice registered using api Stella Boleto", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses ={
                    @ApiResponse(responseCode = "201", description = "Resource created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = byte[].class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{invoiceId}/boleto/pdf", produces = MediaType.APPLICATION_PDF_VALUE )
    public ResponseEntity<byte[]> generateSlipPaymentPdf(@PathVariable long invoiceId){
        byte[] bytesPdf = service.generatePdf(invoiceId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytesPdf);
    }

}
