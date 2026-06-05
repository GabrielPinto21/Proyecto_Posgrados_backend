package ufps.edu.co.records.output.entity;

import java.time.LocalDate;

import lombok.Builder;
import ufps.edu.co.records.OutputResponse;

@Builder
public record CohorteListadoOutput(
        Integer id,
        String nombre,
        boolean activa,
        long inscritos,
        long admitidos,
        int cupos,
        LocalDate fechaLimiteDocumentos,
        LocalDate fechaLimitePago,
        // LocalDate fechaLimiteInscripcion,
        LocalDate fechaInicio
) implements OutputResponse {}
