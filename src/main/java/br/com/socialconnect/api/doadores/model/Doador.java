package br.com.socialconnect.api.doadores.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Doador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doador")
    private Long idDoador;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name="tipo_doador", nullable = false, length = 20)
    private TipoDoador tipo;

    @Column(nullable = false, length = 80)
    private String email;

    @Column(nullable = false, length = 15)
    private String telefone;
}
