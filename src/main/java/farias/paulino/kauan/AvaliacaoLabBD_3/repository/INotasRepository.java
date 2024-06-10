package farias.paulino.kauan.AvaliacaoLabBD_3.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import farias.paulino.kauan.AvaliacaoLabBD_3.model.Notas;

public interface INotasRepository extends JpaRepository<Notas, Integer> {
	 @Query(value = """
	 		Select m.aluno_ra, a.nome, m.codigo_disciplina, m.ano_semestre
	 		from Aluno a, Matricula m 
	 		where a.ra = m.aluno_ra and m.ano_semestre = (Select dbo.fn_turma()) and m.codigo_disciplina = :codigo_disciplina
	 		and m.situacao = 'Em Andamento'
	 		""", nativeQuery = true)
	 List<Object[]> listar_matriculas(@Param("codigo_disciplina") int codigoDisciplina);
	 
	 @Query(value="""
	 		Select av.codigo, av.tipo  From Avaliacao av, Disciplina d
	 		where av.codigo_disciplina = d.codigo
	 		and d.codigo = :codigo_disciplina
	 		""", nativeQuery = true)
	 List<Object[]> listar_avaliacoes(@Param("codigo_disciplina") int codigoDisciplina);
	 
	 @Procedure(name = "sp_iuNotas")
	    String sp_iuNotas(
	        @Param("acao") String acao, 
	        @Param("aluno_ra") String alunoRa, 
	        @Param("codigo_disciplina") int codigoDisciplina, 
	        @Param("nota") double nota, 
	        @Param("codigo_avaliacao") int codigoAvaliacao, 
	        @Param("ano_semestre") String anoSemestre
	    );
	 
	 @Query(value = "SELECT * FROM Notas WHERE aluno_ra = :aluno_ra AND codigo_disciplina = :codigo_disciplina AND codigo_avaliacao = :codigo_avaliacao AND ano_semestre = :ano_semestre", nativeQuery = true)
	    	Notas buscar_nota(@Param("aluno_ra") String alunoRa,
	                                     @Param("codigo_disciplina") int codigoDisciplina,
	                                     @Param("codigo_avaliacao") int codigoAvaliacao,
	                                     @Param("ano_semestre") String anoSemestre);
	 
	 @Query(value = """
	 		Select n.*, m.nota as media,
			CASE 
	 		     WHEN Cast(m.nota as decimal(5,2)) >= 6.0 THEN 'Aprovado'
	 		     WHEN Cast(m.nota as decimal(5,2)) >= 3.0 THEN 'Exame'
	 		     ELSE 'Reprovado'
	 			 END AS status
	 		from Matricula m, Notas n, Avaliacao av
	 		where m.ano_semestre = (Select dbo.fn_turma()) and m.codigo_disciplina = :codigo_disciplina
	 		and m.situacao = 'Em Andamento' and m.ano_semestre = n.ano_semestre
			and m.aluno_ra = n.aluno_ra and m.codigo_disciplina = n.codigo_disciplina
			and n.codigo_avaliacao = av.codigo
	 		""", nativeQuery=true)
	 		List<Object[]> listar_notas(@Param("codigo_disciplina") int codigoDisciplina);
	 
	 @Query(value = """
	 		SELECT  d.nome AS disciplina_nome, m.codigo_disciplina, m.nota, 
	 			 CASE 
	 		     WHEN Cast(m.nota as decimal(5,2)) >= 6.0 THEN 'Aprovado'
	 		     WHEN Cast(m.nota as decimal(5,2)) >= 3.0 THEN 'Exame'
	 		     ELSE 'Reprovado'
	 			 END AS status,
		    STRING_AGG(CONCAT(av.tipo, ' - Nota: ', n.nota), '; ') AS notas
			From Matricula m, Disciplina d, Notas n, Avaliacao av
			Where d.codigo = m.codigo_disciplina
			and m.aluno_ra = n.aluno_ra AND m.codigo_disciplina = n.codigo_disciplina
			and n.codigo_avaliacao = av.codigo and m.ano_semestre = (SELECT dbo.fn_turma())
		    and m.situacao = 'Em Andamento' and m.aluno_ra = :alunoRa
			Group By 
		    d.nome, m.codigo_disciplina, m.nota;
	 		""", nativeQuery = true)
	 List<Object[]> lista_notas_alunos(@Param("alunoRa") String ra);
}
