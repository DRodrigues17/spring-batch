package com.daniel.webflux;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document
public class Pokemon {

    @Id
    private String id;
    private Integer idExterno;
    private String nome;
    private int altura;
    private int peso;
}
