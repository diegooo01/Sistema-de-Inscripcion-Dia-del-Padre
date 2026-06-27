package pe.org.colegiado.inscripciones.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.org.colegiado.inscripciones.model.Estado;
import pe.org.colegiado.inscripciones.model.Inscripcion;
import pe.org.colegiado.inscripciones.repository.InscripcionRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class InscripcionService {

    private static final Logger log = LoggerFactory.getLogger(InscripcionService.class);

    private final InscripcionRepository repository;
    private final String uploadDir;
    private final int aforo;

    public InscripcionService(InscripcionRepository repository,
                              @Value("${app.uploads-dir}") String uploadDir,
                              @Value("${app.aforo}") int aforo) {
        this.repository = repository;
        this.uploadDir = uploadDir;
        this.aforo = aforo;
    }

    public String guardarImagen(MultipartFile imagen) throws IOException {
        String nombreImagen = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
        Files.createDirectories(Paths.get(uploadDir));
        Path destino = Paths.get(uploadDir, nombreImagen);
        Files.copy(imagen.getInputStream(), destino);
        return nombreImagen;
    }

    public Inscripcion guardarInscripcionPendiente(String dni, String nombre, String nombreImagen) {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setDni(dni);
        inscripcion.setNombre(nombre);
        inscripcion.setImagenDniMenor(nombreImagen);
        inscripcion.setEstado(Estado.PENDIENTE);
        return repository.save(inscripcion);
    }

    @Transactional
    public synchronized void aprobarInscripcion(Long id) {
        long aprobadas = repository.countByEstado(Estado.APROBADO);
        if (aprobadas >= aforo) {
            throw new IllegalStateException("No se puede aprobar la inscripción, se ha alcanzado el aforo máximo");
        }

        Inscripcion inscripcion = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada: " + id));

        inscripcion.setEstado(Estado.APROBADO);
        repository.save(inscripcion);

        log.info("Inscripción aprobada con id {} (DNI {})", inscripcion.getId(), inscripcion.getDni());
    }
}