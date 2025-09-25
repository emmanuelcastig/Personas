CREATE TABLE IF NOT EXISTS personas (
                                        id BIGSERIAL PRIMARY KEY,
                                        nombre VARCHAR(50) NOT NULL,
    correo VARCHAR(100) NOT NULL,
    edad INT NOT NULL
    );

CREATE TABLE IF NOT EXISTS bootcamp_persona (
                                                  id BIGSERIAL PRIMARY KEY,
                                                  id_persona BIGINT NOT NULL,
                                                  id_bootcamp BIGINT NOT NULL,
                                                  CONSTRAINT fk_persona FOREIGN KEY (id_persona) REFERENCES personas(id) ON DELETE CASCADE
    );