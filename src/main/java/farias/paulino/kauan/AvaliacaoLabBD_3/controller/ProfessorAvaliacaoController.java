package farias.paulino.kauan.AvaliacaoLabBD_3.controller;

import java.sql.SQLException;
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

import farias.paulino.kauan.AvaliacaoLabBD_3.model.Avaliacao;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Curso;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Disciplina;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Professor;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IAvaliacaoRepository;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IChamadaRepository;

@Controller
public class ProfessorAvaliacaoController {
	@Autowired
	private IChamadaRepository cRep;
	@Autowired
	private IAvaliacaoRepository aRep;

	@RequestMapping(name = "professorAvaliacao", value = "/professorAvaliacao", method = RequestMethod.GET)
	public ModelAndView professorAvaliacaoGet(@RequestParam Map<String, String> param, ModelMap model) {
		return new ModelAndView("professorAvaliacao");
	}

	@RequestMapping(name = "professorAvaliacao", value = "/professorAvaliacao", method = RequestMethod.POST)
	public ModelAndView professorAvaliacaoPost(@RequestParam Map<String, String> param, ModelMap model) {
		// Entrada
		String cmd = param.get("botao");
		String matricula = param.get("matricula");
		String codigo_disciplina = param.get("codigo_disciplina");
		String nome_disciplina = param.get("nome_disciplina");
		String tipo = param.get("tipo");
		String peso = param.get("peso");
		String data_avaliacao = param.get("data_avaliacao");
		String codigo_avaliacao = param.get("codigo_avaliacao");

		// Saida
		String saida = "";
		String erro = "";
		Professor professor = new Professor();
		Disciplina disciplina = new Disciplina();
		Avaliacao avaliacao = new Avaliacao();
		List<Disciplina> disciplinas = new ArrayList<>();
		List<Avaliacao> avaliacoes = new ArrayList<>();

		if (cmd.equals("Listar Disciplinas") || cmd.equals("Acessar") || cmd.equals("Atualizar")
				|| cmd.equals("Cadastrar") || cmd.equals("Editar") && !matricula.trim().isEmpty()) {
			professor.setMatricula(Integer.parseInt(matricula));
		}
		if (cmd.equals("Acessar") || cmd.equals("Editar") || cmd.equals("Cadastrar")) {
			disciplina.setCodigo(Integer.parseInt(codigo_disciplina));
			disciplina.setNome(nome_disciplina);
			avaliacao.setDisciplina(disciplina);
		}
		if (cmd.equals("Cadastrar") || cmd.equals("Editar")) {
			if (!peso.trim().isEmpty() && !tipo.trim().isEmpty() && !data_avaliacao.trim().isEmpty()) {
				avaliacao.setData_avaliacao(LocalDate.parse(data_avaliacao));
				avaliacao.setTipo(tipo);
				avaliacao.setPeso(Double.parseDouble(peso));
			}
			 else {
					saida = "Todos os campos devem estar preenchido";
				}
		}
		try {
			if (cmd.equals("Listar Disciplinas")) {
				if (!matricula.trim().isEmpty()) {
					disciplinas = listarDisciplinas(professor);
				} else {
					saida = "Matricula não pode estar vazia";
				}
			}
			if (cmd.equals("Acessar")) {
				avaliacoes = lista_avalicoes_disciplina(disciplina);
				model.addAttribute("avaliacao", avaliacao);

			}
			if (cmd.equals("Cadastrar")) {
				if (!peso.trim().isEmpty() && !tipo.trim().isEmpty() && !data_avaliacao.trim().isEmpty()) {
					saida = cadastraAvaliacao(avaliacao);
					avaliacoes = lista_avalicoes_disciplina(disciplina);
					model.addAttribute("avaliacao", avaliacao);
				} else {
					saida = "Todos os campos devem estar preenchido";
				}

			}
			if (cmd.equals("Editar")) {
				avaliacao.setCodigo(Integer.parseInt(codigo_avaliacao));
				avaliacoes = lista_avalicoes_disciplina(disciplina);
				avaliacao.setDisciplina(avaliacoes.get(0).getDisciplina());
				model.addAttribute("avaliacao", avaliacao);

			}
			if (cmd.equals("Atualizar")) {
				avaliacao.setCodigo(Integer.parseInt(codigo_avaliacao));
				if (!tipo.trim().isEmpty() && !data_avaliacao.trim().isEmpty() && !peso.trim().isEmpty()) {
					avaliacao = aRep.findById(avaliacao.getCodigo()).orElse(new Avaliacao());
					if (avaliacao != null) {
						avaliacao.setData_avaliacao(LocalDate.parse(data_avaliacao));
						avaliacao.setTipo(tipo);
						avaliacao.setPeso(Double.parseDouble(peso));
						aRep.save(avaliacao);
						saida = "Metodo atualizado com sucesso";
						avaliacoes = lista_avalicoes_disciplina(avaliacao.getDisciplina());
						model.addAttribute("avaliacao", avaliacao);
					}
				} else {
					saida = "Todos os campos devem estar preenchido";
				}
			}
		} catch (Exception e) {
			erro = trata_erros(e.getMessage());
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("professor", professor);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("avaliacoes", avaliacoes);
		}
		return new ModelAndView("professorAvaliacao");
	}

	private List<Avaliacao> lista_avalicoes_disciplina(Disciplina disciplina) {
		return aRep.lista_avaliacao_disciplina(disciplina.getCodigo());
	}

	private String cadastraAvaliacao(Avaliacao a) {
		return aRep.sp_insereAvaliacao(a.getTipo(), a.getPeso(), a.getData_avaliacao().toString(),
				a.getDisciplina().getCodigo());
	}

	private List<Disciplina> listarDisciplinas(Professor professor) throws ClassNotFoundException, SQLException {
		List<Object[]> objetos = cRep.listar_disciplinas_professor(professor.getMatricula());
		List<Disciplina> disciplinas = new ArrayList<>();
		for (Object[] row : objetos) {
			Disciplina disciplina = new Disciplina();
			disciplina.setCodigo((Integer) row[0]);
			disciplina.setNome((String) row[1]);
			disciplina.setAulas_semanais((Integer) row[2]);
			disciplina.setDia_semana((String) row[5]);
			disciplina.setHora_inicio((String) row[3]);
			disciplina.setHora_fim((String) row[4]);
			disciplina.setSemestre((Integer) row[6]);

			Curso curso = new Curso();
			curso.setNome((String) row[7]);
			disciplina.setCurso(curso);

			disciplinas.add(disciplina);
		}
		return disciplinas;
	}

	private String trata_erros(String message) {
		if (message.contains("A soma dos pesos das avaliações não pode ser maior que 1")) {
			return message = "A soma dos pesos das avaliações não pode ser maior que 1";
		}
		if (message.contains("Ja existe uma prova nessa data, para essa disciplina")) {
			return message = "Ja existe uma prova nessa data, para essa disciplina";
		}
		return message;
	}

}
