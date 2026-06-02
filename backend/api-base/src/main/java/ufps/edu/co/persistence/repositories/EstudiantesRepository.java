package ufps.edu.co.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufps.edu.co.persistence.entities.EstudiantesEntity;

@Repository
public interface EstudiantesRepository extends JpaRepository<EstudiantesEntity, Integer> {

    List<EstudiantesEntity> findByProgramaId(Integer programaId);

    List<EstudiantesEntity> findByCohorteId(Integer cohorteId);

    List<EstudiantesEntity> findByProgramaIdAndCohorteId(Integer programaId, Integer cohorteId);

    boolean existsByCedula(String cedula);

    boolean existsByCedulaAndIdNot(String cedula, Integer id);
}
