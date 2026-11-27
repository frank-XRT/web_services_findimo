package com.findimo.api.Repository;

import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.findimo.api.Entity.UsuarioEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<UsuarioEntity> findByIdUsuario(Long idUsuario);

	@Modifying
	@Query("update UsuarioEntity u set u.contrasena = :clave where u.idUsuario = :id and u.estado = true")
	int actualizarContrasenaActiva(Long id, String clave);

	@Modifying
	@Query("""
			update UsuarioEntity u set u.contrasena = :nueva
			where u.idUsuario = :id and u.estado = true and u.contrasena = :anterior
			""")
	int migrarContrasenaAnterior(Long id, String anterior, String nueva);

	Optional<UsuarioEntity> findByCorreoElectronico(String correoElectronico);
}
