package com.ejunior.fisio_api;

import com.ejunior.fisio_api.jwt.JwtToken;
import com.ejunior.fisio_api.web.dtos.UserLoginDto;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.util.function.Consumer;

public class JwtAuthentication {

    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient testClient, String username, String password){
        String token = testClient.post().uri("/api/v1/auth").bodyValue(new UserLoginDto(username, password)).exchange()
                .expectStatus().isOk().expectBody(JwtToken.class).returnResult().getResponseBody().getToken();
        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
