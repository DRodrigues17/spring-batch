package com.daniel.pocredis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Entity
@Table(name = "codigo_acao")
public class Acao  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @Column(unique = true, length = 4)
    private String codigo;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "acao_resultado", joinColumns = {
            @JoinColumn(name = "codigo_acao_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "codigo_resultado_id", referencedColumnName = "id")})
    private Set<Resultado> codigoResultados;

}
