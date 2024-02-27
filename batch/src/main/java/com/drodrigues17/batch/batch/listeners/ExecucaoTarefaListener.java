package com.drodrigues17.batch.batch.listeners;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

/**
 * A jobExecution que estamos recebendo nesses métodos seria uma representação em forma de entidade
 * da tarefa que está sendo executada, se você olhar na classe ou na tabela no banco verá as informações
 * sobre a execução da tarefa. Isso seria uma espécie de repository que o spring nos oferece por padrão para termos
 * informações salvas sobre as execuções de tarefas e suas etapas.
 * <p>
 * <p>
 * O spring batch também nos propõe a oportunidade de implementar um listener customizado baseado na interface
 * JobExecutionListener, onde como no exemplo abaixo, temos métodos para executar antes e depois da execução da etapa.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExecucaoTarefaListener implements JobExecutionListener {

  @Override
  public void beforeJob(JobExecution jobExecution) {
    log.info("a exeucção do job vai começar ");

    JobExecutionListener.super.beforeJob(jobExecution);
  }

  @Override
  public void afterJob(JobExecution jobExecution) {


    Map<String, JobParameter<?>> parameters = jobExecution.getJobParameters().getParameters();

    if (parameters.containsKey("input.file.name")) {

      Path caminhoAbsolutoDoArquivo = Path.of(parameters.get("input.file.name").getValue().toString());
      Path caminhoDoArquivoSemONomeDoArquivo = caminhoAbsolutoDoArquivo.getParent();

      Path pastaArquivosSucesso = Path.of(caminhoDoArquivoSemONomeDoArquivo + File.separator + "com-sucesso");
      Path pastaArquivosErro = Path.of(caminhoDoArquivoSemONomeDoArquivo + File.separator + "com-erro");

      if (ExitStatus.COMPLETED.equals(jobExecution.getExitStatus())) {

        criarPastaSeNecessario(pastaArquivosSucesso);
        moverArquivoProcessado(caminhoAbsolutoDoArquivo, pastaArquivosSucesso);

      }
      //não entendi muito bem o motivo de precisar comparar strings nesse cenário de erro, talvez seja por que uma exception é lançada nesse cenário?
      if (ExitStatus.STOPPED.getExitCode().equalsIgnoreCase(jobExecution.getExitStatus().getExitCode()) ||
          ExitStatus.FAILED.getExitCode().equalsIgnoreCase(jobExecution.getExitStatus().getExitCode())) {

        criarPastaSeNecessario(pastaArquivosErro);
        moverArquivoProcessado(caminhoAbsolutoDoArquivo, pastaArquivosErro);
      }
    }


    log.info("A tarefa finalizou com o status de " + jobExecution.getExitStatus().getExitCode());
    JobExecutionListener.super.afterJob(jobExecution);
  }

  @SneakyThrows
  void moverArquivoProcessado(Path pastaAtual, Path pastaDeDestino) {
    Path destino = pastaDeDestino.resolve(pastaAtual.getFileName());

    log.info("destino arquivo : " + destino);

    Files.move(pastaAtual, destino, StandardCopyOption.ATOMIC_MOVE);
  }

  @SneakyThrows
  void criarPastaSeNecessario(final Path caminhoDaPasta) {

    if (Files.notExists(caminhoDaPasta)) {
      log.warn("------------> criando a pasta : {}", caminhoDaPasta.toAbsolutePath());
      Files.createDirectory(caminhoDaPasta);
    }
  }
}
