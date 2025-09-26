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

    public Mono<Reporte> asignarPersonaABootcamp(Long personaId, Long bootcampId) {
        return personaRepository.obtenerBootcampsPorPersonaId(personaId)
                .collectList()
                .flatMap(inscritos -> {
                    if (inscritos.size() >= 5) {
                        return Mono.error(new IllegalArgumentException("La persona ya está inscrita en 5 bootcamps"));
                    }

                    return bootcampRestConsumer.obtenerBootcamps()
                            .collectList()
                            .flatMap(bootcamps -> {
                                var nuevoBootcamp = bootcamps.stream()
                                        .filter(b -> b.getId().equals(bootcampId))
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalArgumentException("Bootcamp no encontrado"));

                                boolean conflicto = bootcamps.stream()
                                        .filter(b -> inscritos.contains(b.getId()))
                                        .anyMatch(b -> b.getFechaLanzamiento()
                                                .equals(nuevoBootcamp.getFechaLanzamiento())
                                                && b.getDuracion() == nuevoBootcamp.getDuracion());

                                if (conflicto) {
                                    return Mono.error(new IllegalArgumentException(
                                            "Conflicto: el bootcamp se cruza en fecha y duración con otro ya inscrito"));
                                }

                                return personaRepository.asignarPersonaABootcamp(personaId, bootcampId)
                                        .flatMap(persona ->
                                                personaRepository.obtenerPersonasPorBootcampId(bootcampId).collectList()
                                                        .map(personas -> construirReporte(persona, nuevoBootcamp, personas.size()))
                                        );
                            });
                });
    }

    private Reporte construirReporte(Persona persona, BootcampResponse bootcamp, int cantidadPersonasInscritas) {
        int cantidadTecnologias = calcularCantidadTecnologias(bootcamp);
        return Reporte.builder()
                .nombre(persona.getNombre())
                .correo(persona.getCorreo())
                .bootcamp(bootcamp)
                .cantidadCapacidades(bootcamp.getCapacidades() != null ? bootcamp.getCapacidades().size() : 0)
                .cantidadTecnologias(cantidadTecnologias)
                .cantidadPersonasInscritas(cantidadPersonasInscritas)
                .build();
    }
    private int calcularCantidadTecnologias(BootcampResponse bootcamp) {
        if (bootcamp.getCapacidades() == null) return 0;

        return bootcamp.getCapacidades().stream()
                .mapToInt(c -> c.getTecnologias() != null ? c.getTecnologias().size() : 0)
                .sum();
    }
}