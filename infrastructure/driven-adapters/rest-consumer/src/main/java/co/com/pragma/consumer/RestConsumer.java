package co.com.pragma.consumer;

import co.com.pragma.model.persona.consumer.BootcampResponse;
import co.com.pragma.model.persona.consumer.BootcampRestConsumer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements BootcampRestConsumer {
    private final WebClient client;

    @Override
    @CircuitBreaker(name = "obtenerBootcamp" )
    public Flux<BootcampResponse> obtenerBootcamps() {
        return client
                .get()
                .uri("/api/v1/bootcamp")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("Error consumiendo cliente: " + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("Error consumiendo servidor: " + response.statusCode())))
                .bodyToFlux(BootcampResponse.class);
    }
}
