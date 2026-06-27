package pe.org.colegiado.inscripciones.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 8)
    private String dni;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "imagen_dni_menor")
    private String imagenDniMenor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado;

    @Column(length = 500)
    private String observacion;

    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    public Inscripcion() {
    }

    //Getters
    public Long getId() { return id; }
    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getImagenDniMenor() { return imagenDniMenor; }
    public Estado getEstado() { return estado; }
    public String getObservacion() { return observacion; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }

    //Setters
    public void setDni(String dni) { this.dni = dni; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setImagenDniMenor(String imagenDniMenor) { this.imagenDniMenor = imagenDniMenor; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}