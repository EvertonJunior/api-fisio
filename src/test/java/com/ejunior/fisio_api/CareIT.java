package com.ejunior.fisio_api;

import com.ejunior.fisio_api.entities.Care;
import com.ejunior.fisio_api.entities.PhysicalTherapist;
import com.ejunior.fisio_api.repositories.PhysicalTherapistRepository;
import com.ejunior.fisio_api.services.PhysicalTherapistService;
import com.ejunior.fisio_api.web.dtos.*;
import com.ejunior.fisio_api.web.exceptions.StandardError;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/cares/cares-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/cares/cares-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CareIT {

    @Autowired
    private WebTestClient testClient;

    @Autowired
    private PhysicalTherapistService physicalTherapistService;

    @Test
    void careCreate_whenCreateWithUserAuthenticated_ThenReturnSavedNewCare(){
        CareResponseDto response = testClient.post().uri("/api/v1/cares")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "carlos@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CareCreateDto(11L, 31L, 22L,"Atendimento feito em paciente com dores"))
                .exchange().expectStatus().isCreated().expectBody(CareResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getCareName()).isEqualTo("Care 3");

        PhysicalTherapist terapist = physicalTherapistService.findById(8);
        Assertions.assertThat(terapist.getPayment()).isEqualTo(63.00);
    }

    @Test
    void careCreate_WhenDescriptionNotValid_ThenReturnStandardErrorAndStatusCode422(){
        StandardError response = testClient.post().uri("/api/v1/cares")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "carlos@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CareCreateDto(11L, 31L, 22L,"Al"))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void careCreate_WhenUserWithoutPermission_ThenReturnStandardErrorAndStatusCode403(){
        StandardError response = testClient.post().uri("/api/v1/cares")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CareCreateDto(11L, 31L, 22L,"Atendimento feito em paciente com dores"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findCare_WhenFindById_ThenReturnCareAndStatus200(){
        CareResponseDto response = testClient.get().uri("/api/v1/cares/40")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(CareResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getCareName()).isEqualTo("Care 1");
    }

    @Test
    void findCare_WhenFindByIdUserWithoutPermission_ThenReturnStandardErrorAndStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/cares/40")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "carlos@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findCare_WhenFindByIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/cares/61")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findCare_WhenFindByPatientId_ThenReturnCareAndStatus200() {
        List<CareResponseDto> response = testClient.get().uri("/api/v1/cares/patient/30")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "carlos@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBodyList(CareResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(6);
        Assertions.assertThat(response.getFirst().getPatientName()).isEqualTo("Angelina Silva");

        response = testClient.get().uri("/api/v1/cares/patient/30")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBodyList(CareResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(6);
        Assertions.assertThat(response.getFirst().getPatientName()).isEqualTo("Angelina Silva");
    }

    @Test
    void findCare_WhenFindByPatientIdIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/cares/patient/828")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "carlos@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findCare_WhenFindByPhysicalTherapistId_ThenReturnCaresAndStatus200() {
        List<CareResponseDto> response = testClient.get().uri("/api/v1/cares/physical-therapist/8")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBodyList(CareResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(6);
        Assertions.assertThat(response.getFirst().getPhysicalTherapistName()).isEqualTo("Carlos Silva");
        Assertions.assertThat(response.getFirst().getPatientName()).isEqualTo("Angelina Silva");
        Assertions.assertThat(response.get(1).getPatientName()).isEqualTo("Oswaldo Silva");

        response = testClient.get().uri("/api/v1/cares/physical-therapist/6")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBodyList(CareResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(3);
        Assertions.assertThat(response.getFirst().getPhysicalTherapistName()).isEqualTo("Ezequiel Silva");
        Assertions.assertThat(response.get(2).getPatientName()).isEqualTo("Diniz Silva");
    }

    @Test
    void findCare_WhenFindByPhysicalTherapistIdWithUserWithoutPermission_ThenReturnStandardErrorAndStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/cares/physical-therapist/8")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "carlos@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findCare_WhenFindByPhysicalTherapistIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/cares/physical-therapist/828")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findCare_WhenFindByHospitalId_ThenReturnCaresAndStatus200() {
        PageableDto response = testClient.get().uri("/api/v1/cares/hospital/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(PageableDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getContent().size()).isEqualTo(5);

        response = testClient.get().uri("/api/v1/cares/hospital/10?size=2&page=1")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(PageableDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getContent().size()).isEqualTo(2);
        Assertions.assertThat(response.getTotalPages()).isEqualTo(3);
    }

    @Test
    void findCare_WhenFindByHospitalIdWithUserWithoutPermission_ThenReturnStandardErrorAndStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/cares/hospital/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "carlos@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findCare_WhenFindByHospitalIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/cares/hospital/2333")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findCare_WhenFindByUserId_ThenReturnCaresAndStatus200() {
        PageableDto response = testClient.get().uri("/api/v1/cares/my-cares")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "carlos@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(PageableDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getContent().size()).isEqualTo(5);
        Assertions.assertThat(response.getTotalPages()).isEqualTo(2);

        response = testClient.get().uri("/api/v1/cares/my-cares?size=1&page=0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "carlos@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(PageableDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getContent().size()).isEqualTo(1);
        Assertions.assertThat(response.getTotalPages()).isEqualTo(6);


        response = testClient.get().uri("/api/v1/cares/my-cares")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ezequiel@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(PageableDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getContent().size()).isEqualTo(3);
        Assertions.assertThat(response.getTotalPages()).isEqualTo(1);
    }

    @Test
    void findCare_WhenFindByUserIdWithUserWithoutPermission_ThenReturnStandardErrorAndStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/cares/my-cares")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findCare_WhenFindByUserIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/cares/my-cares")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void deleteCare_WhenDeleteById_ThenReturnNoContentStatus204() {
        testClient.delete().uri("/api/v1/cares/40")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNoContent();

        PhysicalTherapist physicalTherapist = physicalTherapistService.findById(8);

        Assertions.assertThat(physicalTherapist.getPayment()).isEqualTo(59);
    }

    @Test
    void deleteCare_WWhenDeleteByIdWithUserWithoutPermission_ThenReturnStandardErrorAndStatusCode403(){
        StandardError response = testClient.delete().uri("/api/v1/cares/40")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void deleteCare_WWhenDeleteByIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.delete().uri("/api/v1/cares/8")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

}
