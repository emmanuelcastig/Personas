package co.com.pragma.model.persona.consumer;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Reporte {
    private List<Long> idPersonas;
    private BootcampResponse bootcamp;
    private int cantidadCapacidades;
    private int cantidadTecnologias;
    private int cantidadPersonasInscritas;
}
