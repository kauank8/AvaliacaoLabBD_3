package farias.paulino.kauan.AvaliacaoLabBD_3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Notas")
public class Notas {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;

	@Column(name = "nota", nullable = false)
	private double nota;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "aluno_ra", referencedColumnName = "aluno_ra", insertable = false, updatable = false),
			@JoinColumn(name = "codigo_disciplina", referencedColumnName = "codigo_disciplina", insertable = false, updatable = false),
			@JoinColumn(name = "ano_semestre", referencedColumnName = "ano_semestre", insertable = false, updatable = false) })
	private Matricula matricula;

	@ManyToOne(targetEntity = Avaliacao.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_avaliacao", nullable = false)
	private Avaliacao avaliacao;
}
