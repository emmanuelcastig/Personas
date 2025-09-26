package co.com.pragma.model.persona.gateways;

import co.com.pragma.model.persona.Persona;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonaRepository {
    Mono<Void> crearPersona(Persona persona);
    Mono<Long> asignarPersonaABootcamp(Long personaId, Long bootcampId);
    Flux<Long> obtenerBootcampsPorPersonaId(Long personaId);
    Flux<Long> obtenerPersonasPorBootcampId(Long bootcampId);
}
