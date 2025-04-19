package com.diplomado.tienda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO implements Serializable {
    private Long idUsuario;
    private String usuario;
    private String email;
    private Long idRol;

}
