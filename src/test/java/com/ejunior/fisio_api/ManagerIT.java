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
@Sql(scripts = "/sql/managers/managers-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/managers/managers-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ManagerIT {

    @Autowired
    private WebTestClient testClient;

    @Test
    void managerCreate_WhenUserValid_ThenReturnManagerCreated() {
        ManagerResponseDto response = testClient.post().uri("/api/v1/managers")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ManagerCreateDto("Ana Silva","48492635851", "Gerente", 10000.00))
                .exchange().expectStatus().isCreated().expectBody(ManagerResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Ana Silva");
    }

    @Test
    void managerCreate_WhenNameNotValid_ThenReturnStandardErrorAndStatusCode422(){
        StandardError response = testClient.post().uri("/api/v1/managers")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ManagerCreateDto("An","48492635851", "Gerente", 10000.00))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = testClient.post().uri("/api/v1/managers")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ManagerCreateDto("","48492635851", "Gerente", 10000.00))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void managerCreate_WhenCpfNotValid_ThenReturnStandardErrorAndStatusCode422(){
        StandardError response = testClient.post().uri("/api/v1/managers")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ManagerCreateDto("Ana Silva","4849263585143242323423", "Gerente", 10000.00))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = testClient.post().uri("/api/v1/managers")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ManagerCreateDto("Ana Silva","2314", "Gerente", 10000.00))
                .exchange().expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

    }

    @Test
    void managerCreate_WhenCpfAlreadyExistsInSystem_ThenReturnStandardErrorAndStatusCode409(){
        StandardError response = testClient.post().uri("/api/v1/managers")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ManagerCreateDto("Ana silva","71288012004", "Gerente", 10000.00))
                .exchange().expectStatus().isEqualTo(409).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    void managerCreate_WhenUserWithoutPermission_ThenReturnStandardErrorAndStatusCode403(){
        StandardError response = testClient.post().uri("/api/v1/managers")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ManagerCreateDto("Ana silva","71288012004", "Gerente", 10000.00))
                .exchange().expectStatus().isEqualTo(409).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    void findManager_WhenFindById_ThenReturnManagerAndStatus200(){
        ManagerResponseDto response = testClient.get().uri("/api/v1/managers/20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(ManagerResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Maria Silva");
    }

    @Test
    void findManager_WhenFindByIdUserWithoutPermission_ThenReturnExceptionAndStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/managers/20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"juvaneide@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }
    @Test
    void findManager_WhenFindById_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/managers/3000")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findManager_WhenFindByCpf_ThenReturnManagerAndStatus200(){
        ManagerResponseDto response = testClient.get().uri("/api/v1/managers/cpf/27343573055")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(ManagerResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Maria Silva");
    }
    @Test
    void findManager_WhenFindByCpfUserWithoutPermission_ThenReturnExceptionAndStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/managers/27343573055")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"juvaneide@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findManager_WhenFindByCpf_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/managers/273435730576")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findManager_WhenFindAll_ThenReturnManagersList(){
        List<ManagerResponseDto> response = testClient.get().uri("/api/v1/managers")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBodyList(ManagerResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(3);

    }

    @Test
    void findManager_WhenFindAllUserWithoutPermission_ThenReturnExceptionAndStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/managers")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"juvaneide@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findManager_WhenFindDetailsWithAuthenticatedUser_ThenReturnDetailsManagers(){
        ManagerResponseDto response = testClient.get().uri("/api/v1/managers/details")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"maria@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(ManagerResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getName()).isEqualTo("Maria Silva");
    }

    @Test
    void updateManager_WhenUpdateSalary_ThenReturnManagerUpdatedAndStatus204(){
        testClient.patch().uri("/api/v1/managers/update-salary/20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ManagerUpdateSalaryDto(15000.00))
                .exchange().expectStatus().isNoContent();
    }

    @Test
    void updateManager_WhenUpdateSalary_ThenReturnForbiddenAndStatusCode403(){
        StandardError response = testClient.patch().uri("/api/v1/managers/update-salary/20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"juvaneide@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ManagerUpdateSalaryDto(15000.00))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void updateManager_WhenUpdatePosition_ThenReturnManagerUpdatedAndStatus204(){
        testClient.patch().uri("/api/v1/managers/update-position/20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ManagerUpdatePositionDto("Diretor"))
                .exchange().expectStatus().isNoContent();
    }

    @Test
    void updateManager_WhenUpdatePosition_ThenReturnForbiddenAndStatusCode403(){
        StandardError response = testClient.patch().uri("/api/v1/managers/update-position/20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"juvaneide@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ManagerUpdatePositionDto("Diretor"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void deleteManager_WhenDeleteById_ThenReturnManagerDeletedAndStatus204(){
        testClient.delete().uri("/api/v1/managers/20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .exchange().expectStatus().isNoContent();
    }

    @Test
    void deleteManager_WhenDeleteById_ThenReturnForbiddenAndStatusCode403(){
        StandardError response = testClient.delete().uri("/api/v1/managers/20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"juvaneide@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void deleteManager_WhenDeleteByIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.delete().uri("/api/v1/managers/20232")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .exchange().expectStatus().isNotFound().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

}
