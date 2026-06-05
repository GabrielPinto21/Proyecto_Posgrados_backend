package ufps.edu.co.rest.dto;

import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UltimocodigoprogramaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer idPrograma;
    private Integer codigo;
}
