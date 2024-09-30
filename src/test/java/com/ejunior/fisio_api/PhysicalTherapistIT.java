package com.ejunior.fisio_api;

import com.ejunior.fisio_api.entities.PhysicalTherapist;
import com.ejunior.fisio_api.web.dtos.*;
import com.ejunior.fisio_api.web.exceptions.StandardError;
import io.jsonwebtoken.Jwt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/physical-therapists/physical-therapists-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/physical-therapists/physical-therapists-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PhysicalTherapistIT {

    @Autowired
    private WebTestClient testClient;

    @Test
    void physicalTherapistCreate_WhenUserValid_ThenReturnPhysicalTherapistCreated(){
        PhysicalTherapistResponseDto response = testClient.post().uri("/api/v1/physical-therapists")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"juvaneide@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PhysicalTherapistCreateDto("Juvaneide Silva","48492635851"))
                .exchange().expectStatus().isCreated().expectBody(PhysicalTherapistResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Leandro Silva");
    }

    @Test
    void physicalTherapistCreate_WhenNameNotValid_ThenReturnStandardErrorAndStatusCode422(){
        StandardError response = testClient.post().uri("/api/v1/physical-therapists")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juvaneide@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PhysicalTherapistCreateDto("Juv","32771877058"))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = testClient.post().uri("/api/v1/physical-therapists")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juvaneide@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PhysicalTherapistCreateDto("","32771877058"))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);


    }

    @Test
    void physicalTherapistCreate_WhenCpfNotValid_ThenReturnStandardErrorAndStatusCode422(){
        StandardError response = testClient.post().uri("/api/v1/physical-therapists")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juvaneide@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PhysicalTherapistCreateDto("Juv","32"))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = testClient.post().uri("/api/v1/physical-therapists")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juvaneide@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PhysicalTherapistCreateDto("Juv","324234234234234234234234234324"))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

    }

    @Test
    void physicalTherapistCreate_WhenCpfAlreadyExistsInSystem_ThenReturnStandardErrorAndStatusCode409(){
        StandardError response = testClient.post().uri("/api/v1/physical-therapists")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juvaneide@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PhysicalTherapistCreateDto("Juvaneide Silva","32771877058"))
                .exchange().expectStatus().isEqualTo(409).expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    void physicalTherapistCreate_WhenUserWithoutPermission_ThenReturnStandardErrorAndStatusCode403(){
        StandardError response = testClient.post().uri("/api/v1/physical-therapists")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PhysicalTherapistCreateDto("Juvaneide Silva","32771877058"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findPhysicalTherapist_WhenFindById_ThenReturnPhysicalTherapistAndStatus200(){
        PhysicalTherapistResponseDto response = testClient.get().uri("/api/v1/physical-therapists/1")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(PhysicalTherapistResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Ana maria");
    }

    @Test
    void findPhysicalTherapist_WhenFindByIdUserWithoutPermission_ThenReturnExceptionAndStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/physical-therapists/1")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "jose@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findPhysicalTherapist_WhenFindById_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/physical-therapists/400")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findPhysicalTherapist_WhenFindByCpf_ThenReturnPhysicalTherapistAndStatus200(){
        PhysicalTherapistResponseDto response = testClient.get().uri("/api/v1/physical-therapists/cpf/27343573055")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(PhysicalTherapistResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Maria Silva");
    }

    @Test
    void findPhysicalTherapist_WhenFindByCpfUserWithoutPermission_ThenReturnExceptionAndStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/physical-therapists/cpf/27343573055")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "jose@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findPhysicalTherapist_WhenFindByCpf_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/physical-therapists/cpf/32771877058")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findPhysicalTherapists_WhenFindAllWithPaginationByAdmin_ThenReturnPhysicalTherapistsList(){
        PageableDto response = testClient.get().uri("/api/v1/physical-therapists")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com","123456"))
                .exchange().expectStatus().isOk().expectBody(PageableDto.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getContent().size()).isEqualTo(5);
        org.assertj.core.api.Assertions.assertThat(response.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(response.getTotalPages()).isEqualTo(2);


        response = testClient.get().uri("/api/v1/physical-therapists?size=1&page=1")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com","123456"))
                .exchange().expectStatus().isOk().expectBody(PageableDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getContent().size()).isEqualTo(1);
        Assertions.assertThat(response.getTotalPages()).isEqualTo(8);
    }

    @Test
    void findPhysicalTherapists_WhenFindAllUserWithoutPermission_ThenReturnExceptionAndStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/physical-therapists/cpf/27343573055")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findDetailsPhysicalTherapist_WhenFindDetailsWithAuthenticatedUser_ThenReturnDetailsPhysicalTherapist(){
        PhysicalTherapistResponseDto response = testClient.get().uri("/api/v1/physical-therapists/details")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(PhysicalTherapistResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Maria Silva");
    }

}

