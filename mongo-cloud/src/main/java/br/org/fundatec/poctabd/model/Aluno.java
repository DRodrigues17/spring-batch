package br.org.fundatec.poctabd.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document
public class Aluno {
    private String id;
    private String name;
    private int age;
    private boolean empregado;
}
