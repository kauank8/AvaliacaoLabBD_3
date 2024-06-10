package farias.paulino.kauan.AvaliacaoLabBD_3.controller;

import java.sql.Date;
import java.time.LocalDate;
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
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IAlunoRepository;

@Controller
public class SecretariaVisualizarAlunoController {
	@Autowired
	private IAlunoRepository aRep;

	@RequestMapping(name = "visualizarAluno", value = "/visualizarAluno", method = RequestMethod.GET)
	public ModelAndView visualizarGet(ModelMap model) {
		return new ModelAndView("visualizarAluno");
	}

	@RequestMapping(name = "visualizarAluno", value = "/visualizarAluno", method = RequestMethod.POST)
	public ModelAndView visualizarPost(@RequestParam Map<String, String> param, ModelMap model) {
		// Entrada
		String cmd = param.get("botao");
		String cod_curso = param.get("codigo_curso");
		String cpf = param.get("cpf");
		String nome = param.get("nome");
		String nome_social = param.get("nome_social");
		String data_nasc = param.get("data_nasc");
		String conclusao = param.get("conclusao_segundo_grau");
		String email_pessoal = param.get("email_pessoal");
		String email_corporativo = param.get("email_corporativo");
		String instituicao = param.get("instituicao_segundo_grau");
		String pontuacao = param.get("pontuacao_vestibular");
		String posicao = param.get("posicao_vestibular");
		String ra = "";

		// Retorno
		String saida = "";
		String erro = "";
		Aluno a = new Aluno();
		List<Aluno> alunos = new ArrayList<>();

		if (cmd.contains("Buscar") || cmd.contains("Alterar")) {
			ra = param.get("ra");
			a.setRa(ra);
		}
		
		if (cmd.contains("Alterar")) {
			if (cpf.isEmpty() || nome.isEmpty() || data_nasc.isEmpty() || conclusao.isEmpty() || email_pessoal.isEmpty()
					|| email_corporativo.isEmpty() || instituicao.isEmpty() || pontuacao.isEmpty() || posicao.isEmpty()
					|| cod_curso.isEmpty()) {
				saida = "Todos campos são obrigatórios, com excessão de nome social";
				model.addAttribute("saida", saida);
				return new ModelAndView("visualizarAluno");
			} else {
				a.setCpf(cpf);
				a.setNome(nome);
				a.setNome_social(nome_social);
				a.setData_nasc(LocalDate.parse(data_nasc));
				a.setConclusao_segundo_grau(LocalDate.parse(conclusao));
				a.setEmail_pessoal(email_pessoal);
				a.setEmail_corporativo(email_corporativo);
				a.setInstituicao_segundo_grau(instituicao);
				a.setPontuacao_vestibular(Double.parseDouble(pontuacao));
				a.setPosicao_vestibular(Integer.parseInt(posicao));
				Curso c = new Curso();
				c.setCodigo(Integer.parseInt(cod_curso));
				a.setCurso(c);
			}
		}
		try {
			if (cmd.contains("Buscar")) {
				a = aRep.findById(a.getRa()).orElse(new Aluno());
				if(a.getRa() ==  null || a.getRa().isEmpty()) {
					erro = "Aluno não encotrado";
				}
			}
			if (cmd.contains("Alterar")) {
				saida = alterarAluno(a);
				if (saida == null) {
					saida = "CPF e Email corporativo não podem ser alterado";
				}
			}
			if (cmd.contains("Listar")) {
				alunos = aRep.findAll();
			}
		}
		catch (Exception e) {
			erro = trata_erros(e.getMessage());
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("aluno", a);
			model.addAttribute("alunos", alunos);

			return new ModelAndView("visualizarAluno");
		}
	}

	

	private String alterarAluno(Aluno a) {
		return aRep.sp_aluno("u", a.getCurso().getCodigo(), a.getRa(), a.getCpf(), a.getNome(), 
				a.getNome_social(), Date.valueOf(a.getData_nasc()), Date.valueOf(a.getConclusao_segundo_grau()), 
				a.getEmail_pessoal(), a.getEmail_corporativo(), a.getInstituicao_segundo_grau(), a.getPontuacao_vestibular(),
				a.getPosicao_vestibular());
	}
	
	private String trata_erros(String message) {
		if(message.contains("Idade Invalida")) {
			return "Idade Invalida";
		}
		return message;
	}
}
