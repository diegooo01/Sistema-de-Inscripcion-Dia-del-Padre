package pe.org.colegiado.inscripciones.service;

import org.springframework.stereotype.Service;
import pe.org.colegiado.inscripciones.dto.ColegiadoDto;

import java.util.Optional;

@Service
public class ValidadorService {

    public ResultadoValidacion validacion(Optional<ColegiadoDto> colegiadoOpt) {
        if (colegiadoOpt.isEmpty()) {
            return ResultadoValidacion.fallido("El colegiado no esta registrado.");
        }

        ColegiadoDto colegiado = colegiadoOpt.get();

        if (!colegiado.habilitado()) {
            return ResultadoValidacion.fallido("El colegiado no esta habilitado.");
        }

        if (!colegiado.consejoDepartamental().equalsIgnoreCase("Lima")) {
            return ResultadoValidacion.fallido("El colegiado no pertenece al consejo departamental de Lima.");
        }

        if (colegiado.esAdministrativo()) {
            return ResultadoValidacion.fallido("El colegiado es administrativo y tiene prohibido inscribirse.");
        }

        return ResultadoValidacion.exitoso();
    }
}
