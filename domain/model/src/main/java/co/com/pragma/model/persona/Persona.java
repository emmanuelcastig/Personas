package co.com.pragma.model.persona;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Persona {
    private Long id;
    private String nombre;
    private String correo;
    private int edad;
    private List<Long> bootcamps;
}
