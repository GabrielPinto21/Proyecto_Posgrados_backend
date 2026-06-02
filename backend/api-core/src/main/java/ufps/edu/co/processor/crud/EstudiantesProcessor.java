package ufps.edu.co.processor.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufps.edu.co.records.input.entity.EstudiantesInput.ESTUDIANTE_CREATE;
import ufps.edu.co.records.input.entity.EstudiantesInput.ESTUDIANTE_UPDATE;
import ufps.edu.co.records.output.entity.EstudiantesOutput;
import ufps.edu.co.records.output.entity.ProgramaOutput;
import ufps.edu.co.rest.dto.EstudiantesDTO;
import ufps.edu.co.rest.dto.ProgramaDTO;
import ufps.edu.co.rest.services.EstudiantesService;
import ufps.edu.co.rest.services.ProgramaService;

@Service
public class EstudiantesProcessor {

    @Autowired
    private EstudiantesService estudiantesService;

    @Autowired
    private ProgramaService programaService;

    public List<EstudiantesOutput> findAll() {
        return estudiantesService.findAll().stream()
                .map(this::toOutput)
                .toList();
    }

    public EstudiantesOutput findById(Integer id) {
        EstudiantesDTO dto = estudiantesService.findById(id);
        if (dto == null) return null;
        return toOutput(dto);
    }

    public List<EstudiantesOutput> findByProgramaId(Integer programaId) {
        return estudiantesService.findByProgramaId(programaId).stream()
                .map(this::toOutput)
                .toList();
    }

    public List<EstudiantesOutput> findByCohorteId(Integer cohorteId) {
        return estudiantesService.findByCohorteId(cohorteId).stream()
                .map(this::toOutput)
                .toList();
    }

    public List<EstudiantesOutput> findByProgramaIdAndCohorteId(Integer programaId, Integer cohorteId) {
        return estudiantesService.findByProgramaIdAndCohorteId(programaId, cohorteId).stream()
                .map(this::toOutput)
                .toList();
    }

    public EstudiantesOutput create(ESTUDIANTE_CREATE body) {
        if (estudiantesService.existsByCedula(body.cedula())) {
            throw new IllegalArgumentException("Ya existe un estudiante con la cédula: " + body.cedula());
        }
        EstudiantesDTO dto = EstudiantesDTO.builder()
                .apellido(body.apellido())
                .apellido2(body.apellido2())
                .cedula(body.cedula())
                .codigo(body.codigo())
                .email(body.email())
                .esposgrado(body.esposgrado())
                .fechalngreso(body.fechalngreso())
                .fechanacimiento(body.fechanacimiento())
                .migrado(body.migrado())
                .moodleld(body.moodleld())
                .nombre(body.nombre())
                .nombre2(body.nombre2())
                .telefono(body.telefono())
                .cohorteId(body.cohorteId())
                .estadoEstudianteId(body.estadoEstudianteId())
                .pensumId(body.pensumId())
                .programaId(body.programaId())
                .usuarioId(body.usuarioId())
                .build();
        return toOutput(estudiantesService.create(dto));
    }

    public EstudiantesOutput update(Integer id, ESTUDIANTE_UPDATE body) {
        if (estudiantesService.existsByCedulaAndIdNot(body.cedula(), id)) {
            throw new IllegalArgumentException("Ya existe otro estudiante con la cédula: " + body.cedula());
        }
        EstudiantesDTO dto = EstudiantesDTO.builder()
                .apellido(body.apellido())
                .apellido2(body.apellido2())
                .cedula(body.cedula())
                .codigo(body.codigo())
                .email(body.email())
                .esposgrado(body.esposgrado())
                .fechalngreso(body.fechalngreso())
                .fechanacimiento(body.fechanacimiento())
                .migrado(body.migrado())
                .moodleld(body.moodleld())
                .nombre(body.nombre())
                .nombre2(body.nombre2())
                .telefono(body.telefono())
                .cohorteId(body.cohorteId())
                .estadoEstudianteId(body.estadoEstudianteId())
                .pensumId(body.pensumId())
                .programaId(body.programaId())
                .usuarioId(body.usuarioId())
                .build();
        return toOutput(estudiantesService.update(id, dto));
    }

    public void delete(Integer id) {
        estudiantesService.deleteById(id);
    }

    private EstudiantesOutput toOutput(EstudiantesDTO dto) {
        ProgramaOutput programaOutput = null;
        if (dto.getProgramaId() != null) {
            ProgramaDTO prog = programaService.findById(dto.getProgramaId());
            if (prog != null) {
                programaOutput = ProgramaOutput.builder()
                        .id(prog.getId())
                        .nombre(prog.getNombre())
                        .codigo(prog.getCodigo())
                        .build();
            }
        }
        return EstudiantesOutput.builder()
                .id(dto.getId())
                .apellido(dto.getApellido())
                .apellido2(dto.getApellido2())
                .cedula(dto.getCedula())
                .codigo(dto.getCodigo())
                .email(dto.getEmail())
                .esposgrado(dto.getEsposgrado())
                .fechalngreso(dto.getFechalngreso())
                .fechanacimiento(dto.getFechanacimiento())
                .migrado(dto.getMigrado())
                .moodleld(dto.getMoodleld())
                .nombre(dto.getNombre())
                .nombre2(dto.getNombre2())
                .telefono(dto.getTelefono())
                .cohorteId(dto.getCohorteId())
                .estadoEstudianteId(dto.getEstadoEstudianteId())
                .pensumId(dto.getPensumId())
                .programaId(dto.getProgramaId())
                .usuarioId(dto.getUsuarioId())
                .programa(programaOutput)
                .build();
    }
}
