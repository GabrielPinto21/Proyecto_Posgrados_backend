package ufps.edu.co.controllers.cases.administrativo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufps.edu.co.processor.cases.PersonaFullCreatePE;
import ufps.edu.co.records.input.entity.PersonaInput.PERSONA_FULL_CREATE;
import ufps.edu.co.records.output.entity.PersonaOutput;

@RestController
@RequestMapping(value = "/superadmin/persona", produces = MediaType.APPLICATION_JSON_VALUE)
public class SuperAdminCase {

    @Autowired
    private PersonaFullCreatePE processor;

    @PostMapping(value = "/crear-completo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonaOutput> crearPersonaCompleto(@RequestBody PERSONA_FULL_CREATE request) {
        PersonaOutput output = processor.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }
}
