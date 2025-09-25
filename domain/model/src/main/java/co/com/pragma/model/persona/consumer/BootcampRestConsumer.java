package co.com.pragma.model.persona.consumer;

import reactor.core.publisher.Flux;

public interface BootcampRestConsumer {
    Flux<BootcampResponse> obtenerBootcamps();
}
