package com.saudesync.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    @NotEmpty(message = "Nome é obrigatório.")
    private String nome;

    @Column(nullable = false)
    @NotEmpty(message = "CPF é obrigatório.")
    private String cpf;

    @Column(nullable = false)
    @NotEmpty(message = "Email é obrigatório.")
    @Email(message = "O Email precisa ser válido")
    private String email;

    @Column(nullable = false)
    @NotEmpty(message = "Senha é obrigatório.")
    @ToString.Exclude
    private String senha;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<Imagem> imagemList;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<Consulta> consultaList;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<DadoSaude> dadoSaudeList;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<DispositivoVestivel> dispositivoVestivelList;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<Alerta> alertaList;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<AnalisePreditiva> analisePreditivaList;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<DispositivoSaude> dispositivoSaudeList;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<PlanoPreventivo> planoPreventivoList;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<AvaliacaoSaude> avaliacaoSaudeList;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<RegistroAtividadeFisica> registroAtividadeFisicaList;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<RecomendacaoNutricional> recomendacaoNutricionalList;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @ToString.Exclude
    private List<RegistroMedicacao> registroMedicacaoList;

    // Métodos UserDetails
}
