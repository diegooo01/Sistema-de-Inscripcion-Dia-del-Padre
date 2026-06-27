package pe.org.colegiado.inscripciones.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pe.org.colegiado.inscripciones.dto.ColegiadoDto;

import java.util.List;
import java.util.Optional;

@Service
public class ColegiadosClient {

    private final RestClient restClient;

    public ColegiadosClient(@Value("${colegiados.api.base-url}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public Optional<ColegiadoDto> buscarColegiadoPorDni(String dni) {
        List<ColegiadoDto> resultado = restClient.get()
                .uri(uri -> uri.path("/colegiados").queryParam("dni", dni).build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<ColegiadoDto>>() {});
        return resultado.stream().findFirst();
    }
}