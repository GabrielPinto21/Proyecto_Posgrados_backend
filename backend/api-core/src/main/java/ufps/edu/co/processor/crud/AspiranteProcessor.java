package ufps.edu.co.processor.crud;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ufps.edu.co.domain.exceptions.DomainException;
import ufps.edu.co.domain.exceptions.errorcodes.CohorteErrorCode;
import ufps.edu.co.maps.specific.AspiranteMap;
import ufps.edu.co.maps.specific.EstadoMap;
import ufps.edu.co.records.input.entity.AspiranteInput.*;
import ufps.edu.co.records.output.entity.AspiranteCalificacionOutput;
import ufps.edu.co.records.output.entity.RankingAdmitidosOutput;
import ufps.edu.co.records.output.entity.AspiranteCohorteOutput;
import ufps.edu.co.records.output.entity.AspiranteCriteriosOutput;
import ufps.edu.co.records.output.entity.AspiranteOutput;
import ufps.edu.co.records.output.entity.CriterioFilaOutput;
import ufps.edu.co.records.output.entity.EstadoOutput;
import ufps.edu.co.records.output.entity.PasoProcesoOutput;
import ufps.edu.co.rest.dto.*;
import ufps.edu.co.rest.services.DocumentoService;
import ufps.edu.co.rest.services.AdmitidoService;
import ufps.edu.co.rest.services.AspiranteService;
import ufps.edu.co.rest.services.CohorteService;
import ufps.edu.co.rest.services.CriteriocohorteService;
import ufps.edu.co.usecase.GlobalUseCase;

@Service
public class AspiranteProcessor implements
        GlobalUseCase<ASPIRANTE_CREATE, ASPIRANTE_UPDATE, ASPIRANTE_DELETE, ASPIRANTE_PATCH, ASPIRANTE_FIND, AspiranteOutput> {

    @Autowired
    private AspiranteService service;

    @Autowired
    private AspiranteMap map;

    @Autowired
    private EstadoMap estadoMap;

    @Autowired
    private CriteriocohorteService criteriocohorteService;

    @Autowired
    private CohorteService cohorteService;

    @Autowired
    private AdmitidoService admitidoService;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private PagoProcessor pagoProcessor;

    @Override
    public AspiranteOutput create(ASPIRANTE_CREATE input) {
        AspiranteDTO dto = map.toDto(input);
        AspiranteDTO created = service.create(dto);
        pagoProcessor.ensureInitialPaymentsForAspirante(created.getId());
        return map.toOutput(created);
    }

    @Override
    public AspiranteOutput update(ASPIRANTE_UPDATE input) {
        AspiranteDTO dto = map.toDto(input);
        AspiranteDTO updated = service.update(input.id(), dto);
        return map.toOutput(updated);
    }

    @Override
    public AspiranteOutput patch(ASPIRANTE_PATCH input) {
        throw new UnsupportedOperationException("Patch operation is not supported for Aspirante");
    }

    @Override
    public AspiranteOutput findById(ASPIRANTE_FIND input) {
        AspiranteDTO dto = service.findById(input.id());
        return map.toOutput(dto);
    }

    @Override
    public List<AspiranteOutput> findAll() {
        return service.findAll().stream().map(map::toOutput).toList();
    }

    public Page<AspiranteOutput> findAll(Pageable pageable) {
        return service.findAll(pageable).map(map::toOutput);
    }

    @Override
    public void deleteById(ASPIRANTE_DELETE input) {
        service.deleteById(input.id());
    }

    public List<AspiranteOutput> findWithDocuments() {
        return service.findWithDocuments().stream().map(map::toOutput).toList();
    }

    public long countValidados() {
        return service.countValidados();
    }

    public long countPorCalificar() {
        return service.countPorCalificar();
    }

    public long countCalificados() {
        return service.countCalificados();
    }

    public List<AspiranteCalificacionOutput> findAllValidadosCalificacion() {
        List<AspiranteDTO> validados = service.findWithDocuments();

        return validados.stream().map(aspirante -> {
            PersonaDTO persona = aspirante.getPersona();

            String nombreCompleto = persona != null
                    ? ((persona.getNombres() != null ? persona.getNombres() : "") + " "
                            + (persona.getApellidos() != null ? persona.getApellidos() : "")).trim()
                    : "";

            return AspiranteCalificacionOutput.builder()
                    .id(aspirante.getId())
                    .nombreCompleto(nombreCompleto)
                    .idEstado(aspirante.getIdEstado())
                    .estado(aspirante.getEstado() != null ? aspirante.getEstado().getTipo() : null)
                    .correo(persona != null ? persona.getCorreo() : null)
                    .puntajeTotal(aspirante.getPuntuacion())
                    .build();
        }).toList();
    }

    public AspiranteCriteriosOutput findCriteriosCalificacion(ASPIRANTE_FIND input) {
        Integer idCohorte = service.findIdCohorteById(input.id());
        if (idCohorte == null) {
            return AspiranteCriteriosOutput.builder()
                    .criterios(List.of())
                    .puntajeTotal(null)
                    .build();
        }
        BigDecimal puntuacion = service.findPuntuacionById(input.id());

        List<CriterioFilaOutput> filas = criteriocohorteService
                .findCriteriosConCalificacion(idCohorte, input.id())
                .stream()
                .map(v -> CriterioFilaOutput.builder()
                        .id(v.getId())
                        .nombreCriterio(v.getNombreCriterio())
                        .peso(v.getPesoSnapshot())
                        .puntajeObtenido(v.getPuntajeObtenido())
                        .build())
                .toList();

        return AspiranteCriteriosOutput.builder()
                .criterios(filas)
                .puntajeTotal(puntuacion)
                .build();
    }

    public EstadoOutput findEstadoById(ASPIRANTE_FIND input) {
        AspiranteDTO dto = service.findById(input.id());
        if (dto == null || dto.getEstado() == null) {
            return null;
        }
        return estadoMap.toOutput(dto.getEstado());
    }

    public List<AspiranteOutput> findPazYSalvoByCohorte(Integer cohorteId) {
        return service.findByCohorte(cohorteId).stream()
                .filter(a -> a.getEstado() != null
                        && "PAZ Y SALVO".equalsIgnoreCase(a.getEstado().getTipo()))
                .map(map::toOutput)
                .toList();
    }

    public List<PasoProcesoOutput> getPasosProceso(Integer idAspirante) {
        String estadoTipo = service.findEstadoTipoById(idAspirante);
        String estado = estadoTipo != null ? estadoTipo.toUpperCase() : "";

        /**
         * Lógica de asignación de estados a cada paso del proceso según el estado del
         * aspirante.
         * s1 = estado del paso 1 (Inscripción)
         * s2 = estado del paso 2 (Pago)
         * s3 = estado del paso 3 (Documentos)
         * s4 = estado del paso 4 (Calificación)
         * s5 = estado del paso 5 (Resultado)
         * s6 = estado del paso 6 (Legalización)
         */
        String s1, s2, s3, s4, s5, s6;
        switch (estado) {
            case "NO CONFIRMADO" -> {
                s1 = "en progreso";
                s2 = "pendiente";
                s3 = "pendiente";
                s4 = "pendiente";
                s5 = "pendiente";
                s6 = "pendiente";
            }
            case "INSCRITO" -> {
                s1 = "completado";
                s2 = "en progreso";
                s3 = "pendiente";
                s4 = "pendiente";
                s5 = "pendiente";
                s6 = "pendiente";
            }
            case "PAZ Y SALVO" -> {
                s1 = "completado";
                s2 = "completado";
                s3 = "en progreso";
                s4 = "pendiente";
                s5 = "pendiente";
                s6 = "pendiente";
            }
            case "VALIDADO_POR_CALIFICAR" -> {
                s1 = "completado";
                s2 = "completado";
                s3 = "completado";
                s4 = "pendiente";
                s5 = "pendiente";
                s6 = "pendiente";
            }
            case "VALIDADO_EN_PROGRESO" -> {
                s1 = "completado";
                s2 = "completado";
                s3 = "completado";
                s4 = "en progreso";
                s5 = "pendiente";
                s6 = "pendiente";
            }
            case "VALIDADO_CALIFICADO" -> {
                s1 = "completado";
                s2 = "completado";
                s3 = "completado";
                s4 = "completado";
                s5 = "en progreso";
                s6 = "pendiente";
            }
            case "ADMITIDO" -> {
                s1 = "completado";
                s2 = "completado";
                s3 = "completado";
                s4 = "completado";
                s5 = "en progreso";
                s6 = "pendiente";
            }
            case "POR LEGALIZAR" -> {
                s1 = "completado";
                s2 = "completado";
                s3 = "completado";
                s4 = "completado";
                s5 = "completado";
                s6 = "en progreso";
            }
            case "LEGALIZADO" -> {
                s1 = "completado";
                s2 = "completado";
                s3 = "completado";
                s4 = "completado";
                s5 = "completado";
                s6 = "completado";
            }
            default -> {
                s1 = "completado";
                s2 = "pendiente";
                s3 = "pendiente";
                s4 = "pendiente";
                s5 = "pendiente";
                s6 = "pendiente";
            }
        }

        return List.of(
                PasoProcesoOutput.builder().id(1).name("Inscripción").status(s1).build(),
                PasoProcesoOutput.builder().id(2).name("Pago").status(s2).build(),
                PasoProcesoOutput.builder().id(3).name("Documentos").status(s3).build(),
                PasoProcesoOutput.builder().id(4).name("Calificación").status(s4).build(),
                PasoProcesoOutput.builder().id(5).name("Resultado").status(s5).build(),
                PasoProcesoOutput.builder().id(6).name("Legalización").status(s6).build());
    }

    public List<AspiranteCalificacionOutput> findAllValidadosCalificacion(Integer cohorteId) {
        return service.findValidadosByCohorte(cohorteId).stream()
                .map(aspirante -> {
                    PersonaDTO persona = aspirante.getPersona();
                    String nombreCompleto = persona != null
                            ? ((persona.getNombres() != null ? persona.getNombres() : "") + " "
                                    + (persona.getApellidos() != null ? persona.getApellidos() : "")).trim()
                            : "";
                    String numerodocumento = (persona != null && persona.getDocumentopersona() != null)
                            ? persona.getDocumentopersona().getNumerodocumento()
                            : null;
                    return AspiranteCalificacionOutput.builder()
                            .id(aspirante.getId())
                            .nombreCompleto(nombreCompleto)
                            .idEstado(aspirante.getIdEstado())
                            .estado(aspirante.getEstado() != null ? aspirante.getEstado().getTipo() : null)
                            .correo(persona != null ? persona.getCorreo() : null)
                            .puntajeTotal(aspirante.getPuntuacion())
                            .numerodocumento(numerodocumento)
                            .build();
                }).toList();
    }

    public long countValidados(Integer cohorteId) {
        return service.countValidadosByCohorte(cohorteId);
    }

    public long countPorCalificar(Integer cohorteId) {
        return service.countPorCalificarByCohorte(cohorteId);
    }

    public long countCalificados(Integer cohorteId) {
        return service.countCalificadosByCohorte(cohorteId);
    }

    public List<AspiranteCohorteOutput> findByCohorteConResumen(Integer cohorteId) {
        return service.findByCohorte(cohorteId).stream().map(aspirante -> {
            PersonaDTO p = aspirante.getPersona();
            String nombre = p != null
                    ? ((p.getNombres() != null ? p.getNombres() : "") + " "
                            + (p.getApellidos() != null ? p.getApellidos() : "")).trim()
                    : "";
            String cedula = p != null && p.getDocumentopersona() != null
                    && p.getDocumentopersona().getNumerodocumento() != null
                            ? p.getDocumentopersona().getNumerodocumento().toString()
                            : null;

            List<DocumentoDTO> docs = documentoService.findByIdAspirante(aspirante.getId());
            long total = docs.size();
            long validados = docs.stream()
                    .filter(d -> d.getEstadodocumento() != null
                            && "APROBADO".equalsIgnoreCase(d.getEstadodocumento().getEstado()))
                    .count();

            return AspiranteCohorteOutput.builder()
                    .id(aspirante.getId())
                    .nombre(nombre)
                    .cedula(cedula)
                    .correo(p != null ? p.getCorreo() : null)
                    .documentosValidados(validados)
                    .totalDocumentos(total)
                    .estadoGeneral(aspirante.getEstado().getTipo())
                    .build();
        }).toList();
    }

    public List<AspiranteCohorteOutput> findAValidarByCohorte(Integer cohorteId) {
        return service.findAValidarByCohorte(cohorteId).stream().map(aspirante -> {
            PersonaDTO p = aspirante.getPersona();
            String nombre = p != null
                    ? ((p.getNombres() != null ? p.getNombres() : "") + " "
                            + (p.getApellidos() != null ? p.getApellidos() : "")).trim()
                    : "";
            String cedula = p != null && p.getDocumentopersona() != null
                    && p.getDocumentopersona().getNumerodocumento() != null
                            ? p.getDocumentopersona().getNumerodocumento().toString()
                            : null;

            List<DocumentoDTO> docs = documentoService.findByIdAspirante(aspirante.getId());
            long total = docs.size();
            long validados = docs.stream()
                    .filter(d -> d.getEstadodocumento() != null
                            && "APROBADO".equalsIgnoreCase(d.getEstadodocumento().getEstado()))
                    .count();

            return AspiranteCohorteOutput.builder()
                    .id(aspirante.getId())
                    .nombre(nombre)
                    .cedula(cedula)
                    .correo(p != null ? p.getCorreo() : null)
                    .documentosValidados(validados)
                    .totalDocumentos(total)
                    .estadoGeneral(aspirante.getEstado().getTipo())
                    .build();
        }).toList();
    }

    public RankingAdmitidosOutput getRankingAdmitidos(Integer cohorteId) {
        CohorteDTO cohorte = cohorteService.findById(cohorteId);
        if (cohorte == null) {
            throw new DomainException(CohorteErrorCode.COHORTE_NOT_FOUND, cohorteId);
        }
        boolean activa = cohorte.getEstado() != null
                && "ABIERTA".equalsIgnoreCase(cohorte.getEstado().getTipo());

        var admitidosList = admitidoService.findByCohorte(cohorte.getId());
        @SuppressWarnings("unused") Set<Integer> admitidosIds = admitidosList.stream()
                .map(a -> a.getIdAspirante())
                .collect(Collectors.toSet());

        long totalAdmitidos = admitidosList.size();
        int cuposDisponibles = Math.max(0, cohorte.getCupos() - (int) totalAdmitidos);

        List<RankingAdmitidosOutput.AspiranteResumen> aspirantesResumen = service.findByCohorte(cohorte.getId())
                .stream()
                .filter(a -> a.getEstado() != null
                        && ("VALIDADO_CALIFICADO".equalsIgnoreCase(a.getEstado().getTipo())
                                || "CANCELADO".equalsIgnoreCase(a.getEstado().getTipo())
                                || "POR LEGALIZAR".equalsIgnoreCase(a.getEstado().getTipo())
                                || "LEGALIZADO".equalsIgnoreCase(a.getEstado().getTipo())
                                || "ADMITIDO".equalsIgnoreCase(a.getEstado().getTipo())))
                .sorted(Comparator.comparing(AspiranteDTO::getPuntuacion,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(a -> {
                    PersonaDTO persona = a.getPersona();
                    String nombre = persona != null
                            ? ((persona.getNombres() != null ? persona.getNombres() : "") + " "
                                    + (persona.getApellidos() != null ? persona.getApellidos() : "")).trim()
                            : "";
                    return RankingAdmitidosOutput.AspiranteResumen.builder()
                            .id(a.getId())
                            .nombre(nombre)
                            .correo(persona != null ? persona.getCorreo() : null)
                            .puntaje(a.getPuntuacion())
                            .admitido(
                                ("ADMITIDO".equalsIgnoreCase(a.getEstado().getTipo()))
                                || ("POR LEGALIZAR".equalsIgnoreCase(a.getEstado().getTipo()))
                                || ("LEGALIZADO".equalsIgnoreCase(a.getEstado().getTipo()))
                            )
                            .build();
                })
                .toList();

        return RankingAdmitidosOutput.builder()
                .cohorteActual(RankingAdmitidosOutput.CohorteResumen.builder()
                        .id(cohorte.getId())
                        .nombre(cohorte.getNombre())
                        .activa(activa)
                        .cuposDisponibles(cuposDisponibles)
                        .totalAdmitidos(totalAdmitidos)
                        .build())
                .aspirantes(aspirantesResumen)
                .build();
    }

    public AspiranteCriteriosOutput getCriteriosAspirante(Integer idAspirante) {
        return findCriteriosCalificacion(new ASPIRANTE_FIND(idAspirante));
    }
}
