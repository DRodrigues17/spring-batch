package com.drodrigues17.batch.mapper;


import com.drodrigues17.batch.dto.ProdutoDTO;
import com.drodrigues17.batch.model.Produto;

public interface ProdutoMapper {

  static Produto toProduto(ProdutoDTO produtoDTO){

    return Produto
        .builder()
        .nome(produtoDTO.getNome())
        .preco(produtoDTO.getPreco())
        .fabricante(produtoDTO.getFabricante())
        .tipoProduto(produtoDTO.getTipoProduto())
        .build();
  }
}
