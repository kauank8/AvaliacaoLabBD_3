package farias.paulino.kauan.AvaliacaoLabBD_3.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@IdClass(DispensaId.class)
@Table(name = "Dispensa")
public class Dispensa {
	@Id
	@ManyToOne( targetEntity = Aluno.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "aluno_ra", nullable = false)
	private Aluno aluno;
	
	@Id
	@ManyToOne( targetEntity = Disciplina.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_disciplina", nullable = false)
	private Disciplina disciplina;
	
	@Column(name = "motivo", nullable = false, length = 255)
	private String motivo;
	
	@Column(name = "data_solicitacao", nullable = false)
	private LocalDate data_solicitacao;
	
	@Column(name = "status_dispensa", nullable = false, length = 20)
	private String status;
	
}
