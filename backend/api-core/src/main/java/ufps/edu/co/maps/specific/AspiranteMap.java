package ufps.edu.co.maps.specific;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import ufps.edu.co.maps.GlobalMapper;
import ufps.edu.co.records.input.entity.AspiranteInput.ASPIRANTE_CREATE;
import ufps.edu.co.records.input.entity.AspiranteInput.ASPIRANTE_DELETE;
import ufps.edu.co.records.input.entity.AspiranteInput.ASPIRANTE_FIND;
import ufps.edu.co.records.input.entity.AspiranteInput.ASPIRANTE_PATCH;
import ufps.edu.co.records.input.entity.AspiranteInput.ASPIRANTE_UPDATE;
import ufps.edu.co.records.output.entity.AspiranteOutput;
import ufps.edu.co.rest.dto.AspiranteDTO;

@Component
public class AspiranteMap extends
        GlobalMapper<ASPIRANTE_CREATE, ASPIRANTE_UPDATE, ASPIRANTE_DELETE, ASPIRANTE_PATCH, ASPIRANTE_FIND, AspiranteOutput, AspiranteDTO> {

    @Autowired
    private PersonaMap personaMap;

    @Autowired
    private EstadoMap estadoMap;

    @Autowired
    private CohorteMap cohorteMap;

    @Autowired
    private CalificacioncriterioMap calificacioncriterioMap;

    public AspiranteMap() {
        super(ASPIRANTE_CREATE.class, ASPIRANTE_UPDATE.class, ASPIRANTE_DELETE.class, ASPIRANTE_PATCH.class,
                ASPIRANTE_FIND.class);
    }

    @Override
    protected AspiranteDTO toDtoCreate(ASPIRANTE_CREATE input) {
        AspiranteDTO dto = new AspiranteDTO();
        dto.setPuntuacion(input.puntuacion());
        dto.setIdCohorte(input.idCohorte());
        dto.setIdEstado(input.idEstado());
        dto.setIdPersona(input.idPersona());
        dto.setIdTipovinculacion(input.idTipovinculacion());
        return dto;
    }

    @Override
    protected AspiranteDTO toDtoUpdate(ASPIRANTE_UPDATE input) {
        AspiranteDTO dto = new AspiranteDTO();
        dto.setId(input.id());
        dto.setPuntuacion(input.puntuacion());
        dto.setIdCohorte(input.idCohorte());
        dto.setIdEstado(input.idEstado());
        dto.setIdPersona(input.idPersona());
        dto.setIdTipovinculacion(input.idTipovinculacion());
        return dto;
    }

    @Override
    protected AspiranteDTO toDtoDelete(ASPIRANTE_DELETE input) {
        AspiranteDTO dto = new AspiranteDTO();
        dto.setId(input.id());
        return dto;
    }

    @Override
    protected AspiranteDTO toDtoPatch(ASPIRANTE_PATCH input) {
        AspiranteDTO.AspiranteDTOBuilder builder = AspiranteDTO.builder()
                .id(input.id());
        if (input.puntuacion() != null) {
            builder.puntuacion(input.puntuacion());
        }
        if (input.idCohorte() != null) {
            builder.idCohorte(input.idCohorte());
        }
        if (input.idEstado() != null) {
            builder.idEstado(input.idEstado());
        }
        if (input.idPersona() != null) {
            builder.idPersona(input.idPersona());
        }
        if (input.idTipovinculacion() != null) {
            builder.idTipovinculacion(input.idTipovinculacion());
        }
        return builder.build();
    }

    @Override
    protected AspiranteDTO toDtoFind(ASPIRANTE_FIND input) {
        AspiranteDTO dto = new AspiranteDTO();
        dto.setId(input.id());
        return dto;
    }

    @Override
    public AspiranteOutput toOutput(AspiranteDTO dto) {
        if (dto == null) return null;

        return AspiranteOutput.builder()
                .id(dto.getId())
                .puntuacion(dto.getPuntuacion())
                .idCohorte(dto.getIdCohorte())
                .idEstado(dto.getIdEstado())
                .idPersona(dto.getIdPersona())
                .idTipovinculacion(dto.getIdTipovinculacion())
                .persona(dto.getPersona() != null ? personaMap.toOutput(dto.getPersona()) : null)
                .estado(dto.getEstado() != null ? estadoMap.toOutput(dto.getEstado()) : null)
                .cohorte(dto.getCohorte() != null ? cohorteMap.toOutput(dto.getCohorte()) : null)
                .calificacioncriterioList(dto.getCalificacioncriterioList() != null ? calificacioncriterioMap.toOutputList(dto.getCalificacioncriterioList()) : null)
                .build();
    }

    public List<AspiranteOutput> toOutputList(List<AspiranteDTO> dtoList) {
        return dtoList.stream().map(this::toOutput).toList();
    }
}
