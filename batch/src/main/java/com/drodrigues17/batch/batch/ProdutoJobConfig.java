package com.drodrigues17.batch.batch;

import com.drodrigues17.batch.batch.listeners.ExecucaoPassoListener;
import com.drodrigues17.batch.batch.listeners.ExecucaoTarefaListener;
import com.drodrigues17.batch.dto.ProdutoDTO;
import com.drodrigues17.batch.handler.PoliticaDeTratamento;
import com.drodrigues17.batch.model.Produto;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@RequiredArgsConstructor
public class ProdutoJobConfig {

  private final EntityManagerFactory entityManagerFactory;
  private final ProdutoProcessor produtoProcessor;
  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final PoliticaDeTratamento politicaDeTratamento;
  private final ExecucaoPassoListener execucaoPassoListener;
  private final ExecucaoTarefaListener execucaoTarefaListener;

  /**
   * Aqui criamos um step (um passo) do job, que nada mais seria que uma etapa dessa tarefa, nesse step passamos coisas como
   * o reader, processor, writer... coisas necessárias para o step e que serão utilizadas na leitura do arquivo.
   */
  @Bean
  public Step salvarDoArquivoNoBanco(ItemReader<ProdutoDTO> produtoDTOReader) {
    return new StepBuilder("salvarDoArquivoNoBanco", jobRepository)
        .<ProdutoDTO, Future<Produto>>chunk(10, transactionManager)
        .reader(produtoDTOReader)
        .processor(processadorDeItensAssincrono())
        .writer(produtoJpaAsyncWriter())
        .faultTolerant()
        .skipPolicy(politicaDeTratamento)
        .listener(execucaoPassoListener)
        .taskExecutor(taskExecutor())
        .build();
  }

  /**
   *
   * Aqui criamos um job, que nada mais seria do que uma tarefa, essa em específico vai ler um arquivo e salvar suas
   * informações no banco, tal como o seu step (etapa) o faz acima */
  @Bean
  public Job importarProdutos(Step salvarDoArquivoNoBanco) {
    return new JobBuilder("importarProdutos", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(salvarDoArquivoNoBanco)
        .listener(execucaoTarefaListener)
        .build();
  }

  /**
   * Aqui definimos o reader dos arquivos, o cara responsável por ler os arquivos e transformar em objetos
   * primeiro ele pega o lugar onde está o recurso (pasta e nome do arquivo), depois definimos um nome para ele.
   * Após isso, informamos que o arquivo é delimitado e informamos qual o delimitador dos campos, nesse caso é o ponto e virgula
   * depois passamos os nomes dos campos e informamos o número de linhas queremos pular,
   * nesse caso pulamos a primeira linha que seria o cabeçalho. Por fim informamos qual o "tipo alvo" nessa desserialização, que seria o produtoDto
   * */
  @Bean
  @StepScope
  public FlatFileItemReader<ProdutoDTO> produtoFileReader(@Value("#{jobParameters['input.file.name']}") String resource) {

    return new FlatFileItemReaderBuilder<ProdutoDTO>()
        .resource(new FileSystemResource(resource))
        .name("produtoFileReader")
        .delimited()
        .delimiter(",")
        .names("nome", "preco", "fabricante", "tipoProduto")
        .linesToSkip(1)
        .targetType(ProdutoDTO.class)
        .build();
  }

  /**
   * Esse writer é responsável basicamente por salvar as entidades no banco apos o processamento
   * */
  @Bean
  public JpaItemWriter<Produto> produtoJpaItemWriter() {
    return new JpaItemWriterBuilder<Produto>()
        .entityManagerFactory(entityManagerFactory)
        .build();
  }

  /**
   * A qui o Task executor é usado para fazer com que a execução do job seja multi thread. Aqui definimos a quantidade
   * padrão e quantidade máxima de threads, fora a capacidade de objetos enfileirados por thread. Também defini um handler
   * para quando alguma execução for rejeitada, que por sua vez vai executar diretamente a task rejeitada a fim de
   * tentar fazer o processo passar para frente, a menos que o executor seja "desligado".
   * Também definimos um prefixo para nomear a thread em que os objetos estão sendo processados.
   */
  @Bean
  public TaskExecutor taskExecutor() {
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(5);
    executor.setQueueCapacity(10);
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.setThreadNamePrefix("Thread N-> :");
    return executor;
  }

  /**
   * Aqui estamos fazendo uma configuração para o processamento de itens de forma assíncrona, onde passamos um processador
   * para que ele saiba oque fazer com os objetos que receber e também podemos passar um task executor.
   */
  @Bean
  public AsyncItemProcessor<ProdutoDTO, Produto> processadorDeItensAssincrono() {
    var processadorDeItensAssincrono = new AsyncItemProcessor<ProdutoDTO, Produto>();
    processadorDeItensAssincrono.setDelegate(produtoProcessor);
    processadorDeItensAssincrono.setTaskExecutor(taskExecutor());
    return processadorDeItensAssincrono;
  }

  /**
   * Aqui criamos a classe responsável por escrever os objetos no banco de forma assíncrona.
   * */
  public AsyncItemWriter<Produto> produtoJpaAsyncWriter() {
    var asyncItemWriter = new AsyncItemWriter<Produto>();
    asyncItemWriter.setDelegate(produtoJpaItemWriter());
    return asyncItemWriter;
  }

}
