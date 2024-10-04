package com.ejunior.fisio_api;

import com.ejunior.fisio_api.entities.Hospital;
import com.ejunior.fisio_api.entities.PhysicalTherapist;
import com.ejunior.fisio_api.web.dtos.CareCreateDto;
import com.ejunior.fisio_api.web.dtos.CareResponseDto;
import com.ejunior.fisio_api.web.dtos.HospitalCreateDto;
import com.ejunior.fisio_api.web.dtos.HospitalResponseDto;
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
@Sql(scripts = "/sql/hospital/hospital-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/hospital/hospital-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class HospitalIT {

    @Autowired
    private WebTestClient testClient;

    @Test
    void hospitalCreate_whenCreateWithUserAuthenticated_ThenReturnSavedNewHospital() {
        HospitalResponseDto response = testClient.post().uri("/api/v1/hospitals")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new HospitalCreateDto("Santa Marcelina","58568709000178"))
                .exchange().expectStatus().isCreated().expectBody(HospitalResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Santa Marcelina");
    }

    @Test
    void hospitalCreate_WhenCnpjNotValid_ThenReturnStandardErrorAndStatusCode422(){
        StandardError response = testClient.post().uri("/api/v1/hospitals")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new HospitalCreateDto("Santa Marcelina","58568"))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = testClient.post().uri("/api/v1/hospitals")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new HospitalCreateDto("Santa Marcelina","58568423423442342323423"))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
        }

    @Test
    void hospitalCreate_WhenNameNotValid_ThenReturnStandardErrorAndStatusCode422(){
            StandardError response = testClient.post().uri("/api/v1/hospitals")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new HospitalCreateDto("Sa","58568709000178"))
                    .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult()
                    .getResponseBody();

            Assertions.assertThat(response).isNotNull();
            Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void hospitalCreate_WhenUserWithoutPermission_ThenReturnStandardErrorAndStatusCode403(){
        StandardError response = testClient.post().uri("/api/v1/hospitals")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new HospitalCreateDto("Santa Marcelina","58568709000178"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void hospitalCreate_WhenAlreadyCnpjRegistered_ThenReturnStandardErrorAndStatusCode409(){
        StandardError response = testClient.post().uri("/api/v1/hospitals")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new HospitalCreateDto("Santa Marcelina","10169408000145"))
                .exchange().expectStatus().isEqualTo(409).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    void findHospital_WhenFindById_ThenReturnHospitalAndStatus200(){
        HospitalResponseDto response = testClient.get().uri("/api/v1/hospitals/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(HospitalResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Santa Casa");

        response = testClient.get().uri("/api/v1/hospitals/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(HospitalResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Santa Casa");
    }

    @Test
    void findHospital_WhenFindByIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/hospitals/45")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findHospital_WhenFindByCnpj_ThenReturnHospitalAndStatus200(){
        HospitalResponseDto response = testClient.get().uri("/api/v1/hospitals/cnpj/10169408000145")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(HospitalResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Santa Casa");

        response = testClient.get().uri("/api/v1/hospitals/cnpj/10169408000145")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(HospitalResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Santa Casa");
    }

    @Test
    void findHospital_WhenFindByIdWithCnpjInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/hospitals/88301125000100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findHospital_WhenFindAll_ThenReturnListHospitalsAndStatus200(){
        List<HospitalResponseDto> response = testClient.get().uri("/api/v1/hospitals")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBodyList(HospitalResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(3);
        Assertions.assertThat(response.get(1).getName()).isEqualTo("Santa Marcelina");

        response = testClient.get().uri("/api/v1/hospitals")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBodyList(HospitalResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(3);
        Assertions.assertThat(response.getFirst().getName()).isEqualTo("Santa Casa");
    }

    @Test
    void deleteHospital_WhenDeleteById_ThenReturnHospitalDeletedAndStatus204(){
      testClient.delete().uri("/api/v1/hospitals/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNoContent();
    }

    @Test
    void deleteHospital_WhenDeleteById_ThenReturnForbiddenAndStatusCode403(){
        StandardError response = testClient.delete().uri("/api/v1/hospitals/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }
    @Test
    void deleteHospital_WhenDeleteByIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.delete().uri("/api/v1/hospitals/25")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

}
