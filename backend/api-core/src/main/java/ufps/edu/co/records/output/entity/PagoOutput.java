package ufps.edu.co.records.output.entity;

import java.math.BigDecimal;

import lombok.Builder;
import ufps.edu.co.records.OutputResponse;

@Builder
public record PagoOutput(
                Integer id,
                Integer idAspirante,
                Integer idEstado,
                Integer idPagoconcepto,
                BigDecimal valorPagoPesos,
                AspiranteOutput aspirante,
                EstadoOutput estado,
                PagoconceptoOutput pagoconcepto) implements OutputResponse {
}