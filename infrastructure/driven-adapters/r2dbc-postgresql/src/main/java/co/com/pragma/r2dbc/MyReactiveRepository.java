package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.PersonaEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MyReactiveRepository extends ReactiveCrudRepository<PersonaEntity, Long>, ReactiveQueryByExampleExecutor<PersonaEntity> {

}
