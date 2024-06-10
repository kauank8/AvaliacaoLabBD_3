package farias.paulino.kauan.AvaliacaoLabBD_3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Curso")
public class Curso {
	@Id
	@Column(name="codigo", nullable = false)
	private int codigo;
	
	@Column(name="nome",length = 170, nullable = false)
	private String nome;
	
	@Column(name="carga_horaria", nullable = false)
	private int carga_horaria;
	
	@Column(name="sigla", length = 10, nullable = false)
	private String sigla;
	
	@Column(name="nota_enade", nullable = false)
	private double nota_enade;
}
