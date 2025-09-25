package co.com.pragma.model.persona.consumer;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CapacidadBootcampResponse {
    private Long id;
    private String nombre;
    private List<TecnologiaResponse> tecnologias;
}
