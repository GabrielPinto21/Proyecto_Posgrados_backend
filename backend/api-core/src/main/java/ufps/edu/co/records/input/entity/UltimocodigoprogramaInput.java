package ufps.edu.co.records.input.entity;

import jakarta.validation.constraints.*;
import ufps.edu.co.records.contracts.*;

public enum UltimocodigoprogramaInput {
    ;

    public record ULTIMOCODIGOPROGRAMA_CREATE(
            @NotNull Integer idPrograma,
            @NotNull Integer codigo) implements CreateType {
    }

    public record ULTIMOCODIGOPROGRAMA_UPDATE(
            @NotNull Integer id,
            @NotNull Integer idPrograma,
            @NotNull Integer codigo) implements UpdateType {
    }

    public record ULTIMOCODIGOPROGRAMA_DELETE(
            @NotNull Integer id) implements DeleteType {
    }

    public record ULTIMOCODIGOPROGRAMA_PATCH(
            @NotNull Integer id,
            Integer idPrograma,
            Integer codigo) implements PatchType {
    }

    public record ULTIMOCODIGOPROGRAMA_FIND(
            @NotNull Integer id) implements FindType {
    }

    public record ULTIMOCODIGOPROGRAMA_FIND_BY_PROGRAMA(
            @NotNull Integer idPrograma) implements FindType {
    }

    public record ULTIMOCODIGOPROGRAMA_UPDATE_CODIGO(
            @NotNull Integer codigo) {
    }
}
