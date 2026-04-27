package Torneo.Estadistica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaResponseDTO {
    private Long id;
    private Long jugadorId;
    private Long partidaId;
    private String metrica;
    private Integer Valor;

}
