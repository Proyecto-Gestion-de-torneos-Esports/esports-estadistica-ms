package Torneo.Estadistica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Estadistica_Jugador")

public class EstadisticaJugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jugador_id",nullable = false)
    private Long jugadorId;

    @Column(name = "partida_id", nullable = false)
    private Long partidaId;

    @Column(nullable = false, length = 50) //kills o goles
    private String Metrica;

    @Column(nullable = false)// 6,7, 11
    private Integer Valor;


}
