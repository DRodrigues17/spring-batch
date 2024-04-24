package com.drodrigues17.springbatch.integration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.DefaultFileNameGenerator;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Duration;

@Component
@EnableIntegration
@IntegrationComponentScan
@RequiredArgsConstructor
public class ProdutoIntegrationConfig {

  @Value("${variaveis_ambiente.arquivos_esperando_processamento}")
  private String pastaProdutos;

  private final Job importarProdutos;
  private final JobRepository jobRepository;


  @Bean
  public IntegrationFlow integrationFlow() {
    return IntegrationFlow.from(fileReadingMessageSource(),
            sourcePolling -> sourcePolling.poller(Pollers.fixedDelay(Duration.ofSeconds(5)).maxMessagesPerPoll(1)))
        .channel(fileIn())
        .handle(renomeadorArquivosEmProcessamento())
        .transform(fileMessageToJobRequest())
        .handle(jobLaunchingGateway())
        .log()
        .get();
  }


  /**
   * Esse bean será usado para criar uma mensagem a partir de um sistema de arquivos, tal como um diretório.
   * Ele vai pegar o arquivo e transformar em uma mensagem,
   * Foi adicionado um filtro, para prevenir um erro muito comum onde alguns arquivos podem não estar num estado aceitável
   * para processamento, então esse filtro vai trazer apenas os que estão prontos para leitura.
   */
  @SneakyThrows
  public FileReadingMessageSource fileReadingMessageSource() {
    var messageSource = new FileReadingMessageSource();
    messageSource.setDirectory(new ClassPathResource("arquivos-esperando-processamento").getFile());
    messageSource.setFilter(new SimplePatternFileListFilter("*.csv"));
    return messageSource;
  }

  /**
   * Um canal que invoca uma unica subscrição pra cada mensagem enviada, essa invocação irá acontecer na thread do
   * remetente.
   */
  public DirectChannel fileIn() {
    return new DirectChannel();
  }

  public FileWritingMessageHandler renomeadorArquivosEmProcessamento() {
    var handler = new FileWritingMessageHandler(new File(pastaProdutos));
    handler.setFileExistsMode(FileExistsMode.REPLACE);
    handler.setDeleteSourceFiles(true);
    handler.setFileNameGenerator(new DefaultFileNameGenerator());
    handler.setFileNameGenerator(fileNameGenerator());
    handler.setRequiresReply(false);
    return handler;
  }

  public DefaultFileNameGenerator fileNameGenerator() {
    var filenameGenerator = new DefaultFileNameGenerator();
    filenameGenerator.setExpression("payload.name + '.processando'");
    return filenameGenerator;
  }

  public FileMessageToJobRequest fileMessageToJobRequest() {
    var transformer = new FileMessageToJobRequest();
    transformer.setJob(importarProdutos);
    return transformer;
  }

  /**
   * Aqui estamos criando um job launcher
   */
  public JobLaunchingGateway jobLaunchingGateway() {
    var taskExecutorJobLauncher = new TaskExecutorJobLauncher();
    taskExecutorJobLauncher.setJobRepository(jobRepository);
    taskExecutorJobLauncher.setTaskExecutor(new SyncTaskExecutor());
    return new JobLaunchingGateway(taskExecutorJobLauncher);
  }

}
