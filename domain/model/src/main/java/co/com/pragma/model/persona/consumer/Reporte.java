package co.com.pragma.model.persona.consumer;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Reporte {
    private String nombre;
    private String correo;
    private BootcampResponse bootcamp;
    private int cantidadCapacidades;
    private int cantidadTecnologias;
    private int cantidadPersonasInscritas;
}
