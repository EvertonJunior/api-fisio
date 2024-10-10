package com.ejunior.fisio_api;

import com.ejunior.fisio_api.web.dtos.HospitalResponseDto;
import com.ejunior.fisio_api.web.dtos.InvoiceCreateDto;
import com.ejunior.fisio_api.web.dtos.InvoiceResponseDto;
import com.ejunior.fisio_api.web.dtos.PhysicalTherapistCreateDto;
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
@Sql(scripts = "/sql/invoices/invoices-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/invoices/invoices-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class InvoiceIT {

    @Autowired
    private WebTestClient testClient;

    @Test
    void invoiceCreate_whenCreateCodeValid_ThenReturnSavedNewInvoice(){
        InvoiceResponseDto response = testClient.post().uri("/api/v1/invoices")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new InvoiceCreateDto("2024-10-01", "2024-10-07", 12L)).exchange().
                expectStatus().isCreated().expectBody(InvoiceResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getCode()).isEqualTo("2024100120241007-12345678910113");
        Assertions.assertThat(response.getTotalValue()).isEqualTo(90.00);
    }

    @Test
    void invoiceCreate_WhenDateNotValid_ThenReturnStandardErrorAndStatusCode422(){
        StandardError response = testClient.post().uri("/api/v1/invoices")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new InvoiceCreateDto("2024-10", "2024-10-07", 11L)).exchange()
                .expectStatus().isEqualTo(422).expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = testClient.post().uri("/api/v1/invoices")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new InvoiceCreateDto("2024-10-01", "204-10", 11L)).exchange()
                .expectStatus().isEqualTo(422).expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);


    }

    @Test
    void invoiceCreate_WhenCodeAlreadyExistsInSystem_ThenReturnStandardErrorAndStatusCode409(){
        StandardError response = testClient.post().uri("/api/v1/invoices")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new InvoiceCreateDto("2024-10-01", "2024-10-07", 10L)).exchange()
                .expectStatus().isEqualTo(409).expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    void invoiceCreate_WhenUserWithoutPermission_ThenReturnStandardErrorAndStatusCode403(){
        StandardError response = testClient.post().uri("/api/v1/invoices")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new InvoiceCreateDto("2024-10-01", "2024-10-07", 10L)).exchange()
                .expectStatus().isForbidden().expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findInvoice_WhenFindById_ThenReturnInvoiceAndStatus200(){
        InvoiceResponseDto response = testClient.get().uri("/api/v1/invoices/71")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456")).exchange().
                expectStatus().isOk().expectBody(InvoiceResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getCode()).isEqualTo("2024100120241007-12345678910112");
        Assertions.assertThat(response.getTotalValue()).isEqualTo(60.00);
    }

    @Test
    void findInvoice_WhenFindByIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/invoices/74")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456")).exchange().
                expectStatus().isNotFound().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findInvoice_WhenFindById_ThenReturnForbiddenAndStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/invoices/71")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456")).exchange().
                expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findInvoice_WhenFindByHospitalId_ThenReturnInvoiceAndStatus200(){
        List<InvoiceResponseDto> response = testClient.get().uri("/api/v1/invoices/hospital/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456")).exchange().
                expectStatus().isOk().expectBodyList(InvoiceResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(2);
    }

    @Test
    void findInvoice_WhenFindByHospitalIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.get().uri("/api/v1/invoices/hospital/7423232")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456")).exchange().
                expectStatus().isNotFound().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void findInvoice_WhenFindByHospitalId_ThenReturnForbiddenAndStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/invoices/hospital/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456")).exchange().
                expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findInvoice_WhenFindAll_ThenReturnListInvoicesAndStatus200(){
        List<InvoiceResponseDto> response = testClient.get().uri("/api/v1/invoices")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456")).exchange().
                expectStatus().isOk().expectBodyList(InvoiceResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(2);
    }

    @Test
    void findInvoice_WhenFindAll_ThenReturnForbiddenAndStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/invoices")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456")).exchange().
                expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void deleteInvoice_WhenDeleteById_ThenReturnInvoiceDeletedAndStatus204(){
        testClient.delete().uri("/api/v1/invoices/70")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456")).exchange().
                expectStatus().isNoContent();
    }

    @Test
    void deleteInvoice_WhenDeleteById_ThenReturnForbiddenAndStatusCode403(){
        StandardError response = testClient.delete().uri("/api/v1/invoices/70")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456")).exchange().
                expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }
    @Test
    void deleteInvoice_WhenDeleteByIdWithIdInvalid_ThenReturnNotFoundExceptionAndStatusCode404(){
        StandardError response = testClient.delete().uri("/api/v1/invoices/75")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456")).exchange().
                expectStatus().isNotFound().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }


}
