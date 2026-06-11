package ufps.edu.co.processor.crud;

import java.util.*;
import java.util.stream.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import ufps.edu.co.rest.dto.*;
import ufps.edu.co.rest.services.*;

@Service
public class CalificacionEstadoService {

    private static final Logger logger = LoggerFactory.getLogger(CalificacionEstadoService.class);

    @Autowired
    private AspiranteService aspiranteService;

    @Autowired
    private CriteriocohorteService criteriocohorteService;

    @Autowired
    private CalificacioncriterioService calificacioncriterioService;

    @Autowired
    private EstadoService estadoService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void actualizarEstadoCalificacion(Integer idAspirante) {
        try {
            AspiranteDTO aspirante = aspiranteService.findById(idAspirante);
            if (aspirante == null || aspirante.getIdCohorte() == null) return;

            List<CriteriocohorteDTO> criteriosCohorte = criteriocohorteService.findByIdCohorte(aspirante.getIdCohorte());
            if (criteriosCohorte.isEmpty()) return;

            List<CalificacioncriterioDTO> calificaciones = calificacioncriterioService.findByIdAspirante(idAspirante);
            Set<Integer> calificadosIds = calificaciones.stream()
                    .filter(c -> c.getPuntuacion() != null)
                    .map(CalificacioncriterioDTO::getIdCriteriocohorte)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            boolean todosCalificados = criteriosCohorte.stream()
                    .map(CriteriocohorteDTO::getId)
                    .allMatch(calificadosIds::contains);

            if (todosCalificados) {
                EstadoDTO estado = estadoService.findByTipoAndEntidad("VALIDADO_CALIFICADO", "aspirante");
                if (estado != null) aspiranteService.updateEstado(idAspirante, estado.getId());
            } else if (!calificadosIds.isEmpty()) {
                EstadoDTO estado = estadoService.findByTipoAndEntidad("VALIDADO_EN_PROGRESO", "aspirante");
                if (estado != null) aspiranteService.updateEstado(idAspirante, estado.getId());
            }
        } catch (Exception e) {
            logger.warn("No se pudo actualizar estado de calificación del aspirante {}: {}", idAspirante, e.getMessage());
        }
    }
}
