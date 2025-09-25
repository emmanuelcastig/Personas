package co.com.pragma.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AsignarPersonaABootcampRequest {
    @NotNull(message = "El id de la persona es obligatorio")
    private Long idPersona;
    @Size(min = 1, max = 5, message = "Una persona puede estar inscrita en minimo 1 y maximo 5 bootcamps")
    private List<Long> bootcamps;
}
