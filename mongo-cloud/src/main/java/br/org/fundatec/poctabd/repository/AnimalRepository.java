package br.org.fundatec.poctabd.repository;

import br.org.fundatec.poctabd.model.Animal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AnimalRepository extends MongoRepository<Animal, String> {
    Animal findByIdExterno(int idExterno);
    void deleteByIdExterno(int idExterno);
}
