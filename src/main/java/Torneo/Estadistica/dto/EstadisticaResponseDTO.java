package Torneo.Estadistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto que representa la respuesta con los datos de una estadística registrada")
public class EstadisticaResponseDTO extends RepresentationModel<EstadisticaResponseDTO> {
    @Schema(description = "ID unico de la estadística generada por la base de datos", example = "1")
    private Long estadisticaId;
    @Schema(description = "ID unico del jugador", example = "5")
    private Long usuarioId;
    @Schema(description = "ID de la partida del torneo", example = "10")
    private Long partidasTorneoId;
    @Schema(description = "Tipo de estadística registrada", example = "Kills")
    private String metrica;
    @Schema(description = "Valor numérico de la métrica", example = "15")
    private Integer valor;
    @Schema(description = "La estadística está activa en el sistema", example = "true")
    private Boolean activo;
}
