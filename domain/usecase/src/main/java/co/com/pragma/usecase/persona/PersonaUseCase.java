package co.com.pragma.usecase.persona;

import co.com.pragma.model.persona.Persona;
import co.com.pragma.model.persona.consumer.BootcampResponse;
import co.com.pragma.model.persona.consumer.BootcampRestConsumer;
import co.com.pragma.model.persona.consumer.Reporte;
import co.com.pragma.model.persona.gateways.PersonaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class PersonaUseCase {

    private final PersonaRepository personaRepository;
    private final BootcampRestConsumer bootcampRestConsumer;

    public Mono<Void> crearPersona(Persona persona) {
        return personaRepository.crearPersona(persona);
    }

    public Mono<Void> asignarPersonaABootcamp(Long personaId, Long bootcampId) {
        return personaRepository.obtenerBootcampsPorPersonaId(personaId)
                .collectList()
                .flatMap(inscritos -> {
                    // No más de 5 bootcamps
                    if (inscritos.size() >= 5) {
                        return Mono.error(new IllegalArgumentException("La persona ya está inscrita en 5 bootcamps"));
                    }

                    // Traer todos los bootcamps
                    return bootcampRestConsumer.obtenerBootcamps()
                            .collectList()
                            .flatMap(bootcamps -> {
                                var nuevoBootcamp = bootcamps.stream()
                                        .filter(b -> b.getId().equals(bootcampId))
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalArgumentException("Bootcamp no encontrado"));

                                // Validar solapamiento
                                boolean conflicto = bootcamps.stream()
                                        .filter(b -> inscritos.contains(b.getId()))
                                        .anyMatch(b -> b.getFechaLanzamiento()
                                                .equals(nuevoBootcamp.getFechaLanzamiento())
                                                && b.getDuracion() == nuevoBootcamp.getDuracion());

                                if (conflicto) {
                                    return Mono.error(new IllegalArgumentException(
                                            "Conflicto: el bootcamp se cruza en fecha y duración con otro ya inscrito"));
                                }

                                // Guardar inscripción y luego contar personas
                                return personaRepository.asignarPersonaABootcamp(personaId, bootcampId)
                                        .flatMap(v -> personaRepository.obtenerPersonasPorBootcampId(bootcampId).collectList())
                                        .flatMap(personas -> enviarReporte(personaId, nuevoBootcamp, personas.size()));
                            });
                });
    }

    private Mono<Void> enviarReporte(Long personaId, BootcampResponse bootcamp, int cantidadPersonasInscritas) {
        int cantidadTecnologias = calcularCantidadTecnologias(bootcamp);
        Reporte reporte = Reporte.builder()
                .idPersonas(List.of(personaId))
                .bootcamp(bootcamp)
                .cantidadCapacidades(bootcamp.getCapacidades() != null ? bootcamp.getCapacidades().size() : 0)
                .cantidadTecnologias(cantidadTecnologias)
                .cantidadPersonasInscritas(cantidadPersonasInscritas)
                .build();

        return bootcampRestConsumer.enviarReporte(reporte);
    }

    private int calcularCantidadTecnologias(BootcampResponse bootcamp) {
        if (bootcamp.getCapacidades() == null) return 0;

        return bootcamp.getCapacidades().stream()
                .mapToInt(c -> c.getTecnologias() != null ? c.getTecnologias().size() : 0)
                .sum();
    }
}