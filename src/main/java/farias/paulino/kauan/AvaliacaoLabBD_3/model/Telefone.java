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
@Table(name = "Telefone")
@IdClass(TelefoneId.class)
@NamedStoredProcedureQuery(
		name="Telefone.sp_insereTelefone",
		procedureName = "sp_insereTelefone",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "ra", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "numero", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class),
		}
)
@NamedStoredProcedureQuery(
		name="Telefone.sp_excluiTelefone",
		procedureName = "sp_excluiTelefone",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "ra", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "numero", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class),
		}
)
@NamedStoredProcedureQuery(
		name="Telefone.sp_alterarTelefone",
		procedureName = "sp_alterarTelefone",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "ra", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "numero_novo", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "numero_antigo", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class),
		}
)
public class Telefone {
	@Id
    @Column(name = "numero", length = 11, nullable = false)
    private String numero;

    @Id
    @ManyToOne(targetEntity = Aluno.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_ra", nullable = false)
    private Aluno aluno;
}
