package ufps.edu.co.records.output.entity;

import lombok.*;
import ufps.edu.co.records.*;

@Builder
public record UltimocodigoprogramaOutput(
        Integer id,
        Integer idPrograma,
        Integer codigo) implements OutputResponse {
}
