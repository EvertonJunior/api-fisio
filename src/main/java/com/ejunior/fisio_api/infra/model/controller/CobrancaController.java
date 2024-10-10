package com.ejunior.fisio_api.infra.model.controller;

import com.ejunior.fisio_api.infra.model.SlipRegistered;
import com.ejunior.fisio_api.infra.model.input.InvoiceInput;
import lombok.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@Getter @Setter
public class CobrancaController {

    private RestTemplate restTemplate = new RestTemplate();

    public SlipRegistered register(InvoiceInput invoiceInput, String token, String key){
        URI uri = URI.create("https://api.hm.bb.com.br/cobrancas/v2/boletos");
        var uriBuilder = UriComponentsBuilder.fromUri(uri);
        uriBuilder.queryParam("gw-dev-app-key", key);
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+token);
        var request = new HttpEntity<>(invoiceInput, headers);
        return restTemplate.postForObject(uriBuilder.build().toUri(), request, SlipRegistered.class);
    }

}
