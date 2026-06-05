package ufps.edu.co.persistence.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import ufps.edu.co.persistence.entities.UltimocodigoprogramaEntity;

@Repository
public interface UltimocodigoprogramaRepository extends JpaRepository<UltimocodigoprogramaEntity, Integer> {

    Optional<UltimocodigoprogramaEntity> findByIdPrograma(Integer idPrograma);

    boolean existsByIdPrograma(Integer idPrograma);
}
