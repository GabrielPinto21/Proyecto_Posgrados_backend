package ufps.edu.co.controllers.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ufps.edu.co.processor.crud.UltimocodigoprogramaProcessor;
import ufps.edu.co.records.input.entity.UltimocodigoprogramaInput.*;
import ufps.edu.co.records.output.entity.*;

@RestController
@RequestMapping(value = "/ultimocodigoprograma", produces = MediaType.APPLICATION_JSON_VALUE)
public class UltimocodigoprogramaRestController {

    @Autowired
    private UltimocodigoprogramaProcessor processor;

    @GetMapping("/listall")
    public ResponseEntity<List<UltimocodigoprogramaOutput>> findAll() {
        return ResponseEntity.ok(processor.findAll());
    }

    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UltimocodigoprogramaOutput> findById(@RequestBody ULTIMOCODIGOPROGRAMA_FIND request) {
        UltimocodigoprogramaOutput output = processor.findById(request);
        if (output != null) {
            return ResponseEntity.ok(output);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/programa", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UltimocodigoprogramaOutput> findByIdPrograma(
            @RequestBody ULTIMOCODIGOPROGRAMA_FIND_BY_PROGRAMA request) {
        UltimocodigoprogramaOutput output = processor.findByIdPrograma(request.idPrograma());
        if (output != null) {
            return ResponseEntity.ok(output);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UltimocodigoprogramaOutput> create(@RequestBody ULTIMOCODIGOPROGRAMA_CREATE request) {
        UltimocodigoprogramaOutput output = processor.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UltimocodigoprogramaOutput> update(@RequestBody ULTIMOCODIGOPROGRAMA_UPDATE request) {
        try {
            return ResponseEntity.ok(processor.update(request));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteById(@RequestBody ULTIMOCODIGOPROGRAMA_DELETE request) {
        try {
            processor.deleteById(request);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
