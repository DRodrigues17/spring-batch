package br.org.fundatec.poctabd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AnimalRequest(
        @NotNull(message = "o id é obrigatório")
        int idExterno,
        @NotBlank(message = "o nome do animal é obrigatório")
        @Size(min = 4, max = 20)
        String nome,
        @NotNull(message = "peso é obrigatório")
        double pesoEmKilogramas,
        @NotNull(message = "a altura obrigatória")
        double alturaEmMetros
) {
}
