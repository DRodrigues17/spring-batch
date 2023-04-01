package br.org.fundatec.poctabd.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Builder
@Document
public class Animal {
    private String id;
    private int idExterno;
    private String nome;
    private double pesoEmKilogramas;
    private double alturaEmMetros;
}
