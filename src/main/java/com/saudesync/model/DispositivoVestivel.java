package com.saudesync.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class DispositivoVestivel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String tipo;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "cd_usuario")
    private Usuario usuario;
}
