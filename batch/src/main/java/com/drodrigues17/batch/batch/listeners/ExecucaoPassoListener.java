package com.drodrigues17.batch.batch.listeners;

import com.drodrigues17.batch.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

/**
 * A stepExecution que estamos recebendo nesse método seria uma representação em forma de entidade
 * da etapa que está sendo executada no job, se você olhar na classe ou na tabela no banco verá as informações
 * sobre a execução da etapa. Isso seria uma espécie de repository que o spring nos oferece por padrão para termos
 * informações salvas sobre as execuções de tarefas e suas etapas.
 * <p>
 * <p>
 * O spring batch também nos propõe a oportunidade de implementar um listener customizado baseado na interface
 * StepExecutionListener, onde como no exemplo abaixo, temos métodos para executar antes e depois da execução da etapa.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExecucaoPassoListener implements StepExecutionListener {

  private final ProdutoRepository produtoRepository;


  @Override
  public void beforeStep(StepExecution stepExecution) {
    produtoRepository.deleteAll();
    log.info("todos os dados anteriores a essa etapa foram apagados");
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {

    if (ExitStatus.COMPLETED.equals(stepExecution.getExitStatus())) {
      log.info("step terminado com sucesso ");
    }
    log.info("o step foi finalizado com o status {}", stepExecution.getExitStatus().getExitCode());


    return stepExecution.getExitStatus();
  }
}
