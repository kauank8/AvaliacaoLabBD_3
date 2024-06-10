package farias.paulino.kauan.AvaliacaoLabBD_3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import farias.paulino.kauan.AvaliacaoLabBD_3.model.Avaliacao;

public interface IAvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {
	 @Procedure(name = "sp_insereAvalicao")
	    String sp_insereAvaliacao(
	        @Param("tipo") String tipo, 
	        @Param("peso") double peso, 
	        @Param("data_avaliacao") String dataAvaliacao, 
	        @Param("codigo_disciplina") int codigoDisciplina);
	 
	 @Query(value="Select  * from Avaliacao where codigo_disciplina = :cod_disciplina", nativeQuery=true)
	 		List<Avaliacao> lista_avaliacao_disciplina(@Param("cod_disciplina") int cod);
	 
	 @Query(value = """
	 		Select d.nome AS disciplina_nome, m.codigo_disciplina,
		    STRING_AGG(CONCAT( av.tipo, ' - Data: ', FORMAT(av.data_avaliacao, 'dd/MM/yyyy')), '; ') AS datas,
		    STRING_AGG(CONCAT( av.tipo, ' - Peso: ', av.peso), '; ') AS pesos
			From Matricula m, Disciplina d, Avaliacao av
			Where d.codigo = m.codigo_disciplina
		    and  m.ano_semestre = (SELECT dbo.fn_turma())
			and av.codigo_disciplina = m.codigo_disciplina
		    and m.situacao = 'Em Andamento'and m.aluno_ra = :alunoRa
			group By 
		    d.nome, m.codigo_disciplina;
	 		""",nativeQuery = true)
	 List<Object[]> listar_avaliacao_aluno(@Param("alunoRa") String ra); 
}
