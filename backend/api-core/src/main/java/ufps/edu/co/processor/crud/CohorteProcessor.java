package ufps.edu.co.processor.crud;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufps.edu.co.domain.exceptions.DomainException;
import ufps.edu.co.domain.exceptions.errorcodes.CohorteErrorCode;
import ufps.edu.co.domain.exceptions.errorcodes.CriteriocohorteErrorCode;
import ufps.edu.co.domain.exceptions.errorcodes.CriterioevaluacionErrorCode;
import ufps.edu.co.maps.specific.CohorteMap;
import ufps.edu.co.records.input.entity.CohorteInput.*;
import ufps.edu.co.records.output.entity.CohorteDetalleOutput;
import ufps.edu.co.records.output.entity.CohorteListadoOutput;
import ufps.edu.co.records.output.entity.CohorteOutput;
import ufps.edu.co.records.output.entity.CohorteResumenOutput;
import ufps.edu.co.records.output.entity.CriteriosCohorteOutput;
import ufps.edu.co.records.output.entity.ProgramaInicioOutput;
import ufps.edu.co.rest.dto.*;
import ufps.edu.co.rest.services.AdmitidoService;
import ufps.edu.co.rest.services.AspiranteService;
import ufps.edu.co.rest.services.CalificacioncriterioService;
import ufps.edu.co.rest.services.CohorteService;
import ufps.edu.co.rest.services.CriteriocohorteService;
import ufps.edu.co.rest.services.CriterioevaluacionService;
import ufps.edu.co.rest.services.DocumentoService;
import ufps.edu.co.rest.services.DocumentosrequisitoconsejocohorteService;
import ufps.edu.co.rest.services.DocumentosrequisitoconsejoService;
import ufps.edu.co.rest.services.DocumentosrequisitoprogramacohorteService;
import ufps.edu.co.rest.services.DocumentosrequisitoprogramaService;
import ufps.edu.co.rest.services.EstadoService;
import ufps.edu.co.rest.services.PlazoService;
import ufps.edu.co.rest.services.SemestreService;
import ufps.edu.co.rest.services.TipoplazoService;
import ufps.edu.co.usecase.GlobalUseCase;

@Service
public class CohorteProcessor implements GlobalUseCase<COHORTE_CREATE, COHORTE_UPDATE, COHORTE_DELETE, COHORTE_PATCH, COHORTE_FIND, CohorteOutput> {

    @Autowired
    private CohorteService service;

    @Autowired
    private CohorteMap map;

    @Autowired
    private AspiranteService aspiranteService;

    @Autowired
    private AdmitidoService admitidoService;

    @Autowired
    private CriterioevaluacionService criterioevaluacionService;

    @Autowired
    private CriteriocohorteService criteriocohorteService;

    @Autowired
    private CalificacioncriterioService calificacioncriterioService;

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private PlazoService plazoService;

    @Autowired
    private SemestreService semestreService;

    @Autowired
    private TipoplazoService tipoplazoService;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private DocumentosrequisitoconsejocohorteService documentosrequisitoconsejocohorteService;

    @Autowired
    private DocumentosrequisitoprogramacohorteService documentosrequisitoprogramacohorteService;

    @Autowired
    private DocumentosrequisitoconsejoService documentosrequisitoconsejoService;

    @Autowired
    private DocumentosrequisitoprogramaService documentosrequisitoprogramaService;

    @Override
    public CohorteOutput create(COHORTE_CREATE input) {
        try {
            CohorteDTO dto = map.toDto(input);
            return map.toOutput(service.create(dto));
        } catch (Exception e) {
            throw new DomainException(CohorteErrorCode.COHORTE_DUPLICADO, input);
        }
    }

    @Override
    public CohorteOutput update(COHORTE_UPDATE input) {
        try {
            CohorteDTO dto = map.toDto(input);
            return map.toOutput(service.update(input.id(), dto));
        } catch (Exception e) {
            throw new DomainException(CohorteErrorCode.COHORTE_NOT_FOUND, input.id());
        }
    }

    @Override
    public CohorteOutput findById(COHORTE_FIND input) {
        try {
            return map.toOutput(service.findById(input.id()));
        } catch (Exception e) {
            throw new DomainException(CohorteErrorCode.COHORTE_NOT_FOUND, input.id());
        }
    }

    @Override
    public List<CohorteOutput> findAll() {
        try {
            return service.findAll().stream()
                    .map(map::toOutput)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DomainException(CohorteErrorCode.COHORTE_NOT_FOUND, "findAll");
        }
    }

    @Override
    public void deleteById(COHORTE_DELETE input) {
        try {
            service.deleteById(input.id());
        } catch (DataIntegrityViolationException e) {
            throw new DomainException(CohorteErrorCode.COHORTE_CON_DEPENDENCIAS_CONFLICT, input.id());
        } catch (Exception e) {
            throw new DomainException(CohorteErrorCode.COHORTE_NOT_FOUND, input.id());
        }
    }

    @Override
    public CohorteOutput patch(COHORTE_PATCH input) {
        return null;
    }

    public CohorteOutput createWithPlazo(COHORTE_WITHPLAZO_CREATE input) {
        throw new UnsupportedOperationException("No se puede crear una cohorte con plazo desde este endpoint");
    }

    public long countAspirantesEnProcesoEnCohorteAbierta(Integer cohorteId) {
        try {
            return service.countAspirantesEnProcesoEnCohorteAbierta(cohorteId);
        } catch (Exception e) {
            throw new DomainException(CohorteErrorCode.COHORTE_NOT_FOUND, cohorteId);
        }
    }

    public List<CohorteOutput> findActivasByIdPrograma(Integer idPrograma) {
        try {
            return service.findActivasByIdPrograma(idPrograma).stream()
                    .map(map::toOutput)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DomainException(CohorteErrorCode.COHORTE_NOT_FOUND, idPrograma);
        }
    }

    public long countAspirantesEnProcesoByCohorteId(Integer cohorteId) {
        try {
            return service.countAspirantesEnProcesoByCohorteId(cohorteId);
        } catch (Exception e) {
            throw new DomainException(CohorteErrorCode.COHORTE_NOT_FOUND, cohorteId);
        }
    }

    public CriteriosCohorteOutput getCriteriosByCohorte(Integer cohorteId) {
        CohorteDTO cohorte = service.findById(cohorteId);
        if (cohorte == null) {
            throw new RuntimeException("Cohorte no encontrada: " + cohorteId);
        }
        boolean activa = cohorte.getEstado() != null
                && "ABIERTA".equalsIgnoreCase(cohorte.getEstado().getTipo());
        List<CriteriosCohorteOutput.CriterioInfo> criterios = criteriocohorteService
                .findByIdCohorte(cohorteId).stream()
                .map(cc -> {
                    CriterioevaluacionDTO ce = criterioevaluacionService.findById(cc.getIdCriterio());
                    return CriteriosCohorteOutput.CriterioInfo.builder()
                            .id(cc.getId())
                            .nombre(ce != null ? ce.getNombre() : null)
                            .descripcion(ce != null ? ce.getDescripcion() : null)
                            .peso(cc.getPesoSnapshot())
                            .build();
                })
                .toList();
        return CriteriosCohorteOutput.builder()
                .cohorteActual(CriteriosCohorteOutput.CohorteInfo.builder()
                        .id(cohorte.getId())
                        .nombre(cohorte.getNombre())
                        .activa(activa)
                        .build())
                .criterios(criterios)
                .build();
    }

    public List<CohorteListadoOutput> getCohortesByPrograma(Integer programaId) {
        return service.findByIdPrograma(programaId).stream().map(cohorte -> {
            boolean activa = cohorte.getEstado() != null
                    && "ABIERTA".equalsIgnoreCase(cohorte.getEstado().getTipo());
            long inscritos = aspiranteService.countByCohorte(cohorte.getId());
            long admitidos = admitidoService.countByCohorte(cohorte.getId());
            return CohorteListadoOutput.builder()
                    .id(cohorte.getId())
                    .nombre(cohorte.getNombre())
                    .activa(activa)
                    .inscritos(inscritos)
                    .admitidos(admitidos)
                    .cupos(cohorte.getCupos())
                    .fechaLimiteDocumentos(cohorte.getPlazo() != null ? cohorte.getPlazo().getFechafin() : null)
                    .fechaLimitePago(cohorte.getPlazo3() != null ? cohorte.getPlazo3().getFechafin() : null)
                    .fechaInicio(cohorte.getSemestre() != null ? cohorte.getSemestre().getFechaInicio() : null)
                    .build();
        }).toList();
    }

    public ProgramaInicioOutput getProgramaInicio(Integer cohorteId) {
        CohorteDTO cohorte = service.findById(cohorteId);
        if (cohorte == null) {
            throw new RuntimeException("Cohorte no encontrada: " + cohorteId);
        }

        long totalInscritos = aspiranteService.countByCohorte(cohorte.getId());
        long validados = aspiranteService.countValidadosByCohorte(cohorte.getId());
        long calificados = aspiranteService.countCalificadosByCohorte(cohorte.getId());

        return ProgramaInicioOutput.builder()
                .cohorteActual(ProgramaInicioOutput.CohorteResumen.builder()
                        .id(cohorte.getId())
                        .nombre(cohorte.getNombre())
                        .activa(true)
                        .fechaLimiteDocumentos(cohorte.getPlazo() != null ? cohorte.getPlazo().getFechafin() : null)
                        .fechaLimitePago(cohorte.getPlazo3() != null ? cohorte.getPlazo3().getFechafin() : null)
                        .build())
                .validacion(ProgramaInicioOutput.ValidacionResumen.builder()
                        .totalInscritos(totalInscritos)
                        .aspirantesValidados(validados)
                        .build())
                .calificacion(ProgramaInicioOutput.CalificacionResumen.builder()
                        .totalValidados(validados)
                        .aspirantesCalificados(calificados)
                        .build())
                .build();
    }

    public List<ProgramaInicioOutput> getProgramaInicioByPrograma(Integer programaId) {
        return service.findResumenDataByIdPrograma(programaId).stream()
                .filter(cohorte -> cohorte.getEstado() != null
                        && "ABIERTA".equalsIgnoreCase(cohorte.getEstado().getTipo()))
                .map(cohorte -> {
                    Integer cohorteId = cohorte.getId();
                    String nombre = cohorte.getNombre();
                    LocalDate fechaLimiteDocumentos = cohorte.getPlazo() != null
                            ? cohorte.getPlazo().getFechafin()
                            : null;
                    LocalDate fechaLimitePago = cohorte.getPlazo3() != null
                            ? cohorte.getPlazo3().getFechafin()
                            : null;

                    long totalInscritos = aspiranteService.countByCohorte(cohorteId);
                    long validados = aspiranteService.countValidadosByCohorte(cohorteId);
                    long calificados = aspiranteService.countCalificadosByCohorte(cohorteId);

                    return ProgramaInicioOutput.builder()
                            .cohorteActual(ProgramaInicioOutput.CohorteResumen.builder()
                                    .id(cohorteId)
                                    .nombre(nombre)
                                    .activa(true)
                                    .fechaLimiteDocumentos(fechaLimiteDocumentos)
                                    .fechaLimitePago(fechaLimitePago)
                                    .build())
                            .validacion(ProgramaInicioOutput.ValidacionResumen.builder()
                                    .totalInscritos(totalInscritos)
                                    .aspirantesValidados(validados)
                                    .build())
                            .calificacion(ProgramaInicioOutput.CalificacionResumen.builder()
                                    .totalValidados(validados)
                                    .aspirantesCalificados(calificados)
                                    .build())
                            .build();
                })
                .toList();
    }

    public CohorteDetalleOutput getCohorteDetalle(Integer cohorteId) {
        CohorteDTO cohorte = service.findById(cohorteId);
        if (cohorte == null) {
            throw new DomainException(CohorteErrorCode.COHORTE_NOT_FOUND, cohorteId);
        }
        boolean activa = cohorte.getEstado() != null
                && "ABIERTA".equalsIgnoreCase(cohorte.getEstado().getTipo());

        List<CohorteDetalleOutput.CriterioInfo> criterios = criteriocohorteService
                .findByIdCohorte(cohorteId).stream()
                .map(cc -> {
                    CriterioevaluacionDTO ce = criterioevaluacionService.findById(cc.getIdCriterio());
                    return CohorteDetalleOutput.CriterioInfo.builder()
                            .id(cc.getId())
                            .idCriterioevaluacion(cc.getIdCriterio())
                            .nombre(ce != null ? ce.getNombre() : null)
                            .peso(cc.getPesoSnapshot())
                            .build();
                })
                .toList();

        List<CohorteDetalleOutput.DocumentoAsignadoInfo> documentosConsejo = documentosrequisitoconsejocohorteService
                .findByIdCohorte(cohorteId).stream()
                .filter(doc -> doc.getIdCohorte() != null && doc.getIdCohorte().equals(cohorteId)
                        && doc.getIdDocrequisito() != null)
                .map(this::mapDocumentoConsejo)
                .toList();

        List<CohorteDetalleOutput.DocumentoAsignadoInfo> documentosPrograma = documentosrequisitoprogramacohorteService
                .findByIdCohorte(cohorteId).stream()
                .filter(doc -> doc.getIdCohorte() != null && doc.getIdCohorte().equals(cohorteId)
                        && doc.getIdDocrequisito() != null)
                .map(this::mapDocumentoPrograma)
                .toList();

        List<AspiranteDTO> aspirantes = aspiranteService.findByCohorte(cohorteId);

        List<CohorteDetalleOutput.AspiranteInfo> inscritosData = aspirantes.stream()
                .map(a -> {
                    PersonaDTO p = a.getPersona();
                    String nombre = p != null
                            ? ((p.getNombres() != null ? p.getNombres() : "") + " "
                                    + (p.getApellidos() != null ? p.getApellidos() : "")).trim()
                            : "";
                    String cedula = p != null && p.getDocumentopersona() != null
                            && p.getDocumentopersona().getNumerodocumento() != null
                                    ? p.getDocumentopersona().getNumerodocumento().toString()
                                    : null;
                    return CohorteDetalleOutput.AspiranteInfo.builder()
                            .id(a.getId())
                            .nombre(nombre)
                            .cedula(cedula)
                            .correo(p != null ? p.getCorreo() : null)
                            .build();
                }).toList();

        List<CohorteDetalleOutput.AspiranteInfo> admitidosData = admitidoService
                .findByCohorte(cohorteId).stream()
                .map(admitido -> {
                    AspiranteDTO a = admitido.getAspirante();
                    if (a == null)
                        return null;
                    PersonaDTO p = a.getPersona();
                    String nombre = p != null
                            ? ((p.getNombres() != null ? p.getNombres() : "") + " "
                                    + (p.getApellidos() != null ? p.getApellidos() : "")).trim()
                            : "";
                    String cedula = p != null && p.getDocumentopersona() != null
                            && p.getDocumentopersona().getNumerodocumento() != null
                                    ? p.getDocumentopersona().getNumerodocumento().toString()
                                    : null;
                    return CohorteDetalleOutput.AspiranteInfo.builder()
                            .id(a.getId())
                            .nombre(nombre)
                            .cedula(cedula)
                            .correo(p != null ? p.getCorreo() : null)
                            .build();
                })
                .filter(java.util.Objects::nonNull)
                .toList();

        return CohorteDetalleOutput.builder()
                .id(cohorte.getId())
                .nombre(cohorte.getNombre())
                .activa(activa)
                .inscritos(aspirantes.size())
                .admitidos(admitidosData.size())
                .cupos(cohorte.getCupos())
                .idSemestre(cohorte.getSemestre() != null ? cohorte.getSemestre().getId() : null)
                .nombreSemestre(cohorte.getSemestre() != null ? cohorte.getSemestre().getNombre() : null)
                .idModalidad(cohorte.getModalidad() != null ? cohorte.getModalidad().getId() : null)
                .nombreModalidad(cohorte.getModalidad() != null ? cohorte.getModalidad().getNombre() : null)
                .fechaInicioDocumentacion(cohorte.getPlazo() != null ? cohorte.getPlazo().getFechainicio() : null)
                .fechaFinDocumentacion(cohorte.getPlazo() != null ? cohorte.getPlazo().getFechafin() : null)
                .fechaInicioInscripcion(cohorte.getPlazo2() != null ? cohorte.getPlazo2().getFechainicio() : null)
                .fechaFinInscripcion(cohorte.getPlazo2() != null ? cohorte.getPlazo2().getFechafin() : null)
                .fechaInicioPago(cohorte.getPlazo3() != null ? cohorte.getPlazo3().getFechainicio() : null)
                .fechaFinPago(cohorte.getPlazo3() != null ? cohorte.getPlazo3().getFechafin() : null)
                .fechaInicio(cohorte.getSemestre() != null ? cohorte.getSemestre().getFechaInicio() : null)
                .criterios(criterios)
                .documentosAsignados(CohorteDetalleOutput.DocumentosAsignadosInfo.builder()
                        .documentosConsejo(documentosConsejo)
                        .documentosPrograma(documentosPrograma)
                        .build())
                .inscritosData(inscritosData)
                .admitidosData(admitidosData)
                .build();
    }

    private CohorteDetalleOutput.DocumentoAsignadoInfo mapDocumentoConsejo(
            DocumentosrequisitoconsejocohorteDTO dto) {
        String nombre = null;
        if (dto.getIdDocrequisito() != null) {
            DocumentosrequisitoconsejoDTO documento = documentosrequisitoconsejoService
                    .findById(dto.getIdDocrequisito());
            nombre = documento != null ? documento.getNombre() : null;
        }
        return CohorteDetalleOutput.DocumentoAsignadoInfo.builder()
                .id(dto.getId())
                .idDocrequisito(dto.getIdDocrequisito())
                .idCohorte(dto.getIdCohorte())
                .nombre(nombre)
                .build();
    }

    private CohorteDetalleOutput.DocumentoAsignadoInfo mapDocumentoPrograma(
            DocumentosrequisitoprogramacohorteDTO dto) {
        String nombre = null;
        if (dto.getIdDocrequisito() != null) {
            DocumentosrequisitoprogramaDTO documento = documentosrequisitoprogramaService
                    .findById(dto.getIdDocrequisito());
            nombre = documento != null ? documento.getNombre() : null;
        }
        return CohorteDetalleOutput.DocumentoAsignadoInfo.builder()
                .id(dto.getId())
                .idDocrequisito(dto.getIdDocrequisito())
                .idCohorte(dto.getIdCohorte())
                .nombre(nombre)
                .build();
    }

    @Transactional
    public CohorteListadoOutput createCohorte(Integer programaId, COHORTE_DIRECTOR_CREATE body) {
        String nombre = body.nombre();

        List<TipoplazoDTO> tipoplazos = tipoplazoService.findAll();
        if (tipoplazos.isEmpty()) {
            throw new RuntimeException("No hay tipos de plazo configurados");
        }
        Integer tipoplazoDocId = tipoplazos.stream()
                .filter(t -> "DOCUMENTACION".equalsIgnoreCase(t.getTipo()))
                .map(TipoplazoDTO::getId)
                .findFirst()
                .orElse(tipoplazos.get(0).getId());
        Integer tipoplazoInscId = tipoplazos.stream()
                .filter(t -> "INSCRIPCION".equalsIgnoreCase(t.getTipo()))
                .map(TipoplazoDTO::getId)
                .findFirst()
                .orElse(tipoplazos.get(0).getId());
        Integer tipoplazoPagoId = tipoplazos.stream()
                .filter(t -> "PAGO".equalsIgnoreCase(t.getTipo()))
                .map(TipoplazoDTO::getId)
                .findFirst()
                .orElse(tipoplazos.get(0).getId());

        PlazoDTO plazoDoc = plazoService.create(PlazoDTO.builder()
                .fechainicio(body.fechaInicioDocumentacion())
                .fechafin(body.fechaFinDocumentacion())
                .idTipoplazo(tipoplazoDocId)
                .build());

        PlazoDTO plazoInsc = plazoService.create(PlazoDTO.builder()
                .fechainicio(body.fechaInicioInscripcion())
                .fechafin(body.fechaFinInscripcion())
                .idTipoplazo(tipoplazoInscId)
                .build());

        PlazoDTO plazoPago = plazoService.create(PlazoDTO.builder()
                .fechainicio(body.fechaInicioPago())
                .fechafin(body.fechaFinPago())
                .idTipoplazo(tipoplazoPagoId)
                .build());

        SemestreDTO semestre = semestreService.findById(body.idSemestre());
        if (semestre == null) {
            throw new DomainException(CohorteErrorCode.COHORTE_NOT_FOUND, body.idSemestre());
        }
        String tipoEstadoSemestre = semestre.getEstado() != null ? semestre.getEstado().getTipo() : null;
        if (!"EN CURSO".equalsIgnoreCase(tipoEstadoSemestre) && !"PROGRAMADO".equalsIgnoreCase(tipoEstadoSemestre)) {
            throw new DomainException(CohorteErrorCode.COHORTE_SEMESTRE_NO_VALIDO_CONFLICT,
                    body.idSemestre() + " / " + tipoEstadoSemestre);
        }

        EstadoDTO estadoCohorte = estadoService.findByTipoAndEntidad("CERRADA", "cohorte");
        if (estadoCohorte == null) {
            throw new RuntimeException("No hay estado CERRADA configurado para cohorte");
        }

        Integer cohorteId = service.createAndGetId(CohorteDTO.builder()
                .nombre(nombre)
                .cupos(body.cupos())
                .idEstado(estadoCohorte.getId())
                .idSemestre(semestre.getId())
                .idModalidad(body.idModalidad())
                .idPlazodocumentacion(plazoDoc.getId())
                .idPlazoinscripcion(plazoInsc.getId())
                .idPlazopago(plazoPago.getId())
                .idPrograma(programaId)
                .build());

        if (body.documentosConsejo() != null) {
            body.documentosConsejo().stream()
                    .map(DOCUMENTO_ASIGNADO_CREATE::idDocrequisito)
                    .filter(java.util.Objects::nonNull)
                    .distinct()
                    .forEach(idDocrequisito -> documentosrequisitoconsejocohorteService.create(
                            DocumentosrequisitoconsejocohorteDTO.builder()
                                    .idDocrequisito(idDocrequisito)
                                    .idCohorte(cohorteId)
                                    .build()));
        }

        if (body.documentosPrograma() != null) {
            body.documentosPrograma().stream()
                    .map(DOCUMENTO_ASIGNADO_CREATE::idDocrequisito)
                    .filter(java.util.Objects::nonNull)
                    .distinct()
                    .forEach(idDocrequisito -> documentosrequisitoprogramacohorteService.create(
                            DocumentosrequisitoprogramacohorteDTO.builder()
                                    .idDocrequisito(idDocrequisito)
                                    .idCohorte(cohorteId)
                                    .build()));
        }

        if (body.criteriosCohorte() != null) {
            body.criteriosCohorte().stream()
                    .filter(java.util.Objects::nonNull)
                    .filter(criterio -> criterio.idCriterio() != null)
                    .distinct()
                    .forEach(criterio -> criteriocohorteService.create(
                            CriteriocohorteDTO.builder()
                                    .idCohorte(cohorteId)
                                    .idCriterio(criterio.idCriterio())
                                    .pesoSnapshot(criterio.pesoSnapshot())
                                    .build()));
        }

        return CohorteListadoOutput.builder()
                .id(cohorteId)
                .nombre(nombre)
                .activa(false)
                .inscritos(0)
                .admitidos(0)
                .cupos(body.cupos())
                .fechaLimiteDocumentos(body.fechaFinDocumentacion())
                .fechaLimitePago(body.fechaFinPago())
                .fechaInicio(body.fechaInicioDocumentacion())
                .build();
    }

    public List<CohorteResumenOutput> getCohortesByProgramaResumen(Integer programaId) {
        Map<Integer, CohorteCountsProjection> countsMap = aspiranteService.countAllByProgramaId(programaId)
                .stream()
                .collect(Collectors.toMap(CohorteCountsProjection::getIdCohorte, p -> p));

        return service.findResumenDataByIdPrograma(programaId).stream().map(row -> {
            Integer id = row.getId();
            String nombre = row.getNombre();
            Integer cuposRaw = row.getCupos();
            String estadoTipo = row.getEstado() != null ? row.getEstado().getTipo() : null;
            String semNombre = row.getSemestre() != null ? row.getSemestre().getNombre() : null;
            LocalDate plazoDocFin = row.getPlazo() != null ? row.getPlazo().getFechafin() : null;
            LocalDate plazoInsFin = row.getPlazo2() != null ? row.getPlazo2().getFechafin() : null;
            LocalDate plazoPagoFin = row.getPlazo3() != null ? row.getPlazo3().getFechafin() : null;

            boolean activa = "ABIERTA".equalsIgnoreCase(estadoTipo);
            int cupos = cuposRaw != null ? cuposRaw : 0;
            CohorteCountsProjection c = countsMap.get(id);

            return CohorteResumenOutput.builder()
                    .id(id)
                    .nombre(nombre)
                    .activa(activa)
                    .semestre(semNombre)
                    .cupos(cupos)
                    .fechaLimitePago(plazoPagoFin)
                    .fechaLimiteDocs(plazoDocFin)
                    .fechaLimiteInscripcion(plazoInsFin)
                    .totalInscritos(c != null && c.getTotalInscritos() != null ? c.getTotalInscritos() : 0L)
                    .totalNoConfirmados(c != null && c.getTotalNoConfirmados() != null ? c.getTotalNoConfirmados() : 0L)
                    .totalConfirmados(c != null && c.getTotalConfirmados() != null ? c.getTotalConfirmados() : 0L)
                    .totalPazysalvo(c != null && c.getTotalPazysalvo() != null ? c.getTotalPazysalvo() : 0L)
                    .totalValidados(c != null && c.getTotalValidados() != null ? c.getTotalValidados() : 0L)
                    .totalCalificados(c != null && c.getTotalCalificados() != null ? c.getTotalCalificados() : 0L)
                    .totalAdmitidos(c != null && c.getTotalAdmitidos() != null ? c.getTotalAdmitidos() : 0L)
                    .totalLegalizados(c != null && c.getTotalLegalizados() != null ? c.getTotalLegalizados() : 0L)
                    .build();
        }).toList();
    }

    public CohorteListadoOutput abrirCohorte(Integer cohorteId) {
        return cambiarEstadoCohorte(cohorteId, "ABIERTA");
    }

    public CohorteListadoOutput cerrarCohorte(Integer cohorteId) {
        return cambiarEstadoCohorte(cohorteId, "CERRADA");
    }

    private CohorteListadoOutput cambiarEstadoCohorte(Integer cohorteId, String nuevoEstado) {
        CohorteDTO cohorte = service.findById(cohorteId);
        if (cohorte == null) {
            throw new RuntimeException("Cohorte no encontrada: " + cohorteId);
        }
        EstadoDTO estado = estadoService.findByTipoAndEntidad(nuevoEstado, "cohorte");
        if (estado == null) {
            throw new RuntimeException("Estado '" + nuevoEstado + "' no configurado para cohorte");
        }
        cohorte.setIdEstado(estado.getId());
        service.update(cohorteId, cohorte);

        boolean activa = "ABIERTA".equalsIgnoreCase(nuevoEstado);
        return CohorteListadoOutput.builder()
                .id(cohorteId)
                .nombre(cohorte.getNombre())
                .activa(activa)
                .inscritos(aspiranteService.countByCohorte(cohorteId))
                .admitidos(admitidoService.countByCohorte(cohorteId))
                .cupos(cohorte.getCupos())
                .fechaLimiteDocumentos(cohorte.getPlazo() != null ? cohorte.getPlazo().getFechafin() : null)
                .fechaLimitePago(cohorte.getPlazo3() != null ? cohorte.getPlazo3().getFechafin() : null)
                .fechaInicio(cohorte.getSemestre() != null ? cohorte.getSemestre().getFechaInicio() : null)
                .build();
    }

    @Transactional
    public CohorteListadoOutput updateCohorte(Integer cohorteId, COHORTE_DIRECTOR_UPDATE body) {
        Integer targetCohorteId = body.id() != null ? body.id() : cohorteId;
        if (targetCohorteId == null) {
            throw new RuntimeException("Debe enviar el id de la cohorte a actualizar");
        }
        if (cohorteId != null && !cohorteId.equals(targetCohorteId)) {
            throw new RuntimeException("El id de la ruta no coincide con el id del body");
        }

        CohorteDTO cohorte = service.findById(targetCohorteId);
        if (cohorte == null) {
            throw new RuntimeException("Cohorte no encontrada: " + targetCohorteId);
        }

        boolean cohorteChanged = false;
        if (body.nombre() != null && !body.nombre().isBlank()) {
            cohorte.setNombre(body.nombre());
            cohorteChanged = true;
        }
        if (body.cupos() != null) {
            cohorte.setCupos(body.cupos());
            cohorteChanged = true;
        }

        if (body.idSemestre() == null) {
            throw new IllegalArgumentException("Debe enviar el id del semestre");
        }

        if (body.idModalidad() == null) {
            throw new IllegalArgumentException("Debe enviar el id de la modalidad");
        }

        SemestreDTO semestre = semestreService.findById(body.idSemestre());
        if (semestre == null) {
            throw new DomainException(CohorteErrorCode.COHORTE_NOT_FOUND, body.idSemestre());
        }
        String tipoEstadoSemestre = semestre.getEstado() != null ? semestre.getEstado().getTipo() : null;
        if (!"EN CURSO".equalsIgnoreCase(tipoEstadoSemestre) && !"PROGRAMADO".equalsIgnoreCase(tipoEstadoSemestre)) {
            throw new DomainException(CohorteErrorCode.COHORTE_SEMESTRE_NO_VALIDO_CONFLICT,
                    body.idSemestre() + " / " + tipoEstadoSemestre);
        }
        if (cohorte.getIdSemestre() == null || !cohorte.getIdSemestre().equals(semestre.getId())) {
            cohorte.setIdSemestre(semestre.getId());
            cohorteChanged = true;
        }

        if (cohorte.getIdModalidad() == null || !cohorte.getIdModalidad().equals(body.idModalidad())) {
            cohorte.setIdModalidad(body.idModalidad());
            cohorteChanged = true;
        }

        LocalDate fechaInicio = semestre.getFechaInicio();

        LocalDate fechaLimiteDocumentos = cohorte.getPlazo() != null ? cohorte.getPlazo().getFechafin() : null;
        if (cohorte.getPlazo() != null && (body.fechaInicioDocumentacion() != null || body.fechaFinDocumentacion() != null)) {
            PlazoDTO plazo = cohorte.getPlazo();
            if (body.fechaInicioDocumentacion() != null) plazo.setFechainicio(body.fechaInicioDocumentacion());
            if (body.fechaFinDocumentacion() != null) {
                plazo.setFechafin(body.fechaFinDocumentacion());
                fechaLimiteDocumentos = body.fechaFinDocumentacion();
            }
            plazoService.update(plazo.getId(), plazo);
        }

        if (cohorte.getPlazo2() != null && (body.fechaInicioInscripcion() != null || body.fechaFinInscripcion() != null)) {
            PlazoDTO plazo2 = cohorte.getPlazo2();
            if (body.fechaInicioInscripcion() != null) plazo2.setFechainicio(body.fechaInicioInscripcion());
            if (body.fechaFinInscripcion() != null) plazo2.setFechafin(body.fechaFinInscripcion());
            plazoService.update(plazo2.getId(), plazo2);
        }

        LocalDate fechaLimitePago = cohorte.getPlazo3() != null ? cohorte.getPlazo3().getFechafin() : null;
        if (cohorte.getPlazo3() != null && (body.fechaInicioPago() != null || body.fechaFinPago() != null)) {
            PlazoDTO plazo3 = cohorte.getPlazo3();
            if (body.fechaInicioPago() != null) plazo3.setFechainicio(body.fechaInicioPago());
            if (body.fechaFinPago() != null) {
                plazo3.setFechafin(body.fechaFinPago());
                fechaLimitePago = body.fechaFinPago();
            }
            plazoService.update(plazo3.getId(), plazo3);
        }

        if (cohorteChanged) {
            service.update(targetCohorteId, cohorte);
        }

        var existingConsejo = documentosrequisitoconsejocohorteService.findByIdCohorte(targetCohorteId);
        if (body.documentosConsejo() == null) {
            deleteAllConsejoDocuments(existingConsejo);
        } else if (!sameConsejoDocuments(body.documentosConsejo(), existingConsejo)) {
            if (body.documentosConsejo().stream().anyMatch(doc -> doc == null || doc.idDocrequisito() == null)) {
                throw new IllegalArgumentException(
                        "Todos los documentos de consejo deben incluir idDocrequisito");
            }

            List<Integer> invalidConsejoIds = body.documentosConsejo().stream()
                    .map(DOCUMENTO_ASIGNADO_CREATE::idDocrequisito)
                    .distinct()
                    .filter(idDocrequisito -> documentosrequisitoconsejoService.findById(idDocrequisito) == null)
                    .toList();

            if (!invalidConsejoIds.isEmpty()) {
                throw new IllegalArgumentException(
                        "No existen documentos de consejo con ids: " + invalidConsejoIds);
            }

            List<Integer> incomingConsejoIds = body.documentosConsejo().stream()
                    .map(DOCUMENTO_ASIGNADO_CREATE::idDocrequisito)
                    .distinct()
                    .toList();

            var toDeleteConsejo = existingConsejo.stream()
                    .filter(actual -> !incomingConsejoIds.contains(actual.getIdDocrequisito()))
                    .toList();

            var blockedConsejo = toDeleteConsejo.stream()
                    .filter(actual -> documentoService.countByIdDocumentosrequisitoconsejocohorte(actual.getId()) > 0)
                    .map(v -> v.getId())
                    .toList();

            if (!blockedConsejo.isEmpty()) {
                throw new DomainException(CohorteErrorCode.COHORTE_CON_ASIGNACIONES_BLOQUEADAS, blockedConsejo);
            }

            toDeleteConsejo.forEach(actual -> documentosrequisitoconsejocohorteService.deleteById(actual.getId()));

            var existingConsejoDocIds = existingConsejo.stream()
                    .map(v -> v.getIdDocrequisito())
                    .toList();

            incomingConsejoIds.stream()
                    .filter(idDocrequisito -> !existingConsejoDocIds.contains(idDocrequisito))
                    .forEach(idDocrequisito -> documentosrequisitoconsejocohorteService.create(
                            DocumentosrequisitoconsejocohorteDTO.builder()
                                    .idDocrequisito(idDocrequisito)
                                    .idCohorte(targetCohorteId)
                                    .build()));
        }

        var existingPrograma = documentosrequisitoprogramacohorteService.findByIdCohorte(targetCohorteId);
        if (body.documentosPrograma() == null) {
            deleteAllProgramaDocuments(existingPrograma);
        } else if (!sameProgramaDocuments(body.documentosPrograma(), existingPrograma)) {
            if (body.documentosPrograma().stream().anyMatch(doc -> doc == null || doc.idDocrequisito() == null)) {
                throw new IllegalArgumentException(
                        "Todos los documentos de programa deben incluir idDocrequisito");
            }

            List<Integer> invalidProgramaIds = body.documentosPrograma().stream()
                    .map(DOCUMENTO_ASIGNADO_CREATE::idDocrequisito)
                    .distinct()
                    .filter(idDocrequisito -> documentosrequisitoprogramaService.findById(idDocrequisito) == null)
                    .toList();

            if (!invalidProgramaIds.isEmpty()) {
                throw new IllegalArgumentException(
                        "No existen documentos de programa con ids: " + invalidProgramaIds);
            }

            List<Integer> incomingProgramaIds = body.documentosPrograma().stream()
                    .map(DOCUMENTO_ASIGNADO_CREATE::idDocrequisito)
                    .distinct()
                    .toList();

            var toDeletePrograma = existingPrograma.stream()
                    .filter(actual -> !incomingProgramaIds.contains(actual.getIdDocrequisito()))
                    .toList();

            var blockedPrograma = toDeletePrograma.stream()
                    .filter(actual -> documentoService.countByIdDocumentosrequisitoprogramacohorte(actual.getId()) > 0)
                    .map(v -> v.getId())
                    .toList();

            if (!blockedPrograma.isEmpty()) {
                throw new DomainException(CohorteErrorCode.COHORTE_CON_ASIGNACIONES_BLOQUEADAS, blockedPrograma);
            }

            toDeletePrograma.forEach(actual -> documentosrequisitoprogramacohorteService.deleteById(actual.getId()));

            var existingProgramaDocIds = existingPrograma.stream()
                    .map(v -> v.getIdDocrequisito())
                    .toList();

            incomingProgramaIds.stream()
                    .filter(idDocrequisito -> !existingProgramaDocIds.contains(idDocrequisito))
                    .forEach(idDocrequisito -> documentosrequisitoprogramacohorteService.create(
                            DocumentosrequisitoprogramacohorteDTO.builder()
                                    .idDocrequisito(idDocrequisito)
                                    .idCohorte(targetCohorteId)
                                    .build()));
        }

        List<CriteriocohorteDTO> criteriosExistentes = criteriocohorteService.findByIdCohorte(targetCohorteId);
        if (body.criteriosCohorte() == null) {
            deleteAllCriterios(criteriosExistentes);
        } else if (!sameCriterios(body.criteriosCohorte(), criteriosExistentes)) {
            Map<Integer, CriteriocohorteDTO> criteriosPorId = criteriosExistentes.stream()
                    .collect(Collectors.toMap(CriteriocohorteDTO::getId, criterio -> criterio));
            Map<Integer, CriteriocohorteDTO> criteriosPorIdCriterio = criteriosExistentes.stream()
                    .collect(Collectors.toMap(CriteriocohorteDTO::getIdCriterio, criterio -> criterio,
                            (primero, segundo) -> primero));

            Set<Integer> idsRecibidos = body.criteriosCohorte().stream()
                    .filter(java.util.Objects::nonNull)
                    .map(CRITERIOCOHORTE_DIRECTOR_UPDATE::id)
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toSet());

            criteriosExistentes.stream()
                    .filter(actual -> !idsRecibidos.contains(actual.getId()))
                    .forEach(actual -> {
                        if (calificacioncriterioService.existsByCriterio(actual.getId())) {
                            throw new DomainException(CriterioevaluacionErrorCode.CRITERIO_CON_CALIFICACIONES_BLOQUEADO,
                                    actual.getIdCriterio());
                        }
                        criteriocohorteService.deleteById(actual.getId());
                    });

            body.criteriosCohorte().stream()
                    .filter(java.util.Objects::nonNull)
                    .forEach(criterio -> {
                        if (criterio.idCriterio() == null) {
                            throw new DomainException(CriteriocohorteErrorCode.CRITERIOCOHORTE_IDCRITERIO_OBLIGATORIO,
                                    criterio);
                        }

                        CriterioevaluacionDTO criterioEvaluacion = criterioevaluacionService
                                .findById(criterio.idCriterio());
                        if (criterioEvaluacion == null) {
                            throw new DomainException(CriterioevaluacionErrorCode.CRITERIOEVALUACION_NOT_FOUND,
                                    criterio.idCriterio());
                        }
                        if (!java.util.Objects.equals(criterioEvaluacion.getIdprograma(), cohorte.getIdPrograma())) {
                            throw new DomainException(CriteriocohorteErrorCode.CRITERIO_NO_PERTENECE_AL_PROGRAMA,
                                    criterio.idCriterio());
                        }
                        if (Boolean.FALSE.equals(criterioEvaluacion.getActivo())) {
                            throw new DomainException(CriteriocohorteErrorCode.CRITERIO_INACTIVO,
                                    criterio.idCriterio());
                        }

                        if (criterio.id() != null) {
                            CriteriocohorteDTO existente = criteriosPorId.get(criterio.id());
                            if (existente == null) {
                                throw new DomainException(CriteriocohorteErrorCode.CRITERIOCOHORTE_NOT_FOUND,
                                        criterio.id());
                            }
                            if (!java.util.Objects.equals(existente.getIdCriterio(), criterio.idCriterio())) {
                                throw new DomainException(CriteriocohorteErrorCode.CRITERIOCOHORTE_MISMATCH,
                                        criterio.id());
                            }
                            if (calificacioncriterioService.existsByCriterio(existente.getId())) {
                                throw new DomainException(
                                        CriterioevaluacionErrorCode.CRITERIO_CON_CALIFICACIONES_BLOQUEADO,
                                        existente.getIdCriterio());
                            }

                            criteriocohorteService.update(existente.getId(), CriteriocohorteDTO.builder()
                                    .idCohorte(existente.getIdCohorte())
                                    .idCriterio(existente.getIdCriterio())
                                    .pesoSnapshot(criterio.pesoSnapshot())
                                    .build());
                            return;
                        }

                        if (criteriosPorIdCriterio.containsKey(criterio.idCriterio())) {
                            throw new DomainException(CriteriocohorteErrorCode.CRITERIO_YA_ASIGNADO_A_COHORTE,
                                    criterio.idCriterio());
                        }

                        criteriocohorteService.create(CriteriocohorteDTO.builder()
                                .idCohorte(targetCohorteId)
                                .idCriterio(criterio.idCriterio())
                                .pesoSnapshot(criterio.pesoSnapshot())
                                .build());
                    });
        }

        boolean activa = cohorte.getEstado() != null && "ABIERTA".equalsIgnoreCase(cohorte.getEstado().getTipo());

        return CohorteListadoOutput.builder()
                .id(targetCohorteId)
                .nombre(cohorte.getNombre())
                .activa(activa)
                .inscritos(aspiranteService.countByCohorte(targetCohorteId))
                .admitidos(admitidoService.countByCohorte(targetCohorteId))
                .cupos(cohorte.getCupos())
                .fechaLimiteDocumentos(fechaLimiteDocumentos)
                .fechaLimitePago(fechaLimitePago)
                .fechaInicio(fechaInicio)
                .build();
    }

    private Boolean sameConsejoDocuments(List<DOCUMENTO_ASIGNADO_CREATE> incoming,
            List<DocumentosrequisitoconsejocohorteDTO> existing) {
        Set<Integer> incomingIds = incoming.stream()
                .map(documento -> documento != null ? documento.idDocrequisito() : null)
                .collect(Collectors.toSet());
        if (incomingIds.contains(null)) {
            return false;
        }

        Set<Integer> existingIds = existing.stream()
                .map(DocumentosrequisitoconsejocohorteDTO::getIdDocrequisito)
                .collect(Collectors.toSet());
        return incomingIds.equals(existingIds);
    }

    private Boolean sameProgramaDocuments(List<DOCUMENTO_ASIGNADO_CREATE> incoming,
            List<DocumentosrequisitoprogramacohorteDTO> existing) {
        Set<Integer> incomingIds = incoming.stream()
                .map(documento -> documento != null ? documento.idDocrequisito() : null)
                .collect(Collectors.toSet());
        if (incomingIds.contains(null)) {
            return false;
        }

        Set<Integer> existingIds = existing.stream()
                .map(DocumentosrequisitoprogramacohorteDTO::getIdDocrequisito)
                .collect(Collectors.toSet());
        return incomingIds.equals(existingIds);
    }

    private Boolean sameCriterios(List<CRITERIOCOHORTE_DIRECTOR_UPDATE> incoming,
            List<CriteriocohorteDTO> existing) {
        Set<String> incomingSignatures = incoming.stream()
                .map(criterio -> criterio != null ? criterioSignature(criterio.idCriterio(), criterio.pesoSnapshot())
                        : null)
                .collect(Collectors.toSet());
        if (incomingSignatures.contains(null)) {
            return false;
        }

        Set<String> existingSignatures = existing.stream()
                .map(criterio -> criterioSignature(criterio.getIdCriterio(), criterio.getPesoSnapshot()))
                .collect(Collectors.toSet());
        return incomingSignatures.equals(existingSignatures);
    }

    private String criterioSignature(Integer idCriterio, BigDecimal pesoSnapshot) {
        String peso = pesoSnapshot != null ? pesoSnapshot.stripTrailingZeros().toPlainString() : "null";
        return idCriterio + "::" + peso;
    }

    private void deleteAllConsejoDocuments(List<DocumentosrequisitoconsejocohorteDTO> existingConsejo) {
        var blockedConsejo = existingConsejo.stream()
                .filter(actual -> documentoService.countByIdDocumentosrequisitoconsejocohorte(actual.getId()) > 0)
                .map(DocumentosrequisitoconsejocohorteDTO::getId)
                .toList();

        if (!blockedConsejo.isEmpty()) {
            throw new DomainException(CohorteErrorCode.COHORTE_CON_ASIGNACIONES_BLOQUEADAS, blockedConsejo);
        }

        existingConsejo.forEach(actual -> documentosrequisitoconsejocohorteService.deleteById(actual.getId()));
    }

    private void deleteAllProgramaDocuments(List<DocumentosrequisitoprogramacohorteDTO> existingPrograma) {
        var blockedPrograma = existingPrograma.stream()
                .filter(actual -> documentoService.countByIdDocumentosrequisitoprogramacohorte(actual.getId()) > 0)
                .map(DocumentosrequisitoprogramacohorteDTO::getId)
                .toList();

        if (!blockedPrograma.isEmpty()) {
            throw new DomainException(CohorteErrorCode.COHORTE_CON_ASIGNACIONES_BLOQUEADAS, blockedPrograma);
        }

        existingPrograma.forEach(actual -> documentosrequisitoprogramacohorteService.deleteById(actual.getId()));
    }

    private void deleteAllCriterios(List<CriteriocohorteDTO> criteriosExistentes) {
        criteriosExistentes.forEach(actual -> {
            if (calificacioncriterioService.existsByCriterio(actual.getId())) {
                throw new DomainException(CriterioevaluacionErrorCode.CRITERIO_CON_CALIFICACIONES_BLOQUEADO,
                        actual.getIdCriterio());
            }
            criteriocohorteService.deleteById(actual.getId());
        });
    }
}
