package com.findimo.api.Service;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AlmacenFotos {
    public static final String RUTA_WEB = "/imagenes/viviendas/";
    private static final long MAX_BYTES = 5L * 1024 * 1024;
    private static final long MAX_PIXELES = 20_000_000L;
    private static final Logger LOG = LoggerFactory.getLogger(AlmacenFotos.class);
    private final Path directorio;
    private final Optional<FotosCloudinary> nube;

    public AlmacenFotos(@Value("${findimo.fotos.directorio:uploads/viviendas}") String directorio,
            Optional<FotosCloudinary> nube) {
        this.directorio = Path.of(directorio).toAbsolutePath().normalize();
        this.nube = nube;
    }

    public Path getDirectorio() {
        return directorio;
    }

    public List<String> guardar(List<MultipartFile> fotos) {
        if (fotos.isEmpty()) return List.of();
        if (fotos.size() > 5) throw fotoInvalida();

        var archivos = new ArrayList<Path>();
        var idsNube = new ArrayList<String>();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int estado) {
                if (estado != STATUS_ROLLED_BACK) return;
                nube.ifPresent(almacen -> idsNube.forEach(id -> {
                    try {
                        almacen.quitar(id);
                    } catch (RuntimeException error) {
                        LOG.warn("No se pudo limpiar una foto remota de la publicación fallida");
                    }
                }));
                for (Path archivo : archivos) {
                    try {
                        Files.deleteIfExists(archivo);
                    } catch (IOException error) {
                        LOG.warn("No se pudo limpiar una foto de la publicación fallida", error);
                    }
                }
            }
        });

        var rutas = new ArrayList<String>();
        for (MultipartFile foto : fotos) {
            rutas.add(guardarFoto(foto, archivos, idsNube));
        }
        return rutas;
    }

    private String guardarFoto(MultipartFile foto, List<Path> archivos, List<String> idsNube) {
        if (foto.isEmpty() || foto.getSize() > MAX_BYTES) throw fotoInvalida();

        try (var entrada = foto.getInputStream(); var imagen = ImageIO.createImageInputStream(entrada)) {
            if (imagen == null) throw fotoInvalida();
            var lectores = ImageIO.getImageReaders(imagen);
            if (!lectores.hasNext()) throw fotoInvalida();
            var lector = lectores.next();
            try {
                lector.setInput(imagen, true, true);
                String formato = lector.getFormatName().toLowerCase(Locale.ROOT);
                boolean jpeg = formato.equals("jpeg");
                if ((!jpeg && !formato.equals("png"))
                        || (long) lector.getWidth(0) * lector.getHeight(0) > MAX_PIXELES) {
                    throw fotoInvalida();
                }
                var pixeles = lector.read(0);
                if (nube.isPresent()) {
                    var salida = new ByteArrayOutputStream();
                    if (!ImageIO.write(pixeles, formato, salida)) {
                        throw new IOException("No hay un codificador de imágenes disponible.");
                    }
                    String id = "findimo/viviendas/" + UUID.randomUUID();
                    idsNube.add(id);
                    return nube.get().guardar(id, formato, salida.toByteArray());
                }
                Files.createDirectories(directorio);
                Path archivo = directorio.resolve(UUID.randomUUID() + (jpeg ? ".jpg" : ".png"));
                try (var salida = Files.newOutputStream(archivo, java.nio.file.StandardOpenOption.CREATE_NEW)) {
                    archivos.add(archivo);
                    if (!ImageIO.write(pixeles, formato, salida)) {
                        throw new IOException("No hay un codificador de imágenes disponible.");
                    }
                }
                return RUTA_WEB + archivo.getFileName();
            } finally {
                lector.dispose();
            }
        } catch (javax.imageio.IIOException error) {
            throw fotoInvalida();
        } catch (IOException error) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo guardar una foto.", error);
        }
    }

    private ResponseStatusException fotoInvalida() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Selecciona hasta cinco fotos JPG o PNG de hasta 5 MB y 20 megapíxeles cada una.");
    }
}
