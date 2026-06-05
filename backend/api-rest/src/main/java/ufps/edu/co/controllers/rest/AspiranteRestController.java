package ufps.edu.co.controllers.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import ufps.edu.co.processor.crud.AspiranteProcessor;
import ufps.edu.co.records.input.entity.AspiranteInput.*;
import ufps.edu.co.records.output.entity.AspiranteOutput;
import ufps.edu.co.records.output.entity.EstadoOutput;

@RestController
@RequestMapping(value = "/aspirante", produces = MediaType.APPLICATION_JSON_VALUE)
public class AspiranteRestController {

    @Autowired
    private AspiranteProcessor processor;

    @GetMapping("/listall")
    public ResponseEntity<List<AspiranteOutput>> findAll() {
        List<AspiranteOutput> list = processor.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/listall/paged")
    public ResponseEntity<Page<AspiranteOutput>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(processor.findAll(PageRequest.of(page, size)));
    }

    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AspiranteOutput> findById(@Valid @RequestBody ASPIRANTE_FIND request) {
        AspiranteOutput output = processor.findById(request);
        if (output != null) {
            return ResponseEntity.ok(output);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<AspiranteOutput> create(@Valid @RequestBody ASPIRANTE_CREATE request) {
        AspiranteOutput output = processor.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @PutMapping("/update")
    public ResponseEntity<AspiranteOutput> update(@Valid @RequestBody ASPIRANTE_UPDATE request) {
        try {
            AspiranteOutput updated = processor.update(request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteById(@Valid @RequestBody ASPIRANTE_DELETE request) {
        try {
            processor.deleteById(request);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/estado", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EstadoOutput> findEstadoById(@Valid @RequestBody ASPIRANTE_FIND request) {
        EstadoOutput estado = processor.findEstadoById(request);
        if (estado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estado);
    }
}
