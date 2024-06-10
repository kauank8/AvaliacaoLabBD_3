package farias.paulino.kauan.AvaliacaoLabBD_3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import farias.paulino.kauan.AvaliacaoLabBD_3.model.Telefone;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.TelefoneId;

public interface ITelefoneRepository extends JpaRepository<Telefone, TelefoneId> {
	@Procedure(name = "Telefone.sp_insereTelefone")
    String insereTelefone(
            @Param("ra") String ra,
            @Param("numero") String numero
    );

    @Procedure(name = "Telefone.sp_excluiTelefone")
    String excluiTelefone(
            @Param("ra") String ra,
            @Param("numero") String numero
    );

    @Procedure(name = "Telefone.sp_alterarTelefone")
    String alterarTelefone(
            @Param("ra") String ra,
            @Param("numero_novo") String novoNumero,
            @Param("numero_antigo") String numeroAntigo
    );
    
    @Query("SELECT t FROM Telefone t WHERE t.aluno.ra = :ra")
    List<Telefone> findByAlunoRa(@Param("ra") String ra);
}
