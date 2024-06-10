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
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Dispensa;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.DispensaId;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IDispensaRepository;

@Controller
public class SecretariaDispensaController {
	@Autowired
	private IDispensaRepository dRep;

	@RequestMapping(name = "dispensaSecretaria", value = "/dispensaSecretaria", method = RequestMethod.GET)
	public ModelAndView secretariaDispensaGet(@RequestParam Map<String, String> param, ModelMap model) {
		List<Dispensa> dispensas = new ArrayList<>();
		String erro = "";
		String saida="";
		try {
			dispensas = listarDispensas();
			if(dispensas.isEmpty()) {
				saida = "Não há solicitações de dispensas nesse momento";
			}
		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		}finally {
		model.addAttribute("dispensas", dispensas);
		model.addAttribute("erro", erro);
		model.addAttribute("saida", saida);
		return new ModelAndView("dispensaSecretaria");
		}
	}

	
	@RequestMapping(name = "dispensaSecretaria", value = "/dispensaSecretaria", method = RequestMethod.POST)
	public ModelAndView secretariaDispensaPost(@RequestParam Map<String, String> param, ModelMap model) {
		//Entrada
		String cmd = param.get("botao");
		String ra = param.get("ra");
		String codigo_disciplina = param.get("codigo");
		
		//Saida
		String saida = "";
		String erro = "";
		Dispensa dispensa = new Dispensa();
		List<Dispensa> dispensas = new ArrayList<>();
		
		if(cmd.equals("Filtar por Ra") || cmd.equals("Aceitar") || cmd.equals("Recusar") && !ra.trim().isEmpty()) {
			Aluno a = new Aluno();
			a.setRa(ra);
			dispensa.setAluno(a);
		}
		if(cmd.equals("Aceitar") || cmd.equals("Recusar")) {
			Disciplina disciplina = new Disciplina();
			disciplina.setCodigo(Integer.parseInt(codigo_disciplina));
			dispensa.setDisciplina(disciplina);
		}
		try {
			if(cmd.equals("Filtar por Ra")) {
				if(!ra.trim().isEmpty()) {
					dispensas = filtrarRa(dispensa);
					if(dispensas.isEmpty()) {
						saida = "O aluno não tem dispensas em andamento";
					}
				}
				else {
					saida = "O ra não pode estar vazio";
					model.addAttribute("saida", saida);
					return new ModelAndView("dispensaSecretaria");
				}
			}
			if(cmd.equals("Aceitar")) {
				DispensaId dId = new DispensaId();
				dId.setAluno(dispensa.getAluno());
				dId.setDisciplina(dispensa.getDisciplina());
				dispensa=dRep.findById(dId).orElse(new Dispensa());
				
				dispensa.setStatus("Aprovada");
				saida = aceitar_recusar_dispensa(dispensa);
				dispensa = null;
				dispensas = listarDispensas();
			}
			if(cmd.equals("Recusar")) {
				DispensaId dId = new DispensaId();
				dId.setAluno(dispensa.getAluno());
				dId.setDisciplina(dispensa.getDisciplina());
				dispensa=dRep.findById(dId).orElse(new Dispensa());
				
				dispensa.setStatus("Reprovada");
				saida = aceitar_recusar_dispensa(dispensa);
				dispensa = null;
				dispensas = listarDispensas();
			}
		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		}finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("dispensa", dispensa);
			model.addAttribute("dispensas", dispensas);
			
			return new ModelAndView("dispensaSecretaria");
		}
		
		
	}
	
	private String aceitar_recusar_dispensa(Dispensa dispensa) throws ClassNotFoundException, SQLException {
		dRep.save(dispensa);
		return "Dispensa respondida com sucesso";
	}


	private List<Dispensa> filtrarRa(Dispensa dispensa)throws ClassNotFoundException, SQLException {
		return dRep.filtra_dispensas_aluno(dispensa.getAluno().getRa());
	}


	private List<Dispensa> listarDispensas() throws ClassNotFoundException, SQLException {
		return dRep.lista_dispensa_em_andamento();
	}
}
