package com.drodrigues17.batch.batch;

import com.drodrigues17.batch.dto.ProdutoDTO;
import com.drodrigues17.batch.model.Produto;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ProdutoJobConfig {

  private final EntityManagerFactory entityManagerFactory;
  private final ProdutoProcessor produtoProcessor;
  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;

  @Bean
  public Step salvarDoArquivoNoBanco() {
    return new StepBuilder("salvarDoArquivoNoBanco", jobRepository)
        .<ProdutoDTO, Produto>chunk(10, transactionManager)
        .reader(produtoFileReader())
        .processor(produtoProcessor)
        .writer(produtoJpaItemWriter())
        .build();
  }

  @Bean
  public Job importarProdutos() {
    return new JobBuilder("importarProdutos", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(salvarDoArquivoNoBanco())
        .build();
  }

  @Bean
  public FlatFileItemReader<ProdutoDTO> produtoFileReader() {
    return new FlatFileItemReaderBuilder<ProdutoDTO>()
        .resource(new ClassPathResource("/data/lista-itens-loja-spring-batch.csv"))
        .name("produtoFileReader")
        .delimited()
        .delimiter(",")
        .names("nome", "preco", "fabricante", "tipoProduto")
        .linesToSkip(1)
        .targetType(ProdutoDTO.class)
        .build();
  }

  @Bean
  public JpaItemWriter<Produto> produtoJpaItemWriter() {
    return new JpaItemWriterBuilder<Produto>()
        .entityManagerFactory(entityManagerFactory)
        .build();
  }

}
