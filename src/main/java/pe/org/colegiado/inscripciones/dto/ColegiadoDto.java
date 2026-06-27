package pe.org.colegiado.inscripciones.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ColegiadoDto(
        String dni,
        String nombre,
        Boolean habilitado,
        @JsonProperty("es_administrativo") Boolean esAdministrativo,
        @JsonProperty("consejo_departamental") String consejoDepartamental
) {
}