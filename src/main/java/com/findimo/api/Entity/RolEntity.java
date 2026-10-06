package com.findimo.api.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tm_rol", schema = "findimo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolEntity {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDROL")
	private Integer idRol;

	@Column(name = "NOMBREROL")
	private String nombreRol;
}
