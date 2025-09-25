package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.PersonaRequest;
import co.com.pragma.model.persona.Persona;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonaMapper {
    Persona toDomain(PersonaRequest request);
}
