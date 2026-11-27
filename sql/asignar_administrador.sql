-- En psql, ejecutar con -v correo='correo-de-la-cuenta' -f asignar_administrador.sql.
-- Usar una cuenta existente dedicada a la administración.
\set ON_ERROR_STOP on
UPDATE findimo.tm_usuario AS usuario
SET idrol = rol.idrol
FROM findimo.tm_rol AS rol
WHERE rol.nombrerol = 'ADMINISTRADOR'
  AND usuario.correoelectronico = :'correo'
  AND usuario.estado
RETURNING usuario.idusuario;
