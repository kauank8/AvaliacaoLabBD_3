package farias.paulino.kauan.AvaliacaoLabBD_3.controller;

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
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IAlunoRepository;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IAvaliacaoRepository;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.INotasRepository;

@Controller
public class AlunoAvaliacaoController {
	@Autowired
	private IAvaliacaoRepository avRep;
	@Autowired
	private IAlunoRepository aRep;
	@Autowired
	private INotasRepository ntRep;

	@RequestMapping(name = "alunoAvaliacao", value = "/alunoAvaliacao", method = RequestMethod.GET)
	public ModelAndView alunoAvaliacaoGet(@RequestParam Map<String, String> param, ModelMap model) {
		return new ModelAndView("alunoAvaliacao");
	}

	@RequestMapping(name = "alunoAvaliacao", value = "/alunoAvaliacao", method = RequestMethod.POST)
	public ModelAndView alunoAvaliacaoPost(@RequestParam Map<String, String> param, ModelMap model) {
		// Entrada
		String cmd = param.get("botao");
		String ra = param.get("ra");

		// Saida
		Aluno a = new Aluno();
		String saida = "";
		String erro = "";

		if (ra.trim().isEmpty()) {
			erro = "Ra Em branco";
			model.addAttribute("erro", erro);
			return new ModelAndView("alunoAvaliacao");
		}
		a.setRa(ra);
		a = aRep.findById(a.getRa()).orElse(new Aluno());

		if (a.getRa() ==  null) {
			erro = "Aluno não encotrado, verifique o ra";
			model.addAttribute("erro", erro);
			return new ModelAndView("alunoAvaliacao");
		}

		try {
			if (cmd.equals("Listar Avaliacoes")) {
				List<Object[]> objetos_avaliacao= avRep.listar_avaliacao_aluno(a.getRa());
				if(objetos_avaliacao.isEmpty()) {
					erro = "Não existem avalições cadastrada no momento";
				}
				model.addAttribute("avaliacoes",objetos_avaliacao);
			}
			if (cmd.equals("Listar Notas")) {
				List<Object[]> objetos_notas= ntRep.lista_notas_alunos(a.getRa());
				if(objetos_notas.isEmpty()) {
					erro = "Não existem notas lancadas no momento";
				}
				model.addAttribute("notas",objetos_notas);
			}
		} catch (Exception e) {
			erro = e.getMessage();
			a = null;
		} finally {
			model.addAttribute("aluno", a);
			model.addAttribute("erro", erro);
		}

		return new ModelAndView("alunoAvaliacao");
	}
}
