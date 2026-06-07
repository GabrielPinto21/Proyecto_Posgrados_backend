package ufps.edu.co.processor.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufps.edu.co.maps.specific.FacultadMap;
import ufps.edu.co.records.input.entity.FacultadInput.*;
import ufps.edu.co.records.output.entity.FacultadOutput;
import ufps.edu.co.rest.dto.FacultadDTO;
import ufps.edu.co.rest.services.FacultadService;
import ufps.edu.co.usecase.GlobalUseCase;

@Service
public class FacultadProcessor implements GlobalUseCase<FACULTAD_CREATE, FACULTAD_UPDATE, FACULTAD_DELETE, FACULTAD_PATCH, FACULTAD_FIND, FacultadOutput> {

    @Autowired
    private FacultadService service;

    @Autowired
    private FacultadMap map;

    @Override
    public FacultadOutput create(FACULTAD_CREATE input) {
        FacultadDTO dto = map.toDto(input);
        FacultadDTO created = service.create(dto);
        return map.toOutput(created);
    }

    @Override
    public FacultadOutput update(FACULTAD_UPDATE input) {
        FacultadDTO dto = map.toDto(input);
        FacultadDTO updated = service.update(input.id(), dto);
        return map.toOutput(updated);
    }

    @Override
    public FacultadOutput patch(FACULTAD_PATCH input) {
        throw new UnsupportedOperationException("Patch not supported for Facultad");
    }

    @Override
    public FacultadOutput findById(FACULTAD_FIND input) {
        return map.toOutput(service.findById(input.id()));
    }

    @Override
    public List<FacultadOutput> findAll() {
        return service.findAll().stream().map(map::toOutput).toList();
    }

    @Override
    public void deleteById(FACULTAD_DELETE input) {
        service.deleteById(input.id());
    }
}
