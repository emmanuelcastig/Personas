package co.com.pragma.model.persona.consumer;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BootcampResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaLanzamiento;
    private int duracion;
    private List<CapacidadBootcampResponse> capacidades;
}
