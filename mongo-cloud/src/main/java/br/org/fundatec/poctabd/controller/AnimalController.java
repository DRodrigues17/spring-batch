package br.org.fundatec.poctabd.controller;

import br.org.fundatec.poctabd.dto.AnimalRequest;
import br.org.fundatec.poctabd.dto.AnimalResponse;
import br.org.fundatec.poctabd.model.Animal;
import br.org.fundatec.poctabd.repository.AnimalRepository;
import br.org.fundatec.poctabd.service.AnimalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@RestController
@RequestMapping("/animais")
@RequiredArgsConstructor
public class AnimalController {
    private final AnimalRepository repository;
    private final AnimalService service;

    @GetMapping
    public ResponseEntity<List<AnimalResponse>> listarAnimais() {
        return ResponseEntity.ok(service.listarAnimais());
    }

    @Transactional
    @PostMapping("/save")
    public ResponseEntity salvarAnimal(@Valid @RequestBody AnimalRequest animal, UriComponentsBuilder uriComponentsBuilder) {
        var uri = uriComponentsBuilder
                .path("/animais/save/{nome}")
                .buildAndExpand(animal.nome())
                .toUri();
        return ResponseEntity.created(uri).body(service.salvarAnimal(animal));
    }

    @PutMapping("/update/{idExterno}")
    public ResponseEntity<Animal> updateVideo(@PathVariable("idExterno") int idExterno, @Valid  @RequestBody AnimalRequest animal) {

        return ResponseEntity.ok(service.updateAnimal(idExterno, animal));

    }


    @DeleteMapping("/delete/{idExterno}")
    public ResponseEntity deleteAlunoByName(@PathVariable("idExterno") int idExterno) {
        service.deletarAnimal(idExterno);
        return ResponseEntity.ok(
                "animal deletado com sucesso"
        );
    }
}
