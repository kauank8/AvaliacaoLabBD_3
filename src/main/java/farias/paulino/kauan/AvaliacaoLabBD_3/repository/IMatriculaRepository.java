package farias.paulino.kauan.AvaliacaoLabBD_3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import farias.paulino.kauan.AvaliacaoLabBD_3.model.Matricula;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.MatriculaId;

public interface IMatriculaRepository extends JpaRepository<Matricula, MatriculaId> {
	  @Procedure(name = "Matricula.sp_insereMatricula")
	    String insereMatricula(
	        @Param("ra") String ra,
	        @Param("codigo_disciplina") int codigo_disciplina,
	        @Param("hora_inicio") String hora_inicio,
	        @Param("hora_fim") String hora_fim,
	        @Param("dia_semana") String dia_semana
	    );
	  
	  @Query(value = "SELECT d.nome, LOWER(m.dia_semana) AS dia_semana, CAST(d.hora_inicio AS varchar(5)) AS hora_inicio, CAST(d.hora_fim AS varchar(5)) AS hora_fim, m.frequencia, m.nota, m.situacao, d.codigo " +
              "FROM Matricula m, Disciplina d " +
              "WHERE m.aluno_ra = :ra " +
              "AND d.codigo = m.codigo_disciplina " +
              "ORDER BY " +
              "CASE m.dia_semana " +
              "    WHEN 'Segunda-feira' THEN 1 " +
              "    WHEN 'Terça-feira' THEN 2 " +
              "    WHEN 'Quarta-feira' THEN 3 " +
              "    WHEN 'Quinta-feira' THEN 4 " +
              "    WHEN 'Sexta-feira' THEN 5 " +
              "    WHEN 'Sábado' THEN 6 " +
              "    WHEN 'Domingo' THEN 7 " +
              "    ELSE 8 " +
              "END",
              nativeQuery = true)
	  List<Object[]> listarMatriculasPorRa(@Param("ra") String ra);
	  
	  @Query(value="Select * from fn_historico(:aluno_ra)", nativeQuery = true)
	  List<Object[]> buscar_historico(@Param("aluno_ra") String ra);
	  
	  @Query(value="Select dbo.fn_turma()", nativeQuery = true)
	  Integer buscar_turma();
}
