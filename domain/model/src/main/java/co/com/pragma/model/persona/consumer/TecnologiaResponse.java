package co.com.pragma.model.persona.consumer;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TecnologiaResponse {
    private Long id;
    private String nombre;
}
