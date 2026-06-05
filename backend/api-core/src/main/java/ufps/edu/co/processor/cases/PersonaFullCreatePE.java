package ufps.edu.co.processor.cases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufps.edu.co.processor.crud.DocumentopersonaProcessor;
import ufps.edu.co.processor.crud.PersonaProcessor;
import ufps.edu.co.processor.crud.UbicacionProcessor;
import ufps.edu.co.records.input.entity.DocumentopersonaInput.DOCUMENTOPERSONA_CREATE;
import ufps.edu.co.records.input.entity.PersonaInput.PERSONA_CREATE;
import ufps.edu.co.records.input.entity.PersonaInput.PERSONA_FULL_CREATE;
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
        UbicacionOutput vivienda = ubicacionProcessor.create(input.ubicacionvivienda());
        UbicacionOutput nacimiento = ubicacionProcessor.create(input.ubicacionnacimiento());

        Integer idUbicaciontrabajo = null;
        if (input.ubicaciontrabajo() != null) {
            idUbicaciontrabajo = ubicacionProcessor.create(input.ubicaciontrabajo()).id();
        }

        UbicacionOutput lugarExpedicion = ubicacionProcessor.create(input.lugarexpedicion());

        Integer idDocumentopersona = documentopersonaProcessor.create(
                new DOCUMENTOPERSONA_CREATE(
                        input.numerodocumento(),
                        input.idTipodocumento(),
                        lugarExpedicion.id())).id();

        return personaProcessor.create(
                new PERSONA_CREATE(
                        input.nombres(),
                        input.apellidos(),
                        input.correo(),
                        input.fechanacimiento(),
                        input.celular(),
                        input.telefono(),
                        vivienda.id(),
                        nacimiento.id(),
                        idUbicaciontrabajo,
                        input.idGenero(),
                        input.idEstadocivil(),
                        input.idGrupoetnico(),
                        input.idPoblacionindigena(),
                        input.idDiscapacidad(),
                        input.idCapacidadexepcional(),
                        idDocumentopersona,
                        input.promediopregrado(),
                        input.titulopregrado(),
                        input.titulosposgrados(),
                        input.empresa(),
                        input.experiencialaboral(),
                        input.egresadoufps()));
    }
}
