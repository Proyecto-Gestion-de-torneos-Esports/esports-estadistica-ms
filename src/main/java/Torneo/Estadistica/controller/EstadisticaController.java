package Torneo.Estadistica.controller;

import Torneo.Estadistica.dto.EstadisticaRequestDTO;
import Torneo.Estadistica.dto.EstadisticaResponseDTO;
import Torneo.Estadistica.service.EstadisticasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estadistica")
@RequiredArgsConstructor
public class EstadisticaController {
    private final EstadisticasService estadisticasService;

    @GetMapping
    public ResponseEntity<List<EstadisticaResponseDTO>> obtenerTodas(){
        return ResponseEntity.ok(estadisticasService.obtenertodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadisticaResponseDTO> obtenerPorId(@PathVariable Long id){
        return estadisticasService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EstadisticaResponseDTO> crear (@Valid @RequestBody EstadisticaRequestDTO dto ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadisticasService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadisticaResponseDTO> actualizar (@PathVariable Long id, @Valid @RequestBody EstadisticaRequestDTO dto){
        return estadisticasService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id ) {
        if (estadisticasService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        estadisticasService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
