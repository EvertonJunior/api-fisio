package com.ejunior.fisio_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Embeddable
public class Address {

    @Column(name = "address_street")
    private String Street;
    @Column(name = "address_number")
    private String number;
    @Column(name = "address_complement")
    private String complement;
    @Column(name = "address_district")
    private String district;
    @Column(name = "address_city")
    private String city;
    @Column(name = "address_uf")
    private String uf;
    @Column(name = "address_cep")
    private String cep;
    
}
