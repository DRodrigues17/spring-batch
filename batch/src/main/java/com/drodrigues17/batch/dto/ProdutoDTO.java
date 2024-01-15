package com.drodrigues17.batch.dto;

import lombok.Data;

@Data
public class ProdutoDTO {

  private String nome;
  private Double preco;
  private String fabricante;
  private String tipoProduto;
}
