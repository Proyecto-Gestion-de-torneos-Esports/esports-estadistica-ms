package Torneo.Estadistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "transeferencia de datos para registrar o actualizar una estadistica")
public class EstadisticaRequestDTO {

 @NotNull(message = "El ID  del jugador  es obligatorio ")
 @Schema(description = "ID unico del jugador", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long usuarioId;

 @NotNull(message = "El ID de la partida es obligatorio")
 @Schema(description = "ID de la partida del torneo", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long partidasTorneoId;

 @NotBlank(message = "La metrica no puede estar vacia")
 @Schema(description = "Tipo de estadística registrada", example = "Kills", requiredMode = Schema.RequiredMode.REQUIRED)
    private String metrica;

 @NotNull(message = "El valor es obligatorio")
 @PositiveOrZero(message = "El valor no puede ser negativo")
 @Schema(description = "Valor numérico de la métrica", example = "15", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer valor;

 @NotNull(message = "El campo activo es obligatorio")
 @Schema(description = "Indica si la estadística está activa en el sistema", example = "true")
    private Boolean activo;
}
