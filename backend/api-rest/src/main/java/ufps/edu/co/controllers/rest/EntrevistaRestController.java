package ufps.edu.co.controllers.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ufps.edu.co.processor.crud.EntrevistaProcessor;
import ufps.edu.co.records.input.entity.EntrevistaInput.*;
import ufps.edu.co.records.output.entity.EntrevistaOutput;

@RestController
@RequestMapping(value = "/entrevista", produces = MediaType.APPLICATION_JSON_VALUE)
public class EntrevistaRestController {

    @Autowired
    private EntrevistaProcessor processor;

    @GetMapping("/listall")
    public ResponseEntity<List<EntrevistaOutput>> findAll() {
        List<EntrevistaOutput> list = processor.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/listall/paged")
    public ResponseEntity<Page<EntrevistaOutput>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(processor.findAll(PageRequest.of(page, size)));
    }

    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntrevistaOutput> findById(@RequestBody ENTREVISTA_FIND request) {
        EntrevistaOutput output = processor.findById(request);
        if (output != null) {
            return ResponseEntity.ok(output);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<EntrevistaOutput> create(@RequestBody ENTREVISTA_CREATE request) {
        EntrevistaOutput output = processor.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @PutMapping("/update")
    public ResponseEntity<EntrevistaOutput> update(@RequestBody ENTREVISTA_UPDATE request) {
        try {
            EntrevistaOutput updated = processor.update(request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteById(@RequestBody ENTREVISTA_DELETE request) {
        try {
            processor.deleteById(request);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
