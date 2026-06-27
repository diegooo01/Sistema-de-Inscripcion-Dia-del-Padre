package pe.org.colegiado.inscripciones.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pe.org.colegiado.inscripciones.model.Estado;
import pe.org.colegiado.inscripciones.repository.InscripcionRepository;
import pe.org.colegiado.inscripciones.service.InscripcionService;

@Controller
public class AdminController {

    private final InscripcionRepository inscripcionRepository;
    private final InscripcionService inscripcionService;

    public AdminController(InscripcionRepository inscripcionRepository,
                           InscripcionService inscripcionService) {
        this.inscripcionRepository = inscripcionRepository;
        this.inscripcionService = inscripcionService;
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

    @PostMapping("/dashboard/aprobar/{id}")
    public String aprobar(@PathVariable Long id) {
        inscripcionService.aprobarInscripcion(id);
        return "redirect:/dashboard";
    }
}