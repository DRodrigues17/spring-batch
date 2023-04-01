package br.org.fundatec.poctabd.dto.converter;

import br.org.fundatec.poctabd.dto.AnimalRequest;
import br.org.fundatec.poctabd.dto.AnimalResponse;
import br.org.fundatec.poctabd.model.Animal;

public class AnimalConverter {

    public static AnimalResponse convertToDTO(Animal animal) {
        return new AnimalResponse(
                animal.getIdExterno(),
                animal.getNome(),
                animal.getPesoEmKilogramas(),
                animal.getAlturaEmMetros()
        );
    }


    public static Animal convertToEntity(AnimalRequest animalRequest) {
        return Animal.builder()
                .idExterno(animalRequest.idExterno())
                .nome(animalRequest.nome())
                .pesoEmKilogramas(animalRequest.pesoEmKilogramas())
                .alturaEmMetros(animalRequest.alturaEmMetros())
                .build();
    }
}
