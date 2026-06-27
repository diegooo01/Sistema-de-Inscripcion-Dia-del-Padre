package pe.org.colegiado.inscripciones.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.org.colegiado.inscripciones.model.Estado;
import pe.org.colegiado.inscripciones.repository.InscripcionRepository;

@Controller
public class AdminController {

    private final InscripcionRepository inscripcionRepository;

    public AdminController(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }

    @GetMapping("/dashboard")
    public String mostrarVistaAdmin(Model model) {
        model.addAttribute("total", inscripcionRepository.count());
        model.addAttribute("aprobados", inscripcionRepository.countByEstado(Estado.APROBADO));
        model.addAttribute("rechazados", inscripcionRepository.countByEstado(Estado.RECHAZADO));
        model.addAttribute("pendientes", inscripcionRepository.countByEstado(Estado.PENDIENTE));
        model.addAttribute("inscripcionesPendientes", inscripcionRepository.findByEstado(Estado.PENDIENTE));
        return "dashboard";
    }
}