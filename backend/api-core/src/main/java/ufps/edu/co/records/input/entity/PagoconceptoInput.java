package ufps.edu.co.records.input.entity;

import jakarta.validation.constraints.NotNull;
import ufps.edu.co.records.contracts.*;

public enum PagoconceptoInput {
    ;

    public record PAGOCONCEPTO_CREATE(
            @NotNull String tipo) implements CreateType {
    }

    public record PAGOCONCEPTO_UPDATE(
            @NotNull Integer id,
            @NotNull String tipo) implements UpdateType {
    }

    public record PAGOCONCEPTO_PATCH(
            @NotNull Integer id,
            @NotNull String tipo) implements PatchType {
    }

    public record PAGOCONCEPTO_DELETE(
            @NotNull Integer id) implements DeleteType {
    }

    public record PAGOCONCEPTO_FIND(
            @NotNull Integer id) implements FindType {
    }
}
