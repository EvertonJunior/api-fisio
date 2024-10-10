package com.ejunior.fisio_api.infra.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenBB {

    private String access_token;
    private String token_type;
    private String expires_in;
}
