package com.drodrigues17.batch.batch;

import com.drodrigues17.batch.dto.ProdutoDTO;
import com.drodrigues17.batch.mapper.ProdutoMapper;
import com.drodrigues17.batch.model.Produto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ProdutoProcessor implements ItemProcessor<ProdutoDTO, Produto> {
  @Override
  public Produto process(ProdutoDTO produtoDTO) {

    log.info("processando produto --------------> " + produtoDTO.toString());

    return ProdutoMapper.toProduto(produtoDTO);
  }
}
