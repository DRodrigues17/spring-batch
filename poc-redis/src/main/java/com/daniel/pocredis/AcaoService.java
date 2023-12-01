package com.daniel.pocredis;

import com.daniel.pocredis.model.Acao;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcaoService{

    private final AcaoRepository acaoRespository;

    @Cacheable("acaoCache")
    public List<Acao> buscarCombinacoes() {

        System.out.println("buscando informações no banco");
        return acaoRespository.findAll();

    }
}
