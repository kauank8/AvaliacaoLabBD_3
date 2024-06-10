package farias.paulino.kauan.AvaliacaoLabBD_3.model;

import java.time.LocalDate;

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
@Table(name = "Professor")
public class Professor {
	 	@Id
	    @Column(name = "matricula", nullable = false)
	    private int matricula;

	    @Column(name = "nome", nullable = false, length = 150)
	    private String nome;

	    @Column(name = "cpf", nullable = false, length = 11, unique = true)
	    private String cpf;

	    @Column(name = "data_nasc", nullable = false)
	    private LocalDate dataNasc;

	    @Column(name = "formacao_academica", nullable = false, length = 255)
	    private String formacaoAcademica;
}
