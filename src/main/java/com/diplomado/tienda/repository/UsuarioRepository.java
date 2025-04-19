package com.diplomado.tienda.repository;

import com.diplomado.tienda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsuario(String usuario);
    Optional<Usuario> findByEmail(String email);
    Usuario findUsuarioByUsuario(String usuario);
    boolean existsByEmail(String email);
}
