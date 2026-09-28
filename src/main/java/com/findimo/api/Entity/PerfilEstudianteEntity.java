package com.findimo.api.Entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tm_perfil_estudiante", schema = "findimo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilEstudianteEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDPERFILESTUDIANTE")
	private Long idPerfilEstudiante;

	@Column(name = "NOMBREUNIVERSIDAD", length = 255)
	private String nombreUniversidad;

	@Column(name = "PRESUPUESTOMAXIMO")
	private Float presupuestoMaximo;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
	private UsuarioEntity usuario;
}
