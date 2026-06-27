package pe.org.colegiado.inscripciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.org.colegiado.inscripciones.model.Estado;
import pe.org.colegiado.inscripciones.model.Inscripcion;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    List<Inscripcion> findByEstado(Estado estado);

    long countByEstado(Estado estado);
}