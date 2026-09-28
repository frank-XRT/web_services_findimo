package com.findimo.api.Entity;

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
@Table(name = "tm_perfil_arrendador", schema = "findimo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilArrendadorEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDPERFILARRENDADOR")
	private Long idPerfilArrendador;

	@Column(name = "CUENTABANCARIA", length = 100)
	private String cuentaBancaria;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
	private UsuarioEntity usuario;
}
