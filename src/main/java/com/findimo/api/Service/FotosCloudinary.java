package com.findimo.api.Service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@Component
@Profile("cloud")
public class FotosCloudinary {
    private final RestClient cliente;

    public FotosCloudinary(@Value("${CLOUDINARY_CLOUD_NAME:}") String nube,
            @Value("${CLOUDINARY_API_KEY:}") String clave,
            @Value("${CLOUDINARY_API_SECRET:}") String secreto) {
        if (nube.isBlank() || clave.isBlank() || secreto.isBlank()) {
            cliente = null;
            return;
        }
        if (!nube.matches("[a-zA-Z0-9_-]+")) {
            throw new IllegalArgumentException("El nombre de la nube de Cloudinary no es válido.");
        }
        var solicitudes = new SimpleClientHttpRequestFactory();
        solicitudes.setConnectTimeout(Duration.ofSeconds(8));
        solicitudes.setReadTimeout(Duration.ofSeconds(30));
        cliente = RestClient.builder().requestFactory(solicitudes)
                .baseUrl("https://api.cloudinary.com/v1_1/" + nube + "/image")
                .defaultHeaders(headers -> headers.setBasicAuth(clave, secreto, StandardCharsets.UTF_8))
                .build();
    }

    public String guardar(String id, String formato, byte[] foto) {
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "La subida de fotos todavía no está configurada.");
        }
        var datos = new LinkedMultiValueMap<String, String>();
        datos.add("public_id", id);
        datos.add("overwrite", "false");
        datos.add("file", "data:image/" + formato + ";base64," + Base64.getEncoder().encodeToString(foto));
        try {
            var respuesta = cliente.post().uri("/upload").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(datos).retrieve().body(new ParameterizedTypeReference<Map<String, Object>>() {});
            if (respuesta != null && respuesta.get("secure_url") instanceof String url
                    && "https".equals(URI.create(url).getScheme())) {
                return url;
            }
        } catch (RestClientException | IllegalArgumentException error) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No se pudo subir la foto.");
        }
        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No se recibió la URL de la foto.");
    }

    public void quitar(String id) {
        if (cliente == null) return;
        var datos = new LinkedMultiValueMap<String, String>();
        datos.add("public_id", id);
        cliente.post().uri("/destroy").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(datos).retrieve().toBodilessEntity();
    }
}
