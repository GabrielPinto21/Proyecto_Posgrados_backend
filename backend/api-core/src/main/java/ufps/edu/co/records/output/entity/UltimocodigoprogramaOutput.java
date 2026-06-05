package ufps.edu.co.records.output.entity;

import lombok.*;
import ufps.edu.co.records.*;

@Builder
public record UltimocodigoprogramaOutput(
        Integer id,
        Integer idPrograma,
        String nombrePrograma,
        Integer codigo) implements OutputResponse {
}
