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
    private Long jugadorid;

    @Column(name = "partida_id", nullable = false)
    private Long partidaid;

    @Column(name = "parametro_nombre") //kills o goles
    private String parametroNombre;

    @Column(name = "parametro_valor")// 6,7, 11
    private Integer parametroValor;


}
