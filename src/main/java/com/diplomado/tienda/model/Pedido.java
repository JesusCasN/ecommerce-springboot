package com.diplomado.tienda.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedido;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Timestamp fecha;

    @Column(nullable = false)
    private double total;

    // Relación OneToMany
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetallePedido> detalles;

    // Relación ManyToOne
    @ManyToOne
    @JoinColumn(name = "id_forma_pago", nullable = false)
    private FormasDePago formaPago;

    @ManyToOne
    @JoinColumn(name = "id_direccion", nullable = false)
    private Direccion direccionEnvio;

    // Método para convertir Timestamp a LocalDateTime
    public LocalDateTime getFechaAsLocalDateTime() {
        return fecha.toLocalDateTime();
    }


}
