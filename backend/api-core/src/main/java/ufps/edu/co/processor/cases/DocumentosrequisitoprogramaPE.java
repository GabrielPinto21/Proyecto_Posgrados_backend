package ufps.edu.co.processor.cases;

import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import ufps.edu.co.maps.specific.*;
import ufps.edu.co.processor.crud.*;
import ufps.edu.co.records.output.cases.*;
import ufps.edu.co.records.output.entity.*;
import ufps.edu.co.rest.dto.*;
import ufps.edu.co.rest.services.*;

@Service
public class DocumentosrequisitoprogramaPE extends DocumentosrequisitoprogramaProcessor {

    @Autowired
    private DocumentosrequisitoprogramaService service;

    @Autowired
    private DocumentosrequisitoprogramaMap map;

    @Autowired
    private DocumentosrequisitoconsejoService consejoService;

    @Autowired
    private DocumentosrequisitoprogramacohorteService programaCohorteService;

    @Autowired
    private DocumentosrequisitoconsejocohorteService consejoCohorteService;

    @Autowired
    private AspiranteService aspiranteService;

    @Autowired
    private PersonaService personaService;

    public List<DocumentoRequeridoOutput> findByIdCohorte(Integer idCohorte, Integer idAspirante) {
        List<DocumentoRequeridoOutput> result = new ArrayList<>();

        boolean esEgresadoUfps = false;
        if (idAspirante != null) {
            Integer idPersona = aspiranteService.findIdPersonaById(idAspirante);
            if (idPersona != null) {
                PersonaDTO persona = personaService.findById(idPersona);
                esEgresadoUfps = persona != null && Boolean.TRUE.equals(persona.getEgresadoufps());
            }
        }
        final boolean filtrarEgresado = esEgresadoUfps;

        consejoCohorteService.findByIdCohorte(idCohorte).forEach(junction -> {
            DocumentosrequisitoconsejoDTO doc = consejoService.findById(junction.getIdDocrequisito());
            if (filtrarEgresado &&
                    ("Título Profesional".equals(doc.getNombre()) || "Certificado de Notas".equals(doc.getNombre()))) {
                return;
            }
            result.add(DocumentoRequeridoOutput.builder()
                    .idDocumentosrequisitoconsejocohorte(junction.getId())
                    .idDocumentosrequisitoprogramacohorte(null)
                    .nombre(doc.getNombre())
                    .urlformato(doc.getUrlformato())
                    .tamanoMaximoMB(doc.getTamanomaximo())
                    .build());
        });

        programaCohorteService.findByIdCohorte(idCohorte).forEach(junction -> {
            var doc = service.findById(junction.getIdDocrequisito());
            result.add(DocumentoRequeridoOutput.builder()
                    .idDocumentosrequisitoconsejocohorte(null)
                    .idDocumentosrequisitoprogramacohorte(junction.getId())
                    .nombre(doc.getNombre())
                    .urlformato(doc.getUrlformato())
                    .tamanoMaximoMB(doc.getTamanomaximo())
                    .build());
        });

        return result;
    }

    public List<DocumentosrequisitoprogramaOutput> findByIdPrograma(Integer idPrograma) {
        try {
            return service.findByIdPrograma(idPrograma).stream().map(map::toOutput).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding Documentosrequisitoprograma by programa: " + e.getMessage(), e);
        }
    }

    public Listdocumentosprogramaconsejo findByIdProgramaAndConsejo(Integer idPrograma) {
        try {
            List<DocumentosrequisitoprogramaOutput> documentosPrograma = findByIdPrograma(idPrograma);
            List<DocumentosrequisitoprogramaOutput> documentosConsejo = consejoService.findAll()
                    .stream()
                    .map(doc -> toProgramaOutput(doc, idPrograma))
                    .toList();

            return Listdocumentosprogramaconsejo.builder()
                    .documentosConsejo(documentosConsejo)
                    .documentosPrograma(documentosPrograma)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error finding Documentosrequisitoprograma by programa y consejo: " + e.getMessage(), e);
        }
    }

    private DocumentosrequisitoprogramaOutput toProgramaOutput(DocumentosrequisitoconsejoDTO consejoDoc,
            Integer idPrograma) {
        return DocumentosrequisitoprogramaOutput.builder()
                .id(consejoDoc.getId())
                .nombre(consejoDoc.getNombre())
                .tamanomaximo(consejoDoc.getTamanomaximo())
                .urlformato(consejoDoc.getUrlformato())
                .id_programa(idPrograma)
                .build();
    }
}
