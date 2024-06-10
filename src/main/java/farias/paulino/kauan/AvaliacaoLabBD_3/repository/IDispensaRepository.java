package farias.paulino.kauan.AvaliacaoLabBD_3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import farias.paulino.kauan.AvaliacaoLabBD_3.model.Dispensa;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.DispensaId;

public interface IDispensaRepository extends JpaRepository<Dispensa, DispensaId> {
	 @Query(value = """
	 		Select d.codigo, d.nome, d.aulas_semanais, Cast(d.hora_inicio as varchar(5)) as hora_inicio, 
	 		Cast(d.hora_fim as varchar(5) ) as hora_fim, d.dia_semana, d.semestre
	 		from Disciplina d Left Outer join Dispensa dp on  d.codigo = dp.codigo_disciplina and dp.aluno_ra = :alunoRa
	 		where  dp.aluno_ra is null
	 		order by semestre asc
	 		""",
			 nativeQuery = true)
	    	List<Object[]> lista_disciplinas_dispensas(@Param("alunoRa") String alunoRa);
	    	
	 @Query(value = "SELECT * FROM Dispensa where aluno_ra = :alunoRa", nativeQuery = true)	
	 		List<Dispensa> lista_dispensas_aluno (@Param("alunoRa") String alunoRa);
	 
	 @Query(value= """
	 		SELECT d.aluno_ra, a.nome, d.codigo_disciplina, d.motivo, d.data_solicitacao, d.status_dispensa 
			FROM Dispensa d, Aluno a where d.aluno_ra = a.ra and d.aluno_ra = :alunoRa and d.status_dispensa = 'Em Andamento'
			order by a.nome asc
	 		""", nativeQuery=true)
	 List<Dispensa> filtra_dispensas_aluno (@Param("alunoRa") String alunoRa);
	 
	 @Query(value="""
	 		SELECT d.aluno_ra, a.nome, d.codigo_disciplina, d.motivo, d.data_solicitacao, d.status_dispensa 
			FROM Dispensa d, Aluno a where d.aluno_ra = a.ra and d.status_dispensa = 'Em Andamento'
			order by a.nome asc
	 		""", nativeQuery = true)
	 List<Dispensa> lista_dispensa_em_andamento();
}
