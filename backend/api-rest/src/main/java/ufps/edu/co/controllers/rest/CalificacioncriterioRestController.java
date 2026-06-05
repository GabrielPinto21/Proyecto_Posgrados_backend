package ufps.edu.co.controllers.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ufps.edu.co.processor.crud.CalificacioncriterioProcessor;
import ufps.edu.co.records.input.entity.CalificacioncriterioInput.*;
import ufps.edu.co.records.output.entity.CalificacioncriterioOutput;

@RestController
@RequestMapping(value = "/calificacioncriterio", produces = MediaType.APPLICATION_JSON_VALUE)
public class CalificacioncriterioRestController {

    @Autowired
    private CalificacioncriterioProcessor processor;

    @GetMapping("/listall")
    public ResponseEntity<List<CalificacioncriterioOutput>> findAll() {
        List<CalificacioncriterioOutput> list = processor.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/listall/paged")
    public ResponseEntity<Page<CalificacioncriterioOutput>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(processor.findAll(PageRequest.of(page, size)));
    }

    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalificacioncriterioOutput> findById(@RequestBody CALIFICACIONCRITERIO_FIND request) {
        CalificacioncriterioOutput output = processor.findById(request);
        if (output != null) {
            return ResponseEntity.ok(output);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/aspirante", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CalificacioncriterioOutput>> findByIdAspirante(
            @RequestBody CALIFICACIONCRITERIO_FIND_BY_ASPIRANTE request) {
        List<CalificacioncriterioOutput> list = processor.findByIdAspirante(request.idAspirante());
        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/criterio", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CalificacioncriterioOutput>> findByIdCriterio(
            @RequestBody CALIFICACIONCRITERIO_FIND_BY_CRITERIO request) {
        List<CalificacioncriterioOutput> list = processor.findByIdCriterio(request.idCriterio());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/create")
    public ResponseEntity<CalificacioncriterioOutput> create(@RequestBody CALIFICACIONCRITERIO_CREATE request) {
        CalificacioncriterioOutput output = processor.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @PutMapping("/update")
    public ResponseEntity<CalificacioncriterioOutput> update(@RequestBody CALIFICACIONCRITERIO_UPDATE request) {
        try {
            CalificacioncriterioOutput updated = processor.update(request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteById(@RequestBody CALIFICACIONCRITERIO_DELETE request) {
        try {
            processor.deleteById(request);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
