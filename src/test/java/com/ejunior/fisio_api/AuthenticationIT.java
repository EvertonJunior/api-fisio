package com.ejunior.fisio_api;


import com.ejunior.fisio_api.jwt.JwtToken;
import com.ejunior.fisio_api.web.dtos.UserLoginDto;
import com.ejunior.fisio_api.web.exceptions.StandardError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthenticationIT {

    @Autowired
    private WebTestClient testClient;


    @Test
    void authenticate_WithValidCredentials_ReturnTokenWithStatus200() {
        JwtToken response = testClient.post().uri("/api/v1/auth").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginDto("ana@email.com", "123456")).exchange()
                .expectStatus().isOk().expectBody(JwtToken.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    public void authenticate_WithInValidCredentials_ReturnStandardErrorStatus400() {
        StandardError response = testClient.post().uri("/api/v1/auth").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginDto("ana@email.com", "12345689")).exchange()
                .expectStatus().isBadRequest().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);

        response = testClient.post().uri("/api/v1/auth").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginDto("an@email.com", "123456")).exchange()
                .expectStatus().isBadRequest().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void authenticate_WithInvalidUsername_ReturnStandardErrorStatus422() {
        StandardError response = testClient.post().uri("/api/v1/auth").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginDto("email.com", "123456")).exchange()
                .expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = testClient.post().uri("/api/v1/auth").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginDto("ana@", "123456")).exchange()
                .expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = testClient.post().uri("/api/v1/auth").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginDto("@email.com", "123456")).exchange()
                .expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void authenticate_WithInvalidPassword_ReturnErrorMessageStatus422() {
        StandardError response = testClient.post().uri("/api/v1/auth").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginDto("ana@email.com", "12345642342343242342342")).exchange()
                .expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = testClient.post().uri("/api/v1/auth").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginDto("ana@email.com", "12")).exchange()
                .expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = testClient.post().uri("/api/v1/auth").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginDto("ana@email.com", "")).exchange()
                .expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

}
