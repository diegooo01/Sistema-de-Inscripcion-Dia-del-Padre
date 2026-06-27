package pe.org.colegiado.inscripciones.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InscripcionController {

    @GetMapping("/inscripcion")
    public String mostrarInscripcion() {
        return "inscripcion";
    }
}