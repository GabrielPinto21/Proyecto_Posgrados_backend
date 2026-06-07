package ufps.edu.co.processor.crud;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufps.edu.co.maps.specific.PaisMap;
import ufps.edu.co.records.input.entity.PaisInput.*;
import ufps.edu.co.records.output.entity.PaisOutput;
import ufps.edu.co.rest.dto.PaisDTO;
import ufps.edu.co.rest.services.PaisService;
import ufps.edu.co.usecase.GlobalUseCase;

@Service
public class PaisProcessor implements GlobalUseCase<PAIS_CREATE, PAIS_UPDATE, PAIS_DELETE, PAIS_PATCH, PAIS_FIND, PaisOutput> {

    @Autowired
    private PaisService service;

    @Autowired
    private PaisMap map;

    @Override
    public PaisOutput create(PAIS_CREATE input) {
        PaisDTO dto = map.toDto(input);
        PaisOutput output = map.toOutput(service.create(dto));
        return output;
    }

    @Override
    public PaisOutput update(PAIS_UPDATE input) {
        PaisDTO dto = map.toDto(input);
        PaisOutput output = map.toOutput(service.update(dto.getId(), dto));
        return output;
    }

    @Override
    public PaisOutput findById(PAIS_FIND input) {
        PaisOutput output = map.toOutput(service.findById(input.id()));
        return output;
    }

    @Override
    public List<PaisOutput> findAll() {
        return service.findAll().stream().map(map::toOutput).collect(Collectors.toList());
    }

    @Override
    public void deleteById(PAIS_DELETE input) {
        service.deleteById(input.id());
    }

    @Override
    public PaisOutput patch(PAIS_PATCH input) {
        throw new UnsupportedOperationException("Unimplemented method 'patch'");
    }
}
