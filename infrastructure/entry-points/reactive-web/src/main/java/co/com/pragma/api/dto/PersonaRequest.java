package co.com.pragma.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PersonaRequest {

    @NotBlank(message = "El nombre es obligatorio y no puede estar vacío")
    private String nombre;
    @NotBlank(message = "El correo es obligatorio y no puede estar vacío")
    @Email(message = "El correo debe tener un formato válido")
    private String correo;
    @NotNull(message = "La edad es obligatoria")
    private int edad;

}
