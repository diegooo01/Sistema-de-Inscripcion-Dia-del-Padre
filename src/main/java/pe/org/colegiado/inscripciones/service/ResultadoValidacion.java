package pe.org.colegiado.inscripciones.service;

public record ResultadoValidacion(boolean valido, String motivo) {

    public static ResultadoValidacion exitoso() {
        return new ResultadoValidacion(true, null);
    }

    public static ResultadoValidacion fallido(String motivo) {
        return new ResultadoValidacion(false, motivo);
    }
}