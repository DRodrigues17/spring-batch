package com.drodrigues17.springbatch.batch;

import com.drodrigues17.springbatch.dto.ProdutoDTO;
import com.drodrigues17.springbatch.mapper.ProdutoMapper;
import com.drodrigues17.springbatch.model.Produto;
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
