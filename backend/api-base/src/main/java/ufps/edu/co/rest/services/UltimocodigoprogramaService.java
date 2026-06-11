package ufps.edu.co.rest.services;

import java.util.List;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import ufps.edu.co.persistence.entities.UltimocodigoprogramaEntity;
import ufps.edu.co.persistence.repositories.UltimocodigoprogramaRepository;
import ufps.edu.co.rest.dto.UltimocodigoprogramaDTO;
import ufps.edu.co.rest.services.commons.GenericService;

@Service
@Transactional
public class UltimocodigoprogramaService extends GenericService<UltimocodigoprogramaEntity, UltimocodigoprogramaDTO> {

    @Autowired
    private UltimocodigoprogramaRepository repository;

    public UltimocodigoprogramaService() {
        super(UltimocodigoprogramaEntity.class, UltimocodigoprogramaDTO.class);
    }

    @Override
    protected UltimocodigoprogramaDTO entityToDto(UltimocodigoprogramaEntity e) {
        return UltimocodigoprogramaDTO.builder()
                .id(e.getId())
                .idPrograma(e.getIdPrograma())
                .codigo(e.getCodigo())
                .build();
    }

    @Transactional(readOnly = true)
    public List<UltimocodigoprogramaDTO> findAll() {
        return entityListToDtoList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public List<UltimocodigoprogramaDTO> findAllConNombrePrograma() {
        return repository.findAllWithPrograma().stream()
                .map(e -> UltimocodigoprogramaDTO.builder()
                        .id(e.getId())
                        .idPrograma(e.getIdPrograma())
                        .codigo(e.getCodigo())
                        .nombrePrograma(e.getPrograma() != null ? e.getPrograma().getNombre() : null)
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public UltimocodigoprogramaDTO findById(Integer id) {
        return entityToDto(repository.findById(id));
    }

    @Transactional(readOnly = true)
    public UltimocodigoprogramaDTO findByIdPrograma(Integer idPrograma) {
        return repository.findByIdPrograma(idPrograma)
                .map(this::entityToDto)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean existsByIdPrograma(Integer idPrograma) {
        return repository.existsByIdPrograma(idPrograma);
    }

    public UltimocodigoprogramaDTO create(UltimocodigoprogramaDTO dto) {
        return entityToDto(repository.save(dtoToEntity(dto)));
    }

    public UltimocodigoprogramaDTO update(Integer id, UltimocodigoprogramaDTO dto) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("UltimoCodigoPrograma no encontrado con id: " + id));
        dto.setId(id);
        return entityToDto(repository.save(dtoToEntity(dto)));
    }

    public void deleteById(Integer id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("UltimoCodigoPrograma no encontrado con id: " + id));
        repository.deleteById(id);
    }
}
