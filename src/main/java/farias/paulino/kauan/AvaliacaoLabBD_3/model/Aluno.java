package farias.paulino.kauan.AvaliacaoLabBD_3.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQuery;
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
@Table(name = "Aluno")
    @NamedStoredProcedureQuery(
        name = "Aluno.sp_aluno",
        procedureName = "sp_aluno",
        parameters = {
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "op", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "cod_curso", type = Integer.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "ra", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "cpf", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "nome", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "nome_social", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "dt_nasc", type = java.sql.Date.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "dt_conclusao", type = java.sql.Date.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "email_pessoal", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "email_corporativo", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "instituicao", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "pontuacao", type = double.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "posicao", type = Integer.class),
            @StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class)
        }
    )
@NamedNativeQuery(
		name = "Aluno.sp_geraRa",
		query = "DECLARE @saida VARCHAR(100);\r\n"
				+ "EXEC sp_geraRa @saida OUTPUT;\r\n"
				+ "SELECT @saida AS RA;",
		resultClass = String.class
	)

public class Aluno {
	@Id
	@Column(name = "ra", length = 9, nullable = false)
	private String ra;

	@Column(name = "cpf", length = 11, nullable = false, unique = true)
	private String cpf;

	@Column(name = "nome", length = 150, nullable = false)
	private String nome;

	@Column(name = "nome_social", length = 100, nullable = true)
	private String nome_social;

	@Column(name = "data_nascimento", nullable = false)
	private LocalDate data_nasc;

	@Column(name = "email_pessoal", length = 170, nullable = false)
	private String email_pessoal;

	@Column(name = "email_corporativo", length = 170, nullable = false, unique = true)
	private String email_corporativo;

	@Column(name = "conclusao_segundoGrau", nullable = false)
	private LocalDate conclusao_segundo_grau;

	@Column(name = "instituicao_segundoGrau", length = 170, nullable = false)
	private String instituicao_segundo_grau;

	@Column(name = "pontuacao_vestibular", nullable = false)
	private double pontuacao_vestibular;

	@Column(name = "posicao_vestibular", nullable = false)
	private int posicao_vestibular;

	@Column(name = "ano_ingresso", nullable = false)
	private int ano_ingresso;

	@Column(name = "semestre_ingresso", nullable = false)
	private int semestre_ingresso;

	@Column(name = "semestre_limite", nullable = false)
	private int semestre_limite;

	@Column(name = "ano_limite", nullable = false)
	private int ano_limite;

	@ManyToOne(targetEntity = Curso.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "curso_codigo", nullable = false)
	private Curso curso;

}
