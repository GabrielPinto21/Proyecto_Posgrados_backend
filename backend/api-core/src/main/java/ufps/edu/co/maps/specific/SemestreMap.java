package ufps.edu.co.maps.specific;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import ufps.edu.co.maps.GlobalMapper;
import ufps.edu.co.records.input.entity.SemestreInput.*;
import ufps.edu.co.records.output.entity.SemestreOutput;
import ufps.edu.co.rest.dto.SemestreDTO;

@Component
public class SemestreMap extends
        GlobalMapper<SEMESTRE_CREATE, SEMESTRE_UPDATE, SEMESTRE_DELETE, SEMESTRE_PATCH, SEMESTRE_FIND, SemestreOutput, SemestreDTO> {

    @Autowired
    private EstadoMap estadoMap;

    public SemestreMap() {
        super(SEMESTRE_CREATE.class, SEMESTRE_UPDATE.class, SEMESTRE_DELETE.class,
                SEMESTRE_PATCH.class, SEMESTRE_FIND.class);
    }

    @Override
    protected SemestreDTO toDtoCreate(SEMESTRE_CREATE input) {
        return SemestreDTO.builder()
                .nombre(input.nombre())
                .fechaInicio(input.fechaInicio())
                .fechaFin(input.fechaFin())
                .idEstado(input.idEstado())
                .build();
    }

    @Override
    protected SemestreDTO toDtoUpdate(SEMESTRE_UPDATE input) {
        return SemestreDTO.builder()
                .id(input.id())
                .nombre(input.nombre())
                .fechaInicio(input.fechaInicio())
                .fechaFin(input.fechaFin())
                .idEstado(input.idEstado())
                .build();
    }

    @Override
    protected SemestreDTO toDtoDelete(SEMESTRE_DELETE input) {
        return SemestreDTO.builder()
                .id(input.id())
                .build();
    }

    @Override
    protected SemestreDTO toDtoPatch(SEMESTRE_PATCH input) {
        return SemestreDTO.builder()
                .id(input.id())
                .nombre(input.nombre())
                .fechaInicio(input.fechaInicio())
                .fechaFin(input.fechaFin())
                .idEstado(input.idEstado())
                .build();
    }

    @Override
    protected SemestreDTO toDtoFind(SEMESTRE_FIND input) {
        return SemestreDTO.builder()
                .id(input.id())
                .build();
    }

    @Override
    public SemestreOutput toOutput(SemestreDTO dto) {
        if (dto == null)
            return null;

        return SemestreOutput.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .fechainicio(dto.getFechaInicio())
                .fechafin(dto.getFechaFin())
                .idEstado(dto.getIdEstado())
                .estado(dto.getEstado() != null ? estadoMap.toOutput(dto.getEstado()) : null)
                .build();
    }

    public List<SemestreOutput> toOutputList(List<SemestreDTO> dtoList) {
        return dtoList.stream().map(this::toOutput).toList();
    }
}