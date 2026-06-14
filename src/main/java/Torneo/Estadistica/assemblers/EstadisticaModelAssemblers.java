package Torneo.Estadistica.assemblers;

import Torneo.Estadistica.controller.EstadisticaController;
import Torneo.Estadistica.dto.EstadisticaResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EstadisticaModelAssemblers implements RepresentationModelAssembler<EstadisticaResponseDTO, EstadisticaResponseDTO> {
    @Override
    public EstadisticaResponseDTO toModel(EstadisticaResponseDTO estadistica) {
        estadistica.add(linkTo(methodOn(EstadisticaController.class).obtenerPorId(estadistica.getEstadisticaId())).withSelfRel());
        estadistica.add(linkTo(methodOn(EstadisticaController.class).obtenerTodas()).withRel("todas-las-estadisticas"));

        return estadistica;
    }
}
