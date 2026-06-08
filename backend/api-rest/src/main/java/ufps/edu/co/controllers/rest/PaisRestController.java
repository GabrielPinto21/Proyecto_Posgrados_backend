package ufps.edu.co.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufps.edu.co.processor.crud.PaisProcessor;
import ufps.edu.co.records.input.entity.PaisInput.*;
import ufps.edu.co.records.output.entity.PaisOutput;

@RestController
@RequestMapping(value = "/pais", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaisRestController {

    @Autowired
    private PaisProcessor processor;
    
    @PostMapping(value = "/list", consumes   = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaisOutput> findById(@RequestBody PAIS_FIND find) {
        PaisOutput paisOutput = processor.findById(find);
        if (paisOutput != null) {
            return ResponseEntity.ok(paisOutput); // 200 OK
        } else {
            return ResponseEntity.notFound().build(); // 404 Not found
        }
    }

    @PostMapping("/create")
    public ResponseEntity<PaisOutput> create(@RequestBody PAIS_CREATE request) {
        PaisOutput output = processor.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @PutMapping("/update")
    public ResponseEntity<PaisOutput> update(@RequestBody PAIS_UPDATE request) {
        try {
            PaisOutput updated = processor.update(request);
            return ResponseEntity.ok(updated); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 404 Not found
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteById(@RequestBody PAIS_DELETE request) {
        try {
            processor.deleteById(request);
            return ResponseEntity.noContent().build(); // 204 No content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 404 Not found
        }
    }
}
