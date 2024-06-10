package farias.paulino.kauan.AvaliacaoLabBD_3.model;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChamadaId implements Serializable {
	private static final long serialVersionUID = 1L;
    private LocalDate data_aula;
    private Aluno aluno;
    private int codigoDisciplina;
    private String semestre;
	
}
