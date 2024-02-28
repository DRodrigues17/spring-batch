package com.drodrigues17.springbatch.mapper;


import com.drodrigues17.springbatch.dto.ProdutoDTO;
import com.drodrigues17.springbatch.model.Produto;

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
