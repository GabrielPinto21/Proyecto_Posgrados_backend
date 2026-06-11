package ufps.edu.co.processor.crud;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.*;
import org.springframework.data.domain.*;
import org.springframework.retry.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import ufps.edu.co.domain.exceptions.*;
import ufps.edu.co.domain.exceptions.errorcodes.*;
import ufps.edu.co.maps.specific.*;
import ufps.edu.co.records.input.entity.CalificacioncriterioInput.*;
import ufps.edu.co.records.output.entity.*;
import ufps.edu.co.rest.dto.*;
import ufps.edu.co.rest.services.*;
import ufps.edu.co.services.*;
import ufps.edu.co.utils.*;
import ufps.edu.co.usecase.GlobalUseCase;

@Service
public class CalificacioncriterioProcessor implements
        GlobalUseCase<CALIFICACIONCRITERIO_CREATE, CALIFICACIONCRITERIO_UPDATE, CALIFICACIONCRITERIO_DELETE, CALIFICACIONCRITERIO_PATCH, CALIFICACIONCRITERIO_FIND, CalificacioncriterioOutput> {

    private static final Logger logger = LoggerFactory.getLogger(CalificacioncriterioProcessor.class);

    @Autowired
    private CalificacioncriterioService service;

    @Autowired
    private CalificacioncriterioMap map;

    @Autowired
    private AspiranteService aspiranteService;

    @Autowired
    private CriteriocohorteService criteriocohorteService;

    @Autowired
    private CalificacionEstadoService calificacionEstadoService;

    @Autowired
    private SESService sesService;

    // @Autowired
    // private EmailTemplates emailTemplates;

    @Override
    @Transactional
    public CalificacioncriterioOutput create(CALIFICACIONCRITERIO_CREATE input) {
        if (service.existsByAspiranteAndCriterio(input.idAspirante(), input.idCriterio())) {
            throw new DomainException(CalificacioncriterioErrorCode.CALIFICACIONCRITERIO_ALREADY_EXISTS,
                    "aspirante=" + input.idAspirante() + ", criterio=" + input.idCriterio());
        }
        CalificacioncriterioDTO dto = map.toDto(input);
        CalificacioncriterioOutput output = map.toOutput(service.create(dto));
        recalcularPuntuacionAspirante(input.idAspirante());
        calificacionEstadoService.actualizarEstadoCalificacion(input.idAspirante());
        return output;
    }

    @Override
    @Transactional
    public CalificacioncriterioOutput update(CALIFICACIONCRITERIO_UPDATE input) {
        try {
            CalificacioncriterioDTO dto = map.toDto(input);
            CalificacioncriterioOutput output = map.toOutput(service.update(input.id(), dto));
            recalcularPuntuacionAspirante(input.idAspirante());
            calificacionEstadoService.actualizarEstadoCalificacion(input.idAspirante());
            return output;
        } catch (CannotAcquireLockException | DeadlockLoserDataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new DomainException(CalificacioncriterioErrorCode.CALIFICACIONCRITERIO_NOT_FOUND, input.id());
        }
    }

    @Override
    public CalificacioncriterioOutput patch(CALIFICACIONCRITERIO_PATCH input) {
        throw new UnsupportedOperationException("Patch operation is not supported for Calificacioncriterio");
    }

    @Override
    public CalificacioncriterioOutput findById(CALIFICACIONCRITERIO_FIND input) {
        try {
            return map.toOutput(service.findById(input.id()));
        } catch (Exception e) {
            throw new DomainException(CalificacioncriterioErrorCode.CALIFICACIONCRITERIO_NOT_FOUND, input.id());
        }
    }

    @Override
    public List<CalificacioncriterioOutput> findAll() {
        return service.findAll().stream().map(map::toOutput).toList();
    }

    public Page<CalificacioncriterioOutput> findAll(Pageable pageable) {
        return service.findAll(pageable).map(map::toOutput);
    }

    @Override
    public void deleteById(CALIFICACIONCRITERIO_DELETE input) {
        try {
            service.deleteById(input.id());
        } catch (Exception e) {
            throw new DomainException(CalificacioncriterioErrorCode.CALIFICACIONCRITERIO_NOT_FOUND, input.id());
        }
    }

    public List<CalificacioncriterioOutput> findByIdAspirante(Integer idAspirante) {
        return service.findByIdAspirante(idAspirante).stream().map(map::toOutput).toList();
    }

    public List<CalificacioncriterioOutput> findByIdCriterio(Integer idCriterio) {
        return service.findByIdCriterio(idCriterio).stream().map(map::toOutput).toList();
    }

    @Retryable(
        retryFor = { CannotAcquireLockException.class, DeadlockLoserDataAccessException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 100, multiplier = 2)
    )
    @Transactional
    public CalificacionCriterioSimpleOutput calificarCriterio(Integer idAspirante, Integer idCriterio,
            BigDecimal puntaje) {
        CriteriocohorteDTO criteriocohorte = criteriocohorteService.findById(idCriterio);
        if (criteriocohorte == null) {
            throw new DomainException(CalificacioncriterioErrorCode.CALIFICACIONCRITERIO_NOT_FOUND, idCriterio);
        }

        AspiranteDTO aspiranteValidacion = aspiranteService.findById(idAspirante);
        if (aspiranteValidacion == null
                || !java.util.Objects.equals(aspiranteValidacion.getIdCohorte(), criteriocohorte.getIdCohorte())) {
            throw new DomainException(CalificacioncriterioErrorCode.CALIFICACIONCRITERIO_NOT_FOUND,
                    "El aspirante no pertenece a la cohorte del criterio");
        }

        BigDecimal pesoMaximo = criteriocohorte.getPesoSnapshot();
        if (pesoMaximo == null) {
            throw new DomainException(CalificacioncriterioErrorCode.CALIFICACIONCRITERIO_NOT_FOUND, idCriterio);
        }
        if (puntaje != null && (puntaje.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0
            || puntaje.compareTo(BigDecimal.ZERO) < 0)) {
            throw new DomainException(CalificacioncriterioErrorCode.PUNTUACION_INVALIDA, puntaje);
        }
        if (puntaje != null && puntaje.compareTo(pesoMaximo) > 0) {
            throw new DomainException(CalificacioncriterioErrorCode.PUNTAJE_EXCEDE_MAXIMO, pesoMaximo.stripTrailingZeros().toPlainString());
        }
        CalificacioncriterioOutput result = service.findByIdAspiranteAndIdCriterio(idAspirante, idCriterio)
                .map(existing -> {
                    CALIFICACIONCRITERIO_UPDATE updateInput = new CALIFICACIONCRITERIO_UPDATE(
                            existing.getId(), idAspirante, idCriterio, puntaje);
                    return update(updateInput);
                })
                .orElseGet(() -> {
                    CALIFICACIONCRITERIO_CREATE createInput = new CALIFICACIONCRITERIO_CREATE(
                            idAspirante, idCriterio, puntaje);
                    return create(createInput);
                });
        AspiranteDTO aspirante = aspiranteService.findById(idAspirante);
        String nombreCriterio = criteriocohorte.getCriterioevaluacion() != null
                ? criteriocohorte.getCriterioevaluacion().getNombre() : "Criterio";
        try {
            sesService.enviarCorreo(aspirante.getPersona().getCorreo(), EmailTemplates.ASUNTO_CALIFICACION_CRITERIO,
                    EmailTemplates.cuerpoCalificacionCriterio(aspirante.getPersona().getNombres(), nombreCriterio,
                            result.puntuacion(), aspirante.getPuntuacion()));
        } catch (Exception ex) {
            logger.error("[CALIFICACION_EMAIL] Fallo al enviar correo de calificación a '{}': {}",
                    aspirante.getPersona().getCorreo(), ex.getMessage(), ex);
        }
        return CalificacionCriterioSimpleOutput.builder()
                .idAspirante(result.idAspirante())
                .idCriterio(result.idCriteriocohorte())
                .puntajeObtenido(result.puntuacion())
                .puntajeTotal(aspirante.getPuntuacion())
                .build();
    }

    @Transactional
    public void actualizarPesoSnapshotYRecalcular(Integer idCriterio, BigDecimal newPeso) {
        List<CalificacioncriterioDTO> calificaciones = service.findByIdCriterio(idCriterio);
        calificaciones.forEach(cal -> {
            service.update(cal.getId(), cal);
        });
        calificaciones.stream()
                .map(CalificacioncriterioDTO::getIdAspirante)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .forEach(this::recalcularPuntuacionAspirante);
    }

    public void recalcularPuntuacionAspirantePublic(Integer idAspirante) {
        recalcularPuntuacionAspirante(idAspirante);
    }

    private void recalcularPuntuacionAspirante(Integer idAspirante) {
        List<CalificacioncriterioDTO> calificaciones = service.findByIdAspirante(idAspirante);
        BigDecimal total = calificaciones.stream()
                .filter(c -> c.getPuntuacion() != null)
                .map(CalificacioncriterioDTO::getPuntuacion)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        aspiranteService.updatePuntuacion(idAspirante, total);
    }
}
