package farias.paulino.kauan.AvaliacaoLabBD_3.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import farias.paulino.kauan.AvaliacaoLabBD_3.model.Chamada;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.ChamadaId;

public interface IChamadaRepository extends JpaRepository<Chamada,ChamadaId >{
	@Query(value = """
			Select d.codigo, d.nome, d.aulas_semanais, Cast(d.hora_inicio as varchar(5)), Cast(d.hora_fim as varchar(5)), 
			d.dia_semana, d.semestre, c.nome as nome_curso
			from Disciplina d, Curso c where d.codigo_curso = c.codigo and d.matricula_professor =  :matricula
			""",
			nativeQuery = true)
			List<Object[]> listar_disciplinas_professor(@Param("matricula") int matricula);
			
	@Query(value="Select Distinct data_aula from Chamada where codigo_disciplina = :cod_disciplina",
			nativeQuery = true)
	List<Object[]> listar_chamadas(@Param("cod_disciplina") int cod);
	
	@Query(value="Select * from fn_alunosChamada(:cod_disciplina)",
			nativeQuery = true)
	List<Object[]> fn_alunosChamada(@Param("cod_disciplina") int cod);
	
	@Procedure(name = "sp_insereChamada")
	    String sp_insereChamada(
	            @Param("ra") String ra,
	            @Param("codigo_disciplina") int codigoDisciplina,
	            @Param("primeira_aula") int primeiraAula,
	            @Param("segunda_aula") int segundaAula,
	            @Param("terceira_aula") int terceiraAula,
	            @Param("quarta_aula") int quartaAula
	    );
	
	@Query(value="Select c.presenca_primeira_aula, c.presenca_segunda_aula, c.presenca_terceira_aula, c.presenca_quarta_aula, " +
	           "c.data_aula, c.codigo_disciplina, c.aluno_ra, a.nome, c.semestre " +
	           "from Chamada c, Aluno a " +
	           "where c.codigo_disciplina = :codigoDisciplina and c.data_aula = :dataAula " +
	           "and c.aluno_ra = a.ra", nativeQuery = true)
	    List<Object[]> listar_editar_chamada(
	            @Param("codigoDisciplina") int codigoDisciplina,
	            @Param("dataAula") LocalDate dataAula
	    );
	    
	@Procedure(name = "sp_atualizaChamada")
	    String sp_atualizaChamada(
	            @Param("ra") String ra,
	            @Param("codigo_disciplina") int codigoDisciplina,
	            @Param("primeira_aula") int primeiraAula,
	            @Param("segunda_aula") int segundaAula,
	            @Param("terceira_aula") int terceiraAula,
	            @Param("quarta_aula") int quartaAula,
	            @Param("data_aula") String dataAula
	    );
	
	@Query(value="Select * from fn_lista_chamada(:codDisciplina)", nativeQuery = true)
	List<Object[]> listar_falta_semana(@Param("codDisciplina") int cod_disc);
	
}
