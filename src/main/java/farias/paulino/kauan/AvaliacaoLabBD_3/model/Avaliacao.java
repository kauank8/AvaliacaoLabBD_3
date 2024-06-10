package farias.paulino.kauan.AvaliacaoLabBD_3.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Avaliacao")
public class Avaliacao {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private int codigo;

    @Column(name = "ano_semestre", length = 10, nullable = false)
    private String semestre;
    
    @Column(name = "tipo", length = 30, nullable = false)
    private String tipo;
    
    @Column(name = "peso", nullable = false)
    private double peso;
    
    @Column(name = "data_avaliacao", nullable = false)
    private LocalDate data_avaliacao;
    
    @ManyToOne(targetEntity = Disciplina.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_disciplina", nullable = false)
    private Disciplina disciplina;

}
