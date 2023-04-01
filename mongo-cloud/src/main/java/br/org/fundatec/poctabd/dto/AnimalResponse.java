package br.org.fundatec.poctabd.dto;

public record AnimalResponse(
        int idExterno,
        String nome,
        double pesoEmKilogramas,
        double alturaEmMetros
) {
}
