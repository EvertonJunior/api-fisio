package com.ejunior.fisio_api;

import com.ejunior.fisio_api.entities.Patient;
import com.ejunior.fisio_api.web.dtos.*;
import com.ejunior.fisio_api.web.exceptions.StandardError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/patients/patient-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/patients/patient-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PatientIT {

    @Autowired
    private WebTestClient testClient;

    @Test
    void patientCreate_whenCreateWithUserAuthenticated_ThenReturnSavedNewPatient(){
        PatientResponseDto response = testClient.post().uri("/api/v1/patients")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PatientCreateDto("Evandro Castro","47675219040",10L))
                .exchange().expectStatus().isCreated().expectBody(PatientResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Evandro Castro");

        response = testClient.post().uri("/api/v1/patients")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PatientCreateDto("Evandro Silva","36690573079",10L))
                .exchange().expectStatus().isCreated().expectBody(PatientResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Evandro Silva");
        Assertions.assertThat(response.getHospitalName()).isEqualTo("Santa Casa");

    }

    @Test
    void patientCreate_WhenCpfAlreadyExistsInSystem_ThenReturnStandardErrorAndStatusCode409(){
        StandardError response = testClient.post().uri("/api/v1/patients")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PatientCreateDto("Juvaneide Silva","82539082025", 10L))
                .exchange().expectStatus().isEqualTo(409).expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    void patientCreate_WhenCpfNotValid_ThenReturnStandardErrorAndStatusCode422(){
        StandardError response = testClient.post().uri("/api/v1/patients")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PatientCreateDto("Evandro Castro","476",10L))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = testClient.post().uri("/api/v1/patients")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PatientCreateDto("Evandro Castro","476321312312312312312312",10L))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void patientCreate_WhenNameNotValid_ThenReturnStandardErrorAndStatusCode422(){
        StandardError response = testClient.post().uri("/api/v1/patients")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PatientCreateDto("Ev","47675219040",10L))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void findPatient_WhenFindById_ThenReturnPatientAndStatus200(){
        PatientResponseDto response = testClient.get().uri("/api/v1/patients/30")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(PatientResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Angelina Silva");
    }

    @Test
    void findPatient_WhenFindByIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/patients/3330")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isEqualTo(404).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findPatient_WhenFindByCpf_ThenReturnPatientAndStatus200(){
        PatientResponseDto response = testClient.get().uri("/api/v1/patients/cpf/82539082025")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(PatientResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Angelina Silva");
    }

    @Test
    void findPatient_WhenFindByCpfWithCpfInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/patients/cpf/3330")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isEqualTo(404).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findPatient_WhenFindByHospitalId_ThenReturnPatientsAndStatus200() {
        PageableDto response = testClient.get().uri("/api/v1/patients/hospital/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(PageableDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getContent().size()).isEqualTo(3);

        response = testClient.get().uri("/api/v1/patients/hospital/10?size=1&page=1")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(PageableDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getContent().size()).isEqualTo(1);
        Assertions.assertThat(response.getTotalPages()).isEqualTo(3);
    }

    @Test
    void findPatient_WhenFindByHospitalIdWithHospitalIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/patients/hospital/2003")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isEqualTo(404).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void deletePatient_WhenDeleteById_ThenReturnPatientDeletedAndStatus204(){
        testClient.delete().uri("/api/v1/patients/30")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNoContent();
    }

    @Test
    void deletePatient_WhenDeleteById_ThenReturnForbiddenAndStatusCode403(){
        StandardError response = testClient.delete().uri("/api/v1/patients/30")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isEqualTo(403).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void deletePatient_WWhenDeleteByIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.delete().uri("/api/v1/patients/304534")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isEqualTo(404).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }
}
