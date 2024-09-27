package com.ejunior.fisio_api;


import com.ejunior.fisio_api.web.dtos.UserCreateDto;
import com.ejunior.fisio_api.web.dtos.UserPasswordDto;
import com.ejunior.fisio_api.web.dtos.UserResponseDto;
import com.ejunior.fisio_api.web.exceptions.StandardError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.assertj.core.api.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {

    @Autowired
    private WebTestClient testClient;

    @Test
    void userObject_whenCreate_ThenReturnSavedNewUser(){
        UserResponseDto response = testClient.post().uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("teste@email.com", "123456")).exchange()
                .expectStatus().isCreated().expectBody(UserResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUsername()).isEqualTo("teste@email.com");
    }

    @Test
    void userObject_whenCreateWithRoleFisio_ThenReturnStatusCode403(){
         StandardError response = testClient.post().uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("teste@email.com", "123456")).exchange()
                .expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void userObject_whenCreate_ThenReturnExistingUserException(){
        StandardError response = testClient.post().uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("maria@email.com", "123456")).exchange()
                .expectStatus().isEqualTo(409).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    void userObject_whenCreateWithInvalidCharactersInEmail_ThenReturnMethodArgumentNotValidException(){
        StandardError response = testClient.post().uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("email.com", "123456")).exchange()
                .expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void userObject_whenCreateWithInvalidPasswordCharacters_ThenReturnMethodArgumentNotValidException(){
        StandardError response = testClient.post().uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("teste@email.com", "123")).exchange()
                .expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void userObject_whenFindById_ThenReturnUserObject(){
        UserResponseDto response = testClient.get().uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk().expectBody(UserResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUsername()).isEqualTo("ana@email.com");

        response = testClient.get().uri("/api/v1/users/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk().expectBody(UserResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUsername()).isEqualTo("maria@email.com");

        response = testClient.get().uri("/api/v1/users/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange()
                .expectStatus().isOk().expectBody(UserResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUsername()).isEqualTo("maria@email.com");
    }

    @Test
    void userObject_whenFindByIdUserWithoutPermission_ThenReturnStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/users/102")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void userObject_whenFindById_ThenReturnNotFoundException(){
        StandardError response = testClient.get().uri("/api/v1/users/500")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void userObject_whenFindAll_ThenReturnUsersList(){
        List<UserResponseDto> response = testClient.get().uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectBodyList(UserResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(3);
        Assertions.assertThat(response.getFirst().getUsername()).isEqualTo("ana@email.com");
    }

    @Test
    void userObject_whenFindAllUserWithoutPermission_ThenReturnStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void userObject_whenUpdatePassword_ThenReturnUpdatedPasswordAndStatusNoContent(){
        testClient.patch().uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("123456","123456","123456"))
                .exchange().expectStatus().isNoContent();

        testClient.patch().uri("/api/v1/users/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("123456","123456","123456"))
                .exchange().expectStatus().isNoContent();
    }

    @Test
    void userObject_whenUpdateUserWithoutPermission_ThenReturnStatusCode403(){
        StandardError response=  testClient.patch().uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("123456","654321","654321"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void userObject_whenUpdatePasswordWhitInvalidPassword_ThenReturnInvalidPasswordException(){
       StandardError response=  testClient.patch().uri("/api/v1/users/100")
               .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
               .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("43434","654321","654321"))
                .exchange().expectStatus().isBadRequest().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void userObject_whenUpdatePasswordWhitNewPasswordDivergent_ThenReturnInvalidPasswordException(){
        StandardError response=  testClient.patch().uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("123456","323234","654321"))
                .exchange().expectStatus().isBadRequest().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void userObject_whenDeleteById_ThenReturnDeletedUserAndStatusNoContent() {
        testClient.delete().uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNoContent();
    }

    @Test
    void userObject_whenDeleteByIdUserWithoutPermission_ThenReturnStatusCode403() {
        StandardError response=  testClient.delete().uri("/api/v1/users/102")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }
}
