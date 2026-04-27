package Torneo.Estadistica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.Mac;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaRequestDTO {

 @NotNull(message = "El ID  del jugador  es obligatorio ")
    private Long jugadorId;

 @NotNull(message = "El ID de la partida es obligatorio")
    private Long partidaId;

 @NotBlank(message = "La metrica no puede estar vacia")
    private String Metrica;

 @NotNull(message = "El valor es obligatorio")
 @PositiveOrZero(message = "El valor no puede ser negativo")
    private Integer Valor;
}
