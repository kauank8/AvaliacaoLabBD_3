package farias.paulino.kauan.AvaliacaoLabBD_3.controller;

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
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Disciplina;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Matricula;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IMatriculaRepository;

@Controller
public class AlunoListarMatriculasController {
	@Autowired
	IMatriculaRepository mRep;
	
	@RequestMapping(name = "aluno", value = "/aluno", method = RequestMethod.GET)
	public ModelAndView AlunoGet(ModelMap model) {
		return new ModelAndView("aluno");
	}

	@RequestMapping(name = "aluno", value = "/aluno", method = RequestMethod.POST)
	public ModelAndView AlunoPost(@RequestParam Map<String, String> param, ModelMap model) {
		// Entrada
		String cmd = param.get("botao");
		String ra = param.get("ra");

		// Retorno
		String saida = "";
		String erro = "";
		Matricula m = new Matricula();
		List<Matricula> matriculas = new ArrayList<>();
		
		if (cmd.equals("Listar Matriculas Ativas")) {
			if (ra.trim().isEmpty()) {
				saida = "Ra em branco";
			} else {
				Aluno a = new Aluno();
				a.setRa(ra);
				m.setAluno(a);
			}
		}
		try {
			if (cmd.contains("Listar Matriculas") || cmd.contains("Listar Matriculas Ativas")) {
				matriculas = listarMatricula(m);
			}
		} catch (Exception e) {
			erro = e.getMessage();
			m = null;
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("matricula", m);
			model.addAttribute("matriculas", matriculas);
			
			return new ModelAndView("aluno");
		}
	}
	private List<Matricula> listarMatricula(Matricula m) {
		List<Object[]> saidas = mRep.listarMatriculasPorRa(m.getAluno().getRa());
		List<Matricula> matriculas = new ArrayList<>();
		for (Object[] resultado : saidas) {
		    Matricula matricula = new Matricula();
		    Disciplina disciplina = new Disciplina();

		    disciplina.setNome((String) resultado[0]);
		    disciplina.setDia_semana((String) resultado[1]);
		    disciplina.setHora_inicio((String) resultado[2]);
		    disciplina.setHora_fim((String) resultado[3]);

		    matricula.setFrequencia((Double) resultado[4]);
		    matricula.setNota((String) resultado[5]);
		    matricula.setSituacao((String) resultado[6]);
		    matricula.setDisciplina(disciplina);

		    matriculas.add(matricula);
		}
		return matriculas;
	}
}
