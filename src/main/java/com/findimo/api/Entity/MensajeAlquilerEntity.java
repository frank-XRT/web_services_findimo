package com.findimo.api.Entity;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tt_mensaje_alquiler", schema = "findimo")
public class MensajeAlquilerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmensaje")
    private Long idMensaje;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idsolicitudalquiler", nullable = false)
    private SolicitudAlquilerEntity solicitud;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idremitente", nullable = false)
    private UsuarioEntity remitente;

    @Column(name = "texto", nullable = false, length = 1000)
    private String texto;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    protected MensajeAlquilerEntity() {}

    public MensajeAlquilerEntity(SolicitudAlquilerEntity solicitud, UsuarioEntity remitente, String texto) {
        this.solicitud = solicitud;
        this.remitente = remitente;
        this.texto = texto;
        this.fecha = Instant.now();
    }

    public Long getIdMensaje() { return idMensaje; }
    public UsuarioEntity getRemitente() { return remitente; }
    public String getTexto() { return texto; }
    public Instant getFecha() { return fecha; }
}
