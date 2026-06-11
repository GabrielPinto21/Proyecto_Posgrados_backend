package ufps.edu.co.maps.specific;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ufps.edu.co.records.output.entity.ListaadmitidosOutput;
import ufps.edu.co.rest.dto.AdmitidoDTO;;

@Component
public class ListaadmitidosMap {

    @Autowired
    private AspiranteMap aspiranteMap;

    @Autowired
    private CohorteMap cohorteMap;

    public ListaadmitidosOutput toOutput(AdmitidoDTO dto) {
        if (dto == null) return null;

        return ListaadmitidosOutput.builder()
                .id(dto.getId())
                .fechageneracion(dto.getFechageneracion())
                .idCohorte(dto.getIdCohorte())
                .idAspirante(dto.getIdAspirante())
                .aspirante(dto.getAspirante() != null ? aspiranteMap.toOutput(dto.getAspirante()) : null)
                .cohorte(dto.getCohorte() != null ? cohorteMap.toOutput(dto.getCohorte()) : null)
                .build();
    }
}
