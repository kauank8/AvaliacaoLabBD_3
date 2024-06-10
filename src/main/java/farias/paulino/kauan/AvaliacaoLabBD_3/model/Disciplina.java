package farias.paulino.kauan.AvaliacaoLabBD_3.model;

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
@Table(name = "Disciplina")
public class Disciplina {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private int codigo;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "aulas_semanais", nullable = false)
    private int aulas_semanais;

    @Column(name = "hora_inicio", length = 15, nullable = false)
    private String hora_inicio;

    @Column(name = "hora_fim", length = 15, nullable = false)
    private String hora_fim;

    @Column(name = "dia_semana", length = 25, nullable = false)
    private String dia_semana;

    @Column(name = "semestre", nullable = false)
    private int semestre;
    
    @Column(name = "turno", length = 20, nullable = false)
    private String turno;


    @ManyToOne(targetEntity = Curso.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_curso", nullable = false)
    private Curso curso;

    @ManyToOne(targetEntity = Professor.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula_professor", nullable = false)
    private Professor professor;

}
