package com.saudesync.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class DadoSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private double batimentosCardiacos;

    @Column(nullable = false)
    private double niveisGlicose;

    @Column(nullable = false)
    private double pressaoArterial;

    @Column(nullable = false)
    private double atividadeFisica;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "cd_usuario")
    private Usuario usuario;
}
