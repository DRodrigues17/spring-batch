package com.drodrigues17.springbatch.integration;

import lombok.Setter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

@Component
@Setter
public class FileMessageToJobRequest {
  private Job job;
  private String nomeArquivo = "input.file.name";

  @Transformer
  public JobLaunchRequest jobLaunchRequest(Message<File> mensagemArquivo) {
    var jobParameters = new JobParametersBuilder();
    jobParameters.addString(nomeArquivo, mensagemArquivo.getPayload().getAbsolutePath());
    jobParameters.addDate("unicidade", new Date());
    return new JobLaunchRequest(job, jobParameters.toJobParameters());
  }
}
