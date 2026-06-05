package ufps.edu.co.maps.specific;

import java.util.List;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import ufps.edu.co.maps.GlobalMapper;
import ufps.edu.co.rest.dto.*;
import ufps.edu.co.records.input.entity.CohorteInput.*;
import ufps.edu.co.records.output.entity.CohorteOutput;
import ufps.edu.co.records.output.entity.ModalidadOutput;

@Component
public class CohorteMap extends
        GlobalMapper<COHORTE_CREATE, COHORTE_UPDATE, COHORTE_DELETE, COHORTE_PATCH, COHORTE_FIND, CohorteOutput, CohorteDTO> {

    @Autowired
    private ModalidadMap modalidadMap;

    @Autowired
    private EstadoMap estadoMap;

    @Autowired
    private PlazoMap plazoMap;

    @Autowired
    private ProgramaMap programaMap;

    @Autowired
    private SemestreMap semestreMap;

    @Override
    protected CohorteDTO toDtoCreate(COHORTE_CREATE input) {
        return CohorteDTO.builder()
                .nombre(input.nombre())
                .cupos(input.cupos())
                .idEstado(input.idEstado())
                .idSemestre(input.idSemestre())
                .idModalidad(input.idModalidad())
                .idPlazodocumentacion(input.idPlazodocumentacion())
                .idPlazoinscripcion(input.idPlazoinscripcion())
                .idPlazopago(input.idPlazopago())
                .idPrograma(input.idPrograma())
                .build();
    }

    @Override
    protected CohorteDTO toDtoUpdate(COHORTE_UPDATE input) {
        return CohorteDTO.builder()
                .id(input.id())
                .nombre(input.nombre())
                .cupos(input.cupos())
                .idEstado(input.idEstado())
                .idSemestre(input.idSemestre())
                .idModalidad(input.idModalidad())
                .idPlazodocumentacion(input.idPlazodocumentacion())
                .idPlazoinscripcion(input.idPlazoinscripcion())
                .idPlazopago(input.idPlazopago())
                .idPrograma(input.idPrograma())
                .build();
    }

    @Override
    protected CohorteDTO toDtoDelete(COHORTE_DELETE input) {
        return CohorteDTO.builder()
                .id(input.id())
                .build();
    }

    @Override
    protected CohorteDTO toDtoPatch(COHORTE_PATCH input) {
        return CohorteDTO.builder()
                .id(input.id())
                .nombre(input.nombre())
                .cupos(input.cupos())
                .idEstado(input.idEstado())
                .idSemestre(input.idSemestre())
                .idModalidad(input.idModalidad())
                .idPlazodocumentacion(input.idPlazodocumentacion())
                .idPlazoinscripcion(input.idPlazoinscripcion())
                .idPlazopago(input.idPlazopago())
                .idPrograma(input.idPrograma())
                .build();
    }

    @Override
    protected CohorteDTO toDtoFind(COHORTE_FIND input) {
        return CohorteDTO.builder()
                .id(input.id())
                .build();
    }

    @Override
    public CohorteOutput toOutput(CohorteDTO dto) {
        if (dto != null) {
            return CohorteOutput.builder()
                    .id(dto.getId())
                    .nombre(dto.getNombre())
                    .cupos(dto.getCupos())
                    .idEstado(dto.getIdEstado())
                    .idSemestre(dto.getIdSemestre())
                    .idModalidad(dto.getIdModalidad())
                    .idPlazodocumentacion(dto.getIdPlazodocumentacion())
                    .idPlazoinscripcion(dto.getIdPlazoinscripcion())
                    .idPlazopago(dto.getIdPlazopago())
                    .idPrograma(dto.getIdPrograma())
                    .estado(dto.getEstado() != null ? (
                            estadoMap.toOutput(dto.getEstado())
                        ) : null)
                    .semestre(dto.getSemestre() != null ? (
                            semestreMap.toOutput(dto.getSemestre())
                        ) : null)
                    .modalidad(dto.getModalidad() != null ? (
                            aux_Modalidad_cutCohortes(dto.getModalidad())
                    ) : null)
                    .plazodocumentacion(dto.getPlazo() != null ? (
                            plazoMap.toOutput(dto.getPlazo())
                        ) : null)
                    .plazoinscripcion(dto.getPlazo2() != null ? (
                            plazoMap.toOutput(dto.getPlazo2())
                        ) : null)
                    .plazopago(dto.getPlazo3() != null ? (
                            plazoMap.toOutput(dto.getPlazo3())
                        ) : null)
                    .programa(dto.getPrograma() != null ? (
                            programaMap.toOutput(dto.getPrograma())
                        ) : null)
                    .build();
        }
        return null;
    }

    public List<CohorteOutput> toOutputList(List<CohorteDTO> dtoList) {
        return dtoList.stream()
                .map(this::toOutput)
                .toList();
    }

    private ModalidadOutput aux_Modalidad_cutCohortes(ModalidadDTO modalidadDTO) {
        modalidadDTO.setCohorteList(null);
        return modalidadMap.toOutput(modalidadDTO);
    }
}
