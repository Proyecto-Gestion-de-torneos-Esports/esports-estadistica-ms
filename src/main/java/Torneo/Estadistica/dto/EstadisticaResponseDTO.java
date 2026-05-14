package Torneo.Estadistica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaResponseDTO {
    private Long estadistica_id;
    private Long usuarioId;
    private Long partidaId;
    private String metrica;
    private Integer valor;
    private Boolean activo;
}
