package com.ejunior.fisio_api.web.controllers;

import com.ejunior.fisio_api.entities.Manager;
import com.ejunior.fisio_api.entities.PhysicalTherapist;
import com.ejunior.fisio_api.jwt.JwtUserDetails;
import com.ejunior.fisio_api.services.ManagerService;
import com.ejunior.fisio_api.services.UserService;
import com.ejunior.fisio_api.web.dtos.*;
import com.ejunior.fisio_api.web.dtos.mapper.CareTypeMapper;
import com.ejunior.fisio_api.web.dtos.mapper.ManagerMapper;
import com.ejunior.fisio_api.web.dtos.mapper.PhysicalTherapistsMapper;
import com.ejunior.fisio_api.web.exceptions.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/managers")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService service;
    private final UserService userService;

    @Operation(summary= "Resource for create new Manager, linked to a registered user", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses ={
                    @ApiResponse(responseCode = "201", description = "Resource created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ManagerResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "409", description = "Resource not processed, already registered Manager",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation =  StandardError.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed,  invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ManagerResponseDto> create(@RequestBody @Valid ManagerCreateDto dto, @AuthenticationPrincipal JwtUserDetails userDetails){
        Manager manager = ManagerMapper.toManager(dto);
        manager.setUser(userService.findById(userDetails.getId()));
        service.save(manager);
        return ResponseEntity.status(201).body(ManagerMapper.toDto(manager));
    }

    @Operation(summary = "Resource for find Manager by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ManagerResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ManagerResponseDto> findById(@PathVariable long id){
        return ResponseEntity.ok(ManagerMapper.toDto(service.findById(id)));
    }

    @Operation(summary = "Resource for find Manager by cpf", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ManagerResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ManagerResponseDto> findByCpf(@PathVariable String cpf){
        return ResponseEntity.ok(ManagerMapper.toDto(service.findByCpf(cpf)));
    }

    @Operation(summary = "Resource for findAll Managers", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ManagerResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ManagerResponseDto>> findAll(){
       List<ManagerResponseDto> managers = service.findAll().stream().map(ManagerMapper::toDto).toList();
        return ResponseEntity.ok(managers);
    }

    @Operation(summary = "Resource for update Salary by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @PatchMapping("/update-salary/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateSalary(@PathVariable long id, @RequestBody @Valid ManagerUpdateSalaryDto newSalary){
        service.updateSalary(id, newSalary.getNewSalary());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Resource for update Position by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not processed, User without permission",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))})
    @PatchMapping("/update-position/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updatePosition(@PathVariable long id, @RequestBody @Valid ManagerUpdatePositionDto newPosition){
        service.updatePosition(id, newPosition.getNewPosition());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Resource for delete manager by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully deleted",
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

    @Operation(summary = "Resource for find details fisio authenticated", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource find successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PhysicalTherapistResponseDto.class)))})
    @GetMapping("/details")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ManagerResponseDto> getDetails(@AuthenticationPrincipal JwtUserDetails userDetails){
        Manager manager = service.getDetails(userDetails.getId());
        return ResponseEntity.ok(ManagerMapper.toDto(manager));
    }

}
