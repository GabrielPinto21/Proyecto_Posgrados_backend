package ufps.edu.co.processor.crud;

import java.util.List;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import ufps.edu.co.domain.exceptions.*;
import ufps.edu.co.domain.exceptions.errorcodes.*;
import ufps.edu.co.maps.specific.*;
import ufps.edu.co.records.input.entity.UltimocodigoprogramaInput.*;
import ufps.edu.co.records.output.entity.*;
import ufps.edu.co.rest.dto.*;
import ufps.edu.co.rest.services.*;
import ufps.edu.co.usecase.*;

@Service
public class UltimocodigoprogramaProcessor implements
        GlobalUseCase<ULTIMOCODIGOPROGRAMA_CREATE, ULTIMOCODIGOPROGRAMA_UPDATE, ULTIMOCODIGOPROGRAMA_DELETE, ULTIMOCODIGOPROGRAMA_PATCH, ULTIMOCODIGOPROGRAMA_FIND, UltimocodigoprogramaOutput> {

    @Autowired
    private UltimocodigoprogramaService service;

    @Autowired
    private UltimocodigoprogramaMap map;

    @Autowired
    private ProgramaService programaService;

    @Override
    public UltimocodigoprogramaOutput create(ULTIMOCODIGOPROGRAMA_CREATE input) {
        if (service.existsByIdPrograma(input.idPrograma())) {
            throw new DomainException(UltimocodigoprogramaErrorCode.ULTIMOCODIGOPROGRAMA_ALREADY_EXISTS_CONFLICT,
                    "idPrograma=" + input.idPrograma());
        }
        UltimocodigoprogramaDTO dto = map.toDto(input);
        return map.toOutput(service.create(dto));
        } catch (DomainException e) {
        throw e;
    }

    @Override
    public UltimocodigoprogramaOutput update(ULTIMOCODIGOPROGRAMA_UPDATE input) {
        try {
            UltimocodigoprogramaDTO dto = map.toDto(input);
            return map.toOutput(service.update(input.id(), dto));
        } catch (Exception e) {
            throw new DomainException(UltimocodigoprogramaErrorCode.ULTIMOCODIGOPROGRAMA_NOT_FOUND, input.id());
        }
    }

    @Override
    public UltimocodigoprogramaOutput patch(ULTIMOCODIGOPROGRAMA_PATCH input) {
        throw new UnsupportedOperationException("Patch operation is not supported for UltimoCodigoPrograma");
    }

    @Override
    public UltimocodigoprogramaOutput findById(ULTIMOCODIGOPROGRAMA_FIND input) {
        UltimocodigoprogramaOutput output = map.toOutput(service.findById(input.id()));
        if (output == null) {
            throw new DomainException(UltimocodigoprogramaErrorCode.ULTIMOCODIGOPROGRAMA_NOT_FOUND, input.id());
        }
        return output;
    }

    @Override
    public List<UltimocodigoprogramaOutput> findAll() {
        return service.findAll().stream().map(dto -> {
            ProgramaDTO programa = dto.getIdPrograma() != null ? programaService.findById(dto.getIdPrograma()) : null;
            return UltimocodigoprogramaOutput.builder()
                    .id(dto.getId())
                    .idPrograma(dto.getIdPrograma())
                    .nombrePrograma(programa != null ? programa.getNombre() : null)
                    .codigo(dto.getCodigo())
                    .build();
        }).toList();
    }

    @Override
    public void deleteById(ULTIMOCODIGOPROGRAMA_DELETE input) {
        try {
            service.deleteById(input.id());
        } catch (Exception e) {
            throw new DomainException(UltimocodigoprogramaErrorCode.ULTIMOCODIGOPROGRAMA_NOT_FOUND, input.id());
        }
    }

    public UltimocodigoprogramaOutput findByIdPrograma(Integer idPrograma) {
        UltimocodigoprogramaDTO dto = service.findByIdPrograma(idPrograma);
        if (dto == null) {
            throw new DomainException(UltimocodigoprogramaErrorCode.ULTIMOCODIGOPROGRAMA_NOT_FOUND,
                    "idPrograma=" + idPrograma);
        }
        return map.toOutput(dto);
    }

    public UltimocodigoprogramaOutput updateCodigoPorPrograma(Integer idPrograma, Integer nuevoCodigo) {
        UltimocodigoprogramaDTO dto = service.findByIdPrograma(idPrograma);
        if (dto == null) {
            throw new DomainException(UltimocodigoprogramaErrorCode.ULTIMOCODIGOPROGRAMA_NOT_FOUND,
                    "idPrograma=" + idPrograma);
        }
        dto.setCodigo(nuevoCodigo);
        return map.toOutput(service.update(dto.getId(), dto));
    }
}
