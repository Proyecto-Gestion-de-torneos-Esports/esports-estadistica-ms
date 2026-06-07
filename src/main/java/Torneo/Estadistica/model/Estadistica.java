package Torneo.Estadistica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Estadistica_Jugador")

public class Estadistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estadistica_id", nullable = false)
    private Long estadisticaId;

    @Column(name = "usuario_id",nullable = false)
    private Long usuarioId;

    @Column(name = "partidas_torneo_id", nullable = false)
    private Long partidasTorneoId;

    @Column(nullable = false, length = 50) //kills o goles
    private String metrica;

    @Column(nullable = false)
    private Integer valor;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    

}
