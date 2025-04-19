
package com.diplomado.tienda.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO implements Serializable {
    @Column
    private String username;
    private String email;
    private String password;

}
