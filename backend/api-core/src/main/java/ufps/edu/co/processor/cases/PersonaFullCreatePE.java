package ufps.edu.co.processor.cases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufps.edu.co.processor.crud.DocumentopersonaProcessor;
import ufps.edu.co.processor.crud.PersonaProcessor;
import ufps.edu.co.processor.crud.UbicacionProcessor;
import ufps.edu.co.records.input.entity.DocumentopersonaInput.DOCUMENTOPERSONA_CREATE;
import ufps.edu.co.records.input.entity.UbicacionInput.UBICACION_CREATE;
import ufps.edu.co.records.input.entity.PersonaInput.PERSONA_CREATE;
import ufps.edu.co.records.input.entity.PersonaInput.PERSONA_FULL_CREATE;
import ufps.edu.co.records.output.entity.DocumentopersonaOutput;
import ufps.edu.co.records.output.entity.PersonaOutput;
import ufps.edu.co.records.output.entity.UbicacionOutput;

@Service
public class PersonaFullCreatePE {

    @Autowired
    private UbicacionProcessor ubicacionProcessor;

    @Autowired
    private DocumentopersonaProcessor documentopersonaProcessor;

    @Autowired
    private PersonaProcessor personaProcessor;

    @Transactional
    public PersonaOutput execute(PERSONA_FULL_CREATE input) {

        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        // Si la ubicacion viene nula, crear una Ubicacion por defecto con municipio=1 y
        // direccion='N/A'
        UBICACION_CREATE viviendaInput = input.ubicacionvivienda() != null
                ? input.ubicacionvivienda()
                : new UBICACION_CREATE("N/A", 1);
        UBICACION_CREATE nacimientoInput = input.ubicacionnacimiento() != null
                ? input.ubicacionnacimiento()
                : new UBICACION_CREATE("N/A", 1);
        UBICACION_CREATE trabajoInput = input.ubicaciontrabajo() != null
                ? input.ubicaciontrabajo()
                : new UBICACION_CREATE("N/A", 1);
        UBICACION_CREATE lugarExpedicionInput = input.lugarexpedicion() != null
                ? input.lugarexpedicion()
                : new UBICACION_CREATE("N/A", 1);

        UbicacionOutput vivienda = ubicacionProcessor.create(viviendaInput);
        UbicacionOutput nacimiento = ubicacionProcessor.create(nacimientoInput);
        UbicacionOutput ubicaciontrabajo = ubicacionProcessor.create(trabajoInput);
        UbicacionOutput lugarExpedicion = ubicacionProcessor.create(lugarExpedicionInput);

        Integer idUbicaciontrabajo = ubicaciontrabajo != null ? ubicaciontrabajo.id() : null;

        DocumentopersonaOutput documentopersona = null;
        // Sólo crear documentopersona si el usuario proporcionó número y tipo válidos
        if (input.numerodocumento() != null && !input.numerodocumento().isBlank() && input.idTipodocumento() != null) {
            documentopersona = documentopersonaProcessor.create(
                    DOCUMENTOPERSONA_CREATE.builder()
                            .numerodocumento(input.numerodocumento())
                            .idTipodocumento(input.idTipodocumento())
                            .idLugarexpedicion(
                                    lugarExpedicion != null ? lugarExpedicion.id() : null)
                            .build());
        }

        return personaProcessor.create(
                PERSONA_CREATE.builder()
                        .nombres(input.nombres())
                        .apellidos(input.apellidos())
                        .correo(input.correo())
                        .fechanacimiento(input.fechanacimiento())
                        .celular(input.celular())
                        .telefono(input.telefono())
                        .idUbicacionvivienda(
                                vivienda != null ? vivienda.id() : null)
                        .idUbicacionnacimiento(
                                nacimiento != null ? nacimiento.id() : null)
                        .idUbicaciontrabajo(
                                idUbicaciontrabajo != null ? idUbicaciontrabajo : null)
                        .idGenero(input.idGenero())
                        .idEstadocivil(input.idEstadocivil())
                        .idGrupoetnico(input.idGrupoetnico())
                        .idPoblacionindigena(input.idPoblacionindigena())
                        .idDiscapacidad(input.idDiscapacidad())
                        .idCapacidadexepcional(input.idCapacidadexepcional())
                        .idDocumentopersona(documentopersona != null ? documentopersona.id() : null)
                        .promediopregrado(input.promediopregrado())
                        .titulopregrado(input.titulopregrado())
                        .titulosposgrados(input.titulosposgrados())
                        .empresa(input.empresa())
                        .experiencialaboral(input.experiencialaboral())
                        .egresadoufps(input.egresadoufps())
                        .build());
    }
}
