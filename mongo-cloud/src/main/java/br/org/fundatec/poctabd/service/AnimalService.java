package br.org.fundatec.poctabd.service;

import br.org.fundatec.poctabd.dto.AnimalRequest;
import br.org.fundatec.poctabd.dto.AnimalResponse;
import br.org.fundatec.poctabd.dto.converter.AnimalConverter;
import br.org.fundatec.poctabd.model.Animal;
import br.org.fundatec.poctabd.repository.AnimalRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class AnimalService {

    private final AnimalRepository repository;

    public List<AnimalResponse> listarAnimais(){
        List<AnimalResponse> listaAnimais= repository.findAll().stream().map(AnimalConverter::convertToDTO).toList();
        if (listaAnimais.isEmpty()) throw new IllegalStateException();
        return listaAnimais;
    }

    public Animal buscarPorIdExterno(int idExterno){
        return repository.findByIdExterno(idExterno);
    }

    public Animal salvarAnimal(@Valid AnimalRequest animalRequest){
        Animal animal = AnimalConverter.convertToEntity(animalRequest);
        return repository.save(animal);
    }

    public Animal updateAnimal(int idExterno, @Valid AnimalRequest animal){
        Animal animalAtual = repository.findByIdExterno(idExterno);
        Animal animalAlterado = animalAtual;
        animalAlterado.setNome(animal.nome());
        animalAlterado.setPesoEmKilogramas(animal.pesoEmKilogramas());
        animalAlterado.setAlturaEmMetros(animal.alturaEmMetros());
        return repository.save(animalAlterado);
    }
    public void deletarAnimal(int idExterno){
        repository.deleteByIdExterno(idExterno);
    }
}
