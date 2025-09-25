package co.com.pragma.api;

import co.com.pragma.api.dto.AsignarPersonaABootcampRequest;
import co.com.pragma.api.dto.PersonaRequest;
import co.com.pragma.api.mapper.PersonaMapper;
import co.com.pragma.usecase.persona.PersonaUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import jakarta.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Handler {
    private final PersonaUseCase personaUseCase;
    private final Validator validator;
    private final TransactionalOperator transactionalOperator;
    private final PersonaMapper personaMapper;

    public Mono<ServerResponse> crearPersona(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PersonaRequest.class)
                .flatMap(this::validacion)
                .map(personaMapper::toDomain)
                .as(transactionalOperator::transactional)
                .flatMap(personaUseCase::crearPersona)
                .then(ServerResponse.status(201).build())
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue(e.getMessage())
                );
    }

    public Mono<ServerResponse> asignarBootcamp(ServerRequest request) {
        return request.bodyToMono(AsignarPersonaABootcampRequest.class)
                .flatMap(this::validacion)
                .flatMapMany(req -> Flux.fromIterable(req.getBootcamps())
                        .concatMap(bootcampId -> personaUseCase.asignarPersonaABootcamp(req.getIdPersona(), bootcampId))
                )
                .then(ServerResponse.status(201).build())
                .onErrorResume(IllegalArgumentException.class,
                        e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public <T> Mono<T> validacion(T request) {
        Set<ConstraintViolation<T>> violaciones = validator.validate(request);
        if (!violaciones.isEmpty()) {
            String errorMessage = violaciones.stream()
                    .map(violation -> violation.getPropertyPath() + ": " +
                            violation.getMessage())
                    .collect(Collectors.joining(", "));
            return Mono.error(new ValidationException(errorMessage));
        }
        return Mono.just(request);
    }
}



