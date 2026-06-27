package pe.org.colegiado.inscripciones.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pe.org.colegiado.inscripciones.client.ColegiadosClient;
import pe.org.colegiado.inscripciones.dto.ColegiadoDto;
import pe.org.colegiado.inscripciones.service.InscripcionService;
import pe.org.colegiado.inscripciones.service.ResultadoValidacion;
import pe.org.colegiado.inscripciones.service.ValidadorService;

import java.io.IOException;
import java.util.Optional;

@Controller
public class InscripcionController {

    private final ColegiadosClient colegiadosClient;
    private final ValidadorService validadorService;
    private final InscripcionService inscripcionService;

    public InscripcionController(ColegiadosClient colegiadosClient,
                                 ValidadorService validadorService,
                                 InscripcionService inscripcionService) {
        this.colegiadosClient = colegiadosClient;
        this.validadorService = validadorService;
        this.inscripcionService = inscripcionService;
    }

    @GetMapping("/inscripcion")
    public String mostrarInscripcion() {
        return "inscripcion";
    }

    @PostMapping("/inscripcion")
    public String registrarInscripcion(@RequestParam String dni,
                                       @RequestParam String nombre,
                                       @RequestParam("imagen") MultipartFile imagen,
                                       Model model) throws IOException {

        Optional<ColegiadoDto> colegiadoOpt = colegiadosClient.buscarColegiadoPorDni(dni);
        ResultadoValidacion resultadoValidacion = validadorService.validacion(colegiadoOpt);

        if (!resultadoValidacion.valido()) {
            model.addAttribute("error", resultadoValidacion.motivo());
            return "resultado";
        }

        String nombreImagen = inscripcionService.guardarImagen(imagen);
        inscripcionService.guardarInscripcionPendiente(dni, nombre, nombreImagen);

        model.addAttribute("exito", "Inscripción registrada, queda Pendiente de revisión.");
        return "resultado";
    }
}