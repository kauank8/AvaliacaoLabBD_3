package farias.paulino.kauan.AvaliacaoLabBD_3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Matricula")
@IdClass(MatriculaId.class)
@NamedStoredProcedureQuery(name = "Matricula.sp_insereMatricula", procedureName = "sp_insereMatricula", 
		parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "ra", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigo_disciplina", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "hora_inicio", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "hora_fim", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "dia_semana", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class) 
		}
)
public class Matricula {
	@Id
	@ManyToOne(targetEntity = Aluno.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "aluno_ra", nullable = false)
	private Aluno aluno;

	@Id
	@ManyToOne(targetEntity = Disciplina.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_disciplina")
	private Disciplina disciplina;

	@Id
	@Column(name = "ano_semestre", length = 10, nullable = false)
	private String ano_semestre;

	@Column(name = "dia_semana", length = 25, nullable = false)
	private String diaSemana;

	@Column(name = "nota", length = 5, nullable = false)
	private String nota;

	@Column(name = "situacao", length = 15, nullable = false)
	private String situacao;

	@Column(name = "quantidade_presenca", nullable = false)
	private int quantidade_presenca;

	@Column(name = "quantidade_falta", nullable = false)
	private int quantidade_faltas;

	@Column(name = "frequencia", nullable = false)
	private double frequencia;

}
