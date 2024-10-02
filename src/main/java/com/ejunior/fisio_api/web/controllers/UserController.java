package com.ejunior.fisio_api.web.controllers;

import com.ejunior.fisio_api.entities.User;
import com.ejunior.fisio_api.services.UserService;
import com.ejunior.fisio_api.web.dtos.UserCreateDto;
import com.ejunior.fisio_api.web.dtos.UserPasswordDto;
import com.ejunior.fisio_api.web.dtos.UserResponseDto;
import com.ejunior.fisio_api.web.exceptions.StandardError;
import com.ejunior.fisio_api.web.dtos.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User endpoint")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

   private final UserService service;

   @Operation(summary= "Resource for create new User", description = "Resource requires a bearer token",
           security = @SecurityRequirement(name = "security"),
           responses ={
           @ApiResponse(responseCode = "201", description = "Resource created successfully",
                   content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
           @ApiResponse(responseCode = "409", description = "Resource not processed, already registered user",
           content = @Content(mediaType = "application/json", schema = @Schema(implementation =  StandardError.class))),
           @ApiResponse(responseCode = "422", description = "Resource not processed,  invalid input data",
           content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
   })
   @PostMapping
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateDto userCreateDto){
        User user = UserMapper.toUserCreateDto(userCreateDto);
        service.create(user);
       return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDto(user));
   }

    @Operation(summary = "Resource for find user by id", description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR ( hasRole('FISIO') AND #id == authentication.principal.id)")
    public ResponseEntity<UserResponseDto> findById(@PathVariable long id){
        User user = service.findById(id);
        return ResponseEntity.ok(new UserResponseDto(user));
   }

    @Operation(summary = "Resource for find all users",
            description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(responseCode = "200", description = "Resource find successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class)))})
   @GetMapping
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<List<UserResponseDto>> findAll(){
       List<User> users= service.findAll();
       List<UserResponseDto> usersDto = users.stream().map(UserResponseDto::new).toList();
       return ResponseEntity.ok(usersDto);
   }

    @Operation(summary = "Resource for update password",
            description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(responseCode = "204", description = "Resource updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "422", description = "Resource not updated, invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "401", description = "Resource not updated, invalid password",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400", description = "Resource not updated, divergent passwords",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
   @PatchMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN') OR ( hasRole('FISIO') AND #id == authentication.principal.id)")
   public ResponseEntity<Void> updatePassword(@PathVariable long id, @Valid @RequestBody UserPasswordDto userDto){
        User user = service.updatePassword(id, userDto.getCurrentPassword(), userDto.getNewPassword(), userDto.getConfirmNewPassword());
        return ResponseEntity.noContent().build();
   }

    @Operation(summary = "Resource for delete user by id",
            description = "Resource requires a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(responseCode = "200", description = "Resource successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
   @DeleteMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<Void> deleteById(@PathVariable  long id){
       service.deleteById(id);
       return ResponseEntity.noContent().build();
   }

}
