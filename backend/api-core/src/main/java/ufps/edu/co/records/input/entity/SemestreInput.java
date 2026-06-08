package ufps.edu.co.records.input.entity;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ufps.edu.co.records.contracts.*;

public enum SemestreInput {
        ;
        
        public record SEMESTRE_CREATE(
                        @NotNull LocalDate fechaInicio,
                        @NotNull LocalDate fechaFin,
                        @NotNull Integer idEstado,
                        @NotBlank String nombre) implements CreateType {
        }

        public record SEMESTRE_UPDATE(
                        @NotNull Integer id,
                        @NotNull LocalDate fechaInicio,
                        @NotNull LocalDate fechaFin,
                        @NotNull Integer idEstado,
                        @NotBlank String nombre) implements UpdateType {
        }

        public record SEMESTRE_PATCH(
                        @NotNull Integer id,
                        LocalDate fechaInicio,
                        LocalDate fechaFin,
                        Integer idEstado,
                        String nombre) implements PatchType {
        }

        public record SEMESTRE_DELETE(
                        @NotNull Integer id) implements DeleteType {
        }

        public record SEMESTRE_FIND(
                        @NotNull Integer id) implements FindType {
        }
}
