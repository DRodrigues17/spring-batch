package com.drodrigues17.batch.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.messaging.core.DestinationResolutionException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PoliticaDeTratamento implements SkipPolicy {

  @Override
  public boolean shouldSkip(Throwable exception, long skipCount) throws SkipLimitExceededException {
/**
 * Aqui deixei um limite de vezes que ele pode ignorar os erro baixo apenas para ver o arquivo com erro cair na pasta de rro
 * */
    int limiteIgnorarException = 1;

    switch (exception) {
      case DestinationResolutionException e when skipCount <= limiteIgnorarException -> {
        log.warn("obtivemos um erro " + e.getMessage());
        return true;
      }
      case FlatFileParseException e when skipCount <= limiteIgnorarException -> {

        log.warn("o input " + e.getInput() + " causou um erro " + e.getMessage());

        return true;
      }
      default -> {
      }
    }


    return false;

  }
}
