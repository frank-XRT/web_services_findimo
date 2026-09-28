package com.findimo.api.Entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tm_propiedad", schema = "findimo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropiedadEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDPROPIEDAD")
	private Long idPropiedad;

	@Column(name = "TITULO")
	private String titulo;

	@Column(name = "DESCRIPCION", columnDefinition = "TEXT")
	private String descripcion;

	@Column(name = "PRECIO")
	private BigDecimal precio;

	@Column(name = "HABITACIONES")
	private Integer habitaciones;

	@Column(name = "DIRECCION")
	private String direccion;

	@Column(name = "DISTRITO")
	private String distrito;

	@Column(name = "ESTADO")
	private String estado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPerfilArrendador", referencedColumnName = "idPerfilArrendador")
	private PerfilArrendadorEntity perfilArrendador;
}
