package pe.org.colegiado.inscripciones.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    private final InscripcionRepository repository;
    private final String uploadDir;

    public InscripcionService(InscripcionRepository repository,
                              @Value("${app.uploads-dir}") String uploadDir) {
        this.repository = repository;
        this.uploadDir = uploadDir;
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
}