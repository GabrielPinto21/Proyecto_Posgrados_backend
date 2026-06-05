package ufps.edu.co.maps.specific;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import ufps.edu.co.maps.GlobalMapper;
import ufps.edu.co.records.input.entity.AdministrativoInput.ADMINISTRATIVO_CREATE;
import ufps.edu.co.records.input.entity.AdministrativoInput.ADMINISTRATIVO_DELETE;
import ufps.edu.co.records.input.entity.AdministrativoInput.ADMINISTRATIVO_FIND;
import ufps.edu.co.records.input.entity.AdministrativoInput.ADMINISTRATIVO_PATCH;
import ufps.edu.co.records.input.entity.AdministrativoInput.ADMINISTRATIVO_UPDATE;
import ufps.edu.co.records.output.entity.AdministrativoOutput;
import ufps.edu.co.rest.dto.AdministrativoDTO;

@Component
public class AdministrativoMap extends
        GlobalMapper<ADMINISTRATIVO_CREATE, ADMINISTRATIVO_UPDATE, ADMINISTRATIVO_DELETE, ADMINISTRATIVO_PATCH, ADMINISTRATIVO_FIND, AdministrativoOutput, AdministrativoDTO> {

    @Autowired
    private DocumentoMap documentoMap;

    @Autowired
    private PersonaMap personaMap;

    @Autowired
    private EstadoMap estadoMap;

    @Autowired
    private CargoMap cargoMap;

    public AdministrativoMap() {
        super(ADMINISTRATIVO_CREATE.class, ADMINISTRATIVO_UPDATE.class, ADMINISTRATIVO_DELETE.class,
                ADMINISTRATIVO_PATCH.class, ADMINISTRATIVO_FIND.class);
    }

    @Override
    protected AdministrativoDTO toDtoCreate(ADMINISTRATIVO_CREATE input) {
        AdministrativoDTO dto = new AdministrativoDTO();
        dto.setIdPersona(input.idPersona());
        dto.setFechainicio(input.fechainicio());
        dto.setFechasalida(input.fechasalida());
        dto.setIdEstado(input.idEstado());
        dto.setIdCargo(input.idCargo());
        return dto;
    }

    @Override
    protected AdministrativoDTO toDtoUpdate(ADMINISTRATIVO_UPDATE input) {
        AdministrativoDTO dto = new AdministrativoDTO();
        dto.setId(input.id());
        dto.setIdPersona(input.idPersona());
        dto.setFechainicio(input.fechainicio());
        dto.setFechasalida(input.fechasalida());
        dto.setIdEstado(input.idEstado());
        dto.setIdCargo(input.idCargo());
        return dto;
    }

    @Override
    protected AdministrativoDTO toDtoDelete(ADMINISTRATIVO_DELETE input) {
        AdministrativoDTO dto = new AdministrativoDTO();
        dto.setId(input.id());
        return dto;
    }

    @Override
    protected AdministrativoDTO toDtoPatch(ADMINISTRATIVO_PATCH input) {
        AdministrativoDTO dto = new AdministrativoDTO();
        dto.setId(input.id());
        if (input.idPersona() != null)
            dto.setIdPersona(input.idPersona());
        if (input.fechainicio() != null)
            dto.setFechainicio(input.fechainicio());
        if (input.fechasalida() != null)
            dto.setFechasalida(input.fechasalida());
        if (input.idEstado() != null)
            dto.setIdEstado(input.idEstado());
        if (input.idCargo() != null)
            dto.setIdCargo(input.idCargo());
        return dto;
    }

    @Override
    protected AdministrativoDTO toDtoFind(ADMINISTRATIVO_FIND input) {
        AdministrativoDTO dto = new AdministrativoDTO();
        dto.setId(input.id());
        return dto;
    }

    @Override
    public AdministrativoOutput toOutput(AdministrativoDTO dto) {

        if (dto == null)
            return null;

        return AdministrativoOutput.builder()
                .id(dto.getId())
                .fechainicio(dto.getFechainicio())
                .fechasalida(dto.getFechasalida())
                .idCargo(dto.getIdCargo())
                .idEstado(dto.getIdEstado())
                .idPersona(dto.getIdPersona())
                .persona(dto.getPersona() != null ? personaMap.toOutput(dto.getPersona()) : null)
                .estado(dto.getEstado() != null ? estadoMap.toOutput(dto.getEstado()) : null)
                .cargo(dto.getCargo() != null ? cargoMap.toOutput(dto.getCargo()) : null)
                .documentoList(
                        dto.getDocumentoList() != null ? documentoMap.toOutputList(dto.getDocumentoList()) : null)
                .build();
    }

    public java.util.List<AdministrativoOutput> toOutputList(java.util.List<AdministrativoDTO> dtoList) {
        return dtoList.stream().map(this::toOutput).toList();
    }

    public java.util.List<AdministrativoDTO> toDtoList(java.util.List<ADMINISTRATIVO_FIND> inputList) {
        return inputList.stream().map(this::toDtoFind).toList();
    }
}
