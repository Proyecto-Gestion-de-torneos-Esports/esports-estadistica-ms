package Torneo.Estadistica.controller;

import Torneo.Estadistica.assemblers.EstadisticaModelAssemblers;
import Torneo.Estadistica.dto.EstadisticaRequestDTO;
import Torneo.Estadistica.dto.EstadisticaResponseDTO;
import Torneo.Estadistica.service.EstadisticaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
@Tag(name = "Estadísticas", description = "Endpoints para la gestión de estadísticas de jugadores")
public class EstadisticaController {
    private final EstadisticaService estadisticaService;
    private final EstadisticaModelAssemblers assembler;


    @Operation(summary = "Listar todas las estadísticas", description = "Retorna una lista completa de todas las estadísticas en el sistema acompañadas de sus enlaces de navegación")
    @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito")
    @PreAuthorize("hasRole('JUGADOR') or hasRole('COACH') or hasRole('ARBITRO') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EstadisticaResponseDTO>> obtenerTodas(){
        List<EstadisticaResponseDTO> lista = estadisticaService.obtenertodas().stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtener estadística por ID", description = "Busca y retorna una estadística específica utilizando su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estadística encontrada"),
            @ApiResponse(responseCode = "404", description = "Estadística no encontrada")
    })
    @PreAuthorize("hasRole('JUGADOR') or hasRole('COACH') or hasRole('ARBITRO') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EstadisticaResponseDTO> obtenerPorId(@PathVariable Long id){
        EstadisticaResponseDTO estadistica = estadisticaService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(estadistica));
    }

    @Operation(summary = "Registrar nueva estadística", description = "Crea una nueva estadística en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Estadística creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PreAuthorize("hasRole('ARBITRO') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EstadisticaResponseDTO> crear (@Valid @RequestBody EstadisticaRequestDTO dto ) {
        EstadisticaResponseDTO nuevaEstadistica = estadisticaService.registrarEstadistica(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(nuevaEstadistica));
    }

    @Operation(summary = "Actualizar estadística", description = "Modifica los datos de una estadística existente por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estadística actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Estadística no encontrada")
    })
    @PreAuthorize("hasRole('ARBITRO') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EstadisticaResponseDTO> actualizar (@PathVariable Long id, @Valid @RequestBody EstadisticaRequestDTO dto){
        EstadisticaResponseDTO estadisticaActualizada = estadisticaService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(estadisticaActualizada));
    }

    @Operation(summary = "Eliminar estadística", description = "Elimina un registro del sistema y pasa a estado inactivo.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estadística eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Estadística no encontrada")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id ) {
        estadisticaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
