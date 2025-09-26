package co.com.pragma.r2dbc;

import co.com.pragma.model.persona.Persona;
import co.com.pragma.model.persona.gateways.PersonaRepository;
import co.com.pragma.r2dbc.entity.PersonaBootcampEntity;
import co.com.pragma.r2dbc.entity.PersonaEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Persona,
        PersonaEntity,
        Long,
        MyReactiveRepository
        > implements PersonaRepository {
    private final PersonaBootcampReactiveRepository bootcampReactiveRepository;

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper, PersonaBootcampReactiveRepository bootcampReactiveRepository) {

        super(repository, mapper, d -> mapper.map(d, Persona.class));
        this.bootcampReactiveRepository = bootcampReactiveRepository;
    }

    @Override
    public Mono<Void> crearPersona(Persona persona) {
        return repository.save(toData(persona)).then();
    }

    @Override
    public Mono<Persona> asignarPersonaABootcamp(Long personaId, Long bootcampId) {
        PersonaBootcampEntity entity = PersonaBootcampEntity.builder()
                .idPersona(personaId)
                .idBootcamp(bootcampId)
                .build();

        return bootcampReactiveRepository.save(entity)
                .flatMap(saved -> repository.findById(personaId))
                .map(this::toEntity);
    }

    @Override
    public Flux<Long> obtenerBootcampsPorPersonaId(Long personaId) {
        return bootcampReactiveRepository.findIdBootcampByIdPersona(personaId)
                .doOnNext(id -> log.info("Bootcamp encontrado: {}", id));
    }

    @Override
    public Flux<Long> obtenerPersonasPorBootcampId(Long bootcampId) {
        return bootcampReactiveRepository.findIdPersonaByIdBootcamp(bootcampId);
    }

}
