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

  /**
   * Aqui fazemos basicamente o processamento do arquivo, poderíamos ter vários processamentos, mas nesse caso para
   * abstração de complexidade deixei apenas um mapeamento de to para entidade para que o objeto seja salvo no baanco.
   * */
  @Override
  public Produto process(ProdutoDTO produtoDTO) throws InterruptedException {
    //Thread.sleep(2000);
    log.info("processando produto --------------> " + produtoDTO.toString());

    return ProdutoMapper.toProduto(produtoDTO);
  }
}
