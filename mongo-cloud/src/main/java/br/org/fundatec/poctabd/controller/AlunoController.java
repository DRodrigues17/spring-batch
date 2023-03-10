package br.org.fundatec.poctabd.controller;

import br.org.fundatec.poctabd.model.Aluno;
import br.org.fundatec.poctabd.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {
    private final AlunoRepository repository;

    @GetMapping
    public ResponseEntity<List<Aluno>> listAlunos(){
        List<Aluno> alunos = repository.findAll();
        return ResponseEntity.ok(alunos);
    }

    @Transactional
    @PostMapping("/save")
    public ResponseEntity saveALuno(@RequestBody Aluno aluno, UriComponentsBuilder uriComponentsBuilder){
        var uri =  uriComponentsBuilder
                .path("/alunos/save/{name}")
                .buildAndExpand(aluno.getName())
                .toUri();
        return ResponseEntity.created(uri).body(repository.save(aluno));
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity deleteAlunoByName(@PathVariable("name") String name){
        repository.deleteByName(name);
        return ResponseEntity.noContent().build();
    }
}
