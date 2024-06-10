package farias.paulino.kauan.AvaliacaoLabBD_3.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Chamada")
@IdClass(ChamadaId.class)

public class Chamada {
	@Id
	@Column(name = "data_aula", nullable = false)
	private LocalDate data_aula;
	
	@Id
    @ManyToOne(targetEntity = Aluno.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_ra", nullable = false)
    private Aluno aluno;
	
	@Id
	@ManyToOne(targetEntity = Disciplina.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_disciplina", nullable = false)
    private Disciplina codigoDisciplina;

	 @Id
	 @Column(name = "semestre", nullable = false, length = 10)
	 private String semestre;
	
	
	@Column(name = "presenca_primeira_aula", nullable = false)
	private int presenca_primeira_aula;

	@Column(name = "presenca_segunda_aula", nullable = false)
	private int presenca_segunda_aula;

	@Column(name = "presenca_terceira_aula")
	private int presenca_terceira_aula;

	@Column(name = "presenca_quarta_aula")
	private int presenca_quarta_aula;
	
	  @ManyToOne
	    @JoinColumns({
	            @JoinColumn(name = "aluno_ra", referencedColumnName = "aluno_ra", insertable = false, updatable = false),
	            @JoinColumn(name = "codigo_disciplina", referencedColumnName = "codigo_disciplina", insertable = false, updatable = false),
	            @JoinColumn(name = "semestre", referencedColumnName = "ano_semestre", insertable = false, updatable = false)
	    })
	    private Matricula matricula;
}
