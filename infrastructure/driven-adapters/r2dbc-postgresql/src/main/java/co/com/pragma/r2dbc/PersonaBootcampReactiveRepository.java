package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.PersonaBootcampEntity;
import co.com.pragma.r2dbc.entity.PersonaEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PersonaBootcampReactiveRepository extends ReactiveCrudRepository<PersonaBootcampEntity, Long>, ReactiveQueryByExampleExecutor<PersonaBootcampEntity> {
    @Query("SELECT id_bootcamp FROM bootcamp_persona WHERE id_persona = :personaId")
    Flux<Long> findIdBootcampByIdPersona(Long personaId);
}
