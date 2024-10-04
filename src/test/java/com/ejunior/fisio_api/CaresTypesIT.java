package com.ejunior.fisio_api;

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
@Sql(scripts = "/sql/care-types/care-types-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/care-types/care-types-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CaresTypesIT {

    @Autowired
    private WebTestClient testClient;

    @Test
    void careTypeCreate_whenCreateWithUserAuthenticated_ThenReturnSavedNewCareType() {
        CareTypeResponseDto response = testClient.post().uri("/api/v1/care-types")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CareTypeCreateDto("A-12","Care 4",200.00))
                .exchange().expectStatus().isCreated().expectBody(CareTypeResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Care 4");
        Assertions.assertThat(response.getCode()).isEqualTo("A-12");
        Assertions.assertThat(response.getPrice()).isEqualTo(200.00);
    }

    @Test
    void careTypeCreate_WhenCodeNotValid_ThenReturnStandardErrorAndStatusCode422(){
        StandardError response = testClient.post().uri("/api/v1/care-types")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CareTypeCreateDto("A-1","Care 4",200.00))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = testClient.post().uri("/api/v1/care-types")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CareTypeCreateDto("A-13232","Care 4",200.00))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void careTypeCreate_WhenAlreadyCodeRegistered_ThenReturnStandardErrorAndStatusCode409(){
        StandardError response = testClient.post().uri("/api/v1/care-types")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CareTypeCreateDto("A-14","Care 9",200.00))
                .exchange().expectStatus().isEqualTo(409).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    void careTypeCreate_WhenNameNotValid_ThenReturnStandardErrorAndStatusCode422(){
        StandardError response = testClient.post().uri("/api/v1/care-types")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CareTypeCreateDto("A-1","C",200.00))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void careTypeCreate_WhenUserWithoutPermission_ThenReturnStandardErrorAndStatusCode403(){
        StandardError response = testClient.post().uri("/api/v1/care-types")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CareTypeCreateDto("A-10","Care 20",200.00))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findCareType_WhenFindById_ThenReturnCareTypeAndStatus200(){
        CareTypeResponseDto response = testClient.get().uri("/api/v1/care-types/20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(CareTypeResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Care 1");
        Assertions.assertThat(response.getCode()).isEqualTo("A-13");
        Assertions.assertThat(response.getPrice()).isEqualTo(10.00);
    }

    @Test
    void findCareType_WhenFindByIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/care-types/3232")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findCareType_WhenFindAll_ThenReturnListCaresTypesAndStatus200(){
        List<CareTypeResponseDto> response = testClient.get().uri("/api/v1/care-types")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBodyList(CareTypeResponseDto.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(4);
        Assertions.assertThat(response.getFirst().getCode()).isEqualTo("A-13");
    }

    @Test
    void updateCareTypePrice_WhenUpdateById_ThenReturnCareTypeUpdatedAndStatus204(){
        testClient.patch().uri("/api/v1/care-types/20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CareTypeUpdateDto(200.00)).exchange().expectStatus().isNoContent();
    }

    @Test
    void updateCareTypePrice_WhenUpdateById_ThenReturnForbiddenAndStatusCode403(){
        StandardError response = testClient.patch().uri("/api/v1/care-types/20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CareTypeUpdateDto(200.00))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void deleteCareType_WhenDeleteById_ThenReturnCareTypeDeletedAndStatus204(){
        testClient.delete().uri("/api/v1/care-types/20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNoContent();
    }

    @Test
    void deleteCareType_WhenDeleteById_ThenReturnForbiddenAndStatusCode403(){
        StandardError response = testClient.delete().uri("/api/v1/care-types/20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }
    @Test
    void deleteCareType_WhenDeleteByIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.delete().uri("/api/v1/care-types/203232")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class).returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

}
