package farias.paulino.kauan.AvaliacaoLabBD_3.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaId implements Serializable {
	private static final long serialVersionUID = 1L;
	private Aluno aluno;
	private Disciplina disciplina;
	private String ano_semestre;

}
