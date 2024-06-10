package farias.paulino.kauan.AvaliacaoLabBD_3.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
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
import jakarta.transaction.Transactional;

@Controller
public class SecretariaCadastrarAlunoController {
	@Autowired
	private IAlunoRepository aRep;

	@RequestMapping(name = "secretaria", value = "/secretaria", method = RequestMethod.GET)
	public ModelAndView SecretariaGet(ModelMap model) {
		return new ModelAndView("secretaria");
	}

	@RequestMapping(name = "secretaria", value = "/secretaria", method = RequestMethod.POST)
	public ModelAndView SecretariaPost(@RequestParam Map<String, String> param, ModelMap model) {
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
				
				if (cmd.contains("Cadastrar")) {
					if (cpf.isEmpty() || nome.isEmpty() || data_nasc.isEmpty() || conclusao.isEmpty() || email_pessoal.isEmpty()
							|| email_corporativo.isEmpty() || instituicao.isEmpty() || pontuacao.isEmpty() || posicao.isEmpty()
							|| cod_curso.isEmpty()) {
						saida = "Todos campos são obrigatórios, com excessão de nome social";
						model.addAttribute("saida", saida);
						return new ModelAndView("secretaria");
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
					if (cmd.contains("Cadastrar")) {
						a.setRa(geraRa());
						saida = cadastraAluno(a) + ", Ra gerado:" + a.getRa();
					}
				}
				catch (Exception  e) {
		            erro = trata_erros(e.getMessage());
		        }finally {
					model.addAttribute("saida", saida);
					model.addAttribute("erro", erro);
					model.addAttribute("aluno", a);
					model.addAttribute("alunos", alunos);

					return new ModelAndView("secretaria");
				}
	}


	@Transactional
	private String geraRa() throws SQLException{
		return aRep.sp_geraRa();
	}
	
	private String cadastraAluno(Aluno a){
		 return aRep.sp_aluno("i", a.getCurso().getCodigo(), a.getRa(), a.getCpf(), a.getNome(), 
				a.getNome_social(), Date.valueOf(a.getData_nasc()), Date.valueOf(a.getConclusao_segundo_grau()), 
				a.getEmail_pessoal(), a.getEmail_corporativo(), a.getInstituicao_segundo_grau(), a.getPontuacao_vestibular(),
				a.getPosicao_vestibular());
	}
	
	
	private String trata_erros(String message) {
		if(message.contains("Idade Invalida")) {
			return "Idade Invalida";
		}
		if(message.contains("Numero de digitos invalido")) {
			return "Numero de digitos invalido";
		}
		if(message.contains("Todos os digitos são iguais ")) {
			return "Todos os digitos são iguais ";
		}
		if(message.contains("CPF Invalido")) {
			return "Cpf Invalido ";
		}
		return message;
	}
}
