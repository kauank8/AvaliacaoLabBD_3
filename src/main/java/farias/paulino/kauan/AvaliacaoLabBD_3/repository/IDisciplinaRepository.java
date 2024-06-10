package farias.paulino.kauan.AvaliacaoLabBD_3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import farias.paulino.kauan.AvaliacaoLabBD_3.model.Disciplina;

public interface IDisciplinaRepository  extends JpaRepository<Disciplina, Integer>{
	   @Query(value = "select cod_disciplina as codigo, nome, aulas_semanais, dia_semana, hora_inicio, hora_fim, codigo_curso " +
               "from fn_listaDisciplina(:ra)",
       nativeQuery = true)
	   List<Object[]> listarDisciplinasPorRa(@Param("ra") String ra);
}
