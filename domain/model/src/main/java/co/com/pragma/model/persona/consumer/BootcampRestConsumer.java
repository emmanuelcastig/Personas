package co.com.pragma.model.persona.consumer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcampRestConsumer {
    Flux<BootcampResponse> obtenerBootcamps();
    Mono<Void> enviarReporte(Reporte reporte);
}
