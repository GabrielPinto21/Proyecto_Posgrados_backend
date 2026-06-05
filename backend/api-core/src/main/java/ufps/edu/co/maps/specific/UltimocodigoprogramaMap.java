package ufps.edu.co.maps.specific;

import org.springframework.stereotype.*;
import ufps.edu.co.maps.*;
import ufps.edu.co.records.input.entity.UltimocodigoprogramaInput.*;
import ufps.edu.co.records.output.entity.*;
import ufps.edu.co.rest.dto.*;

@Component
public class UltimocodigoprogramaMap extends
        GlobalMapper<ULTIMOCODIGOPROGRAMA_CREATE, ULTIMOCODIGOPROGRAMA_UPDATE, ULTIMOCODIGOPROGRAMA_DELETE, ULTIMOCODIGOPROGRAMA_PATCH, ULTIMOCODIGOPROGRAMA_FIND, UltimocodigoprogramaOutput, UltimocodigoprogramaDTO> {

    public UltimocodigoprogramaMap() {
        super(ULTIMOCODIGOPROGRAMA_CREATE.class, ULTIMOCODIGOPROGRAMA_UPDATE.class,
                ULTIMOCODIGOPROGRAMA_DELETE.class, ULTIMOCODIGOPROGRAMA_PATCH.class,
                ULTIMOCODIGOPROGRAMA_FIND.class);
    }

    @Override
    protected UltimocodigoprogramaDTO toDtoCreate(ULTIMOCODIGOPROGRAMA_CREATE input) {
        return UltimocodigoprogramaDTO.builder()
                .idPrograma(input.idPrograma())
                .codigo(input.codigo())
                .build();
    }

    @Override
    protected UltimocodigoprogramaDTO toDtoUpdate(ULTIMOCODIGOPROGRAMA_UPDATE input) {
        return UltimocodigoprogramaDTO.builder()
                .id(input.id())
                .idPrograma(input.idPrograma())
                .codigo(input.codigo())
                .build();
    }

    @Override
    protected UltimocodigoprogramaDTO toDtoDelete(ULTIMOCODIGOPROGRAMA_DELETE input) {
        return UltimocodigoprogramaDTO.builder()
                .id(input.id())
                .build();
    }

    @Override
    protected UltimocodigoprogramaDTO toDtoPatch(ULTIMOCODIGOPROGRAMA_PATCH input) {
        UltimocodigoprogramaDTO.UltimocodigoprogramaDTOBuilder builder = UltimocodigoprogramaDTO.builder()
                .id(input.id());
        if (input.idPrograma() != null) builder.idPrograma(input.idPrograma());
        if (input.codigo() != null) builder.codigo(input.codigo());
        return builder.build();
    }

    @Override
    protected UltimocodigoprogramaDTO toDtoFind(ULTIMOCODIGOPROGRAMA_FIND input) {
        return UltimocodigoprogramaDTO.builder()
                .id(input.id())
                .build();
    }

    @Override
    public UltimocodigoprogramaOutput toOutput(UltimocodigoprogramaDTO dto) {
        if (dto == null) return null;
        return UltimocodigoprogramaOutput.builder()
                .id(dto.getId())
                .idPrograma(dto.getIdPrograma())
                .codigo(dto.getCodigo())
                .build();
    }
}
