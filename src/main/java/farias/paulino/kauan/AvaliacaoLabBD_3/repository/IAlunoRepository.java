package farias.paulino.kauan.AvaliacaoLabBD_3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import farias.paulino.kauan.AvaliacaoLabBD_3.model.Aluno;


public interface IAlunoRepository  extends JpaRepository<Aluno, String>   {
	@Procedure(name = "Aluno.sp_aluno")
	String sp_aluno(
			@Param("op") String op, 
	        @Param("cod_curso") int codCurso,
	        @Param("ra") String ra, 
	        @Param("cpf") String cpf,
	        @Param("nome") String nome, 
	        @Param("nome_social") String nomeSocial, 
	        @Param("dt_nasc") java.sql.Date dtNasc,
	        @Param("dt_conclusao") java.sql.Date dtConclusao,
	        @Param("email_pessoal") String emailPessoal, 
	        @Param("email_corporativo") String emailCorporativo, 
	        @Param("instituicao") String instituicao, 
	        @Param("pontuacao") double pontuacao, 
	        @Param("posicao") int posicao
	        );
	
	@Query(name = "Aluno.sp_geraRa", nativeQuery = true)
    String sp_geraRa();
}
