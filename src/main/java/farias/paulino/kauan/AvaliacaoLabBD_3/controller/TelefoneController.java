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
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Telefone;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.TelefoneId;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.ITelefoneRepository;

@Controller
public class TelefoneController {
	@Autowired
	ITelefoneRepository aRep;

	@RequestMapping(name = "telefone", value = "/telefone", method = RequestMethod.GET)
	public ModelAndView TelefoneGet(ModelMap model) {
		return new ModelAndView("telefone");
	}

	@RequestMapping(name = "telefone", value = "/telefone", method = RequestMethod.POST)
	public ModelAndView TelefonePost(@RequestParam Map<String, String> param, ModelMap model) {
		// Entrada
		String cmd = param.get("botao");
		String ra = param.get("ra");
		String numero = param.get("numero");
		String numero_novo = param.get("numero_novo");
		String numero_antigo = param.get("numero_antigo");

		// Retorno
		String saida = "";
		String erro = "";
		Telefone tel = new Telefone();
		List<Telefone> telefones = new ArrayList<>();
		

		if (!ra.isEmpty()) {
			Aluno a = new Aluno();
			a.setRa(ra);
			tel.setAluno(a);
		}

		if (cmd.contains("Cadastrar") || cmd.contains("Excluir")) {
			tel.setNumero(numero);
		}

		if (cmd.contains("Alterar")) {
			tel.setNumero(numero_novo);
		}

		try {
			if (cmd.contains("Cadastrar")) {
				if (numero.isEmpty() || ra.isEmpty()) {
					saida = "Preencha todos os campos referente a Cadastrar";
				} else {
					saida = cadastrarTelefone(tel);
					tel = null;
				}
			}

			if (cmd.contains("Alterar")) {
				if (numero_novo.isEmpty() || ra.isEmpty() || numero_antigo.isEmpty() || numero_novo.isEmpty()) {
					saida = "Preencha todos os campos referente a Alterar";
				} else {
					Telefone tel_antigo = new Telefone();
					tel_antigo.setNumero(numero_antigo);
					saida = alterarTelefone(tel, tel_antigo);
					tel = null;
				}
			}
			if (cmd.contains("Excluir")) {
				if (numero.isEmpty() || ra.isEmpty()) {
					saida = "Preencha todos os campos referente a Excluir";
				} else {
					saida = excluirTelefone(tel);
					tel = null;
				}
			}
			if (cmd.contains("Listar")) {
				if (ra.isEmpty()) {
					saida = "Preencha o ra do aluno que deseja listar os telefones";
				} else {
					telefones = listarTelefone(tel);
					if(telefones.isEmpty()) {
						erro = "Não existe telefone para esse Ra";
					}
					tel = null;
				}
			}
		} catch (Exception e) {
			 erro = trata_erros(e.getMessage());
			tel = null;
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("telefone", tel);
			model.addAttribute("telefones", telefones);

			return new ModelAndView("telefone");
		}

	}

	private String trata_erros(String message) {
		if(message.contains("Esse numero não existe")) {
			return "Esse numero não existe";
		}
		if(message.contains("Telefone Invalido, O numero de digitos de ser exatamente 11")) {
			return "Telefone Invalido, O numero de digitos de ser exatamente 11";
		}
		if(message.contains("Ra Inexistente")) {
			return "Ra Inexistente";
		}
		if(message.contains("O numero novo não contem 11 digitos")) {
			return "O numero novo não contem 11 digitos";
		}
		
		return message;
	}

	private List<Telefone> listarTelefone(Telefone tel) {
		return aRep.findByAlunoRa(tel.getAluno().getRa());
	}

	private String excluirTelefone(Telefone tel) {
		return aRep.excluiTelefone(tel.getAluno().getRa(), tel.getNumero());
	}

	private String alterarTelefone(Telefone tel, Telefone tel_antigo) {
		return aRep.alterarTelefone(tel.getAluno().getRa(), tel.getNumero(), tel_antigo.getNumero());
	}

	private String cadastrarTelefone(Telefone tel) {
		return aRep.insereTelefone(tel.getAluno().getRa(), tel.getNumero());
	}
}
