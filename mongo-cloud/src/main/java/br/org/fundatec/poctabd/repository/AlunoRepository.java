package br.org.fundatec.poctabd.repository;

import br.org.fundatec.poctabd.model.Aluno;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AlunoRepository extends MongoRepository<Aluno, String> {

    void deleteByName(String name);
}
