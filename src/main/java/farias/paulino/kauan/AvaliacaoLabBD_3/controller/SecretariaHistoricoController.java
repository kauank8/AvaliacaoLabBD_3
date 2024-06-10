package farias.paulino.kauan.AvaliacaoLabBD_3.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import farias.paulino.kauan.AvaliacaoLabBD_3.model.Aluno;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Curso;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Disciplina;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Matricula;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Professor;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IMatriculaRepository;

@Controller
public class SecretariaHistoricoController {
	@Autowired
	private IMatriculaRepository mRep;
	
	@RequestMapping(name = "secretariaHistorico", value = "/secretariaHistorico", method = RequestMethod.GET)
	public ModelAndView secretariaHistoricoGet(ModelMap model) {
		return new ModelAndView("secretariaHistorico");
	}
	
	@RequestMapping(name = "secretariaHistorico", value = "/secretariaHistorico", method = RequestMethod.POST)
	public ModelAndView secretariaHistoricoPost(@RequestParam Map<String, String> param, ModelMap model) {
		String cmd = param.get("botao");
		String ra = param.get("ra");
		
		// Retorno
		String saida = "";
		String erro = "";
		Aluno a = new Aluno();
		List<Matricula> matriculas = new ArrayList<>();
		
		if(cmd.contains("Buscar Historico")) {
			if (ra.trim().isEmpty()) {
				saida = "Ra em branco";
			} else {
				a.setRa(ra);
			}
		}
		
		try {
			if(cmd.contains("Buscar Historico")) {
				matriculas = buscar_historico(a);
				if(matriculas.isEmpty()) {
					saida = "Esse aluno não possui matriculas aprovada, verifique o Ra";
				}
			}
		}catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("aluno", a);
			model.addAttribute("matriculas", matriculas);
			
			return new ModelAndView("secretariaHistorico");
		}
		
		
	}

	private List<Matricula> buscar_historico(Aluno a) throws ClassNotFoundException, SQLException {
		List<Object[]> objetos = mRep.buscar_historico(a.getRa());
		List<Matricula> matriculas = new ArrayList<>();
		
		for(Object[] row:objetos) {
			Matricula m = new Matricula();
			Professor p = new Professor();
			Curso curso = new Curso();
			Disciplina d = new Disciplina();
			
			a.setNome((String) row[1]);
			curso.setNome((String) row[2]);
			a.setCurso(curso);
			a.setPontuacao_vestibular(((BigDecimal) row[4]).doubleValue());
			a.setPosicao_vestibular((Integer) row[5]);
			
			d.setCodigo((Integer) row[6]);
			d.setNome((String) row[7]);
			p.setNome((String) row[8]);
			d.setProfessor(p);
			
			
			m.setAluno(a);
			m.setAno_semestre(String.valueOf(row[3]));
			m.setDisciplina(d);
			m.setNota((String) row[9]);
			m.setQuantidade_faltas((Integer) row[10]);
			
			matriculas.add(m);
		}
		return matriculas;
	}
	
}
