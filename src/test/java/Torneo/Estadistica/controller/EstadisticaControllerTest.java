package Torneo.Estadistica.controller;


import Torneo.Estadistica.dto.EstadisticaRequestDTO;
import Torneo.Estadistica.dto.EstadisticaResponseDTO;

import Torneo.Estadistica.service.EstadisticaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstadisticaController.class)

public class EstadisticaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EstadisticaService estadisticaService;

    @Test
    void testObtenertodas() throws Exception {
        EstadisticaResponseDTO responseDTO = new EstadisticaResponseDTO(1L, 5L, 10L, "Kills", 15, true);

        when(estadisticaService.obtenertodas()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/estadisticas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estadisticaId").value(1L))
                .andExpect(jsonPath("$[0].metrica").value("Kills"));
    }

    @Test
    void testObtenerPorId() throws Exception {
        Long id = 1L;
        EstadisticaResponseDTO responseDTO = new EstadisticaResponseDTO(id, 5L, 10L, "Kills", 15, true);

        when(estadisticaService.obtenerPorId(id)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/estadisticas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadisticaId").value(id))
                .andExpect(jsonPath("$.metrica").value("Kills"));
    }

    @Test
    void testObtenerPorIdNoExiste() throws Exception {
        Long id = 99L;
        when(estadisticaService.obtenerPorId(id))
                .thenThrow(new RuntimeException("No se encontro ninguna estadistica con el ID: " + id));

        mockMvc.perform(get("/api/estadisticas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No se encontro ninguna estadistica con el ID: 99"));
    }

    @Test
    void testRegistrarEstadisticas() throws Exception {
        EstadisticaRequestDTO requestDTO = new EstadisticaRequestDTO(5L, 10L, "Asistencias", 10, true);
        EstadisticaResponseDTO responseDTO = new EstadisticaResponseDTO(100L, 5L, 10L, "Asistencias", 10, true);

        when(estadisticaService.registrarEstadistica(any(EstadisticaRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/estadisticas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))) // <-- Corregido de contentType a content
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estadisticaId").value(100L))
                .andExpect(jsonPath("$.metrica").value("Asistencias"));
    }

    @Test
    void testRegistrarEstadisticasNoValido() throws Exception {
        EstadisticaRequestDTO requestInvalido = new EstadisticaRequestDTO(5L, 10L, "", -5, true);

        mockMvc.perform(post("/api/estadisticas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.valor").value("El valor no puede ser negativo"))
                .andExpect(jsonPath("$.metrica").value("La metrica no puede estar vacia"));
    }

    @Test
    void testActualizarEstadistica() throws Exception {
        Long id = 1L;
        EstadisticaRequestDTO requestDTO = new EstadisticaRequestDTO(5L, 10L, "Muertes", 3, true);
        EstadisticaResponseDTO responseDTO = new EstadisticaResponseDTO(id, 5L, 10L, "Muertes", 3, true);

        when(estadisticaService.actualizar(eq(id), any(EstadisticaRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/estadisticas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metrica").value("Muertes"))
                .andExpect(jsonPath("$.valor").value(3));
    }

    @Test
    void testEliminarEstadistica() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/estadisticas/{id}", id)) // <-- Faltaba pasar el 'id'
                .andExpect(status().isNoContent());

        verify(estadisticaService).eliminar(id);
    }
}