package farias.paulino.kauan.AvaliacaoLabBD_3.controller;

import java.sql.Date;
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

import farias.paulino.kauan.AvaliacaoLabBD_3.model.Aluno;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Chamada;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Curso;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Disciplina;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Matricula;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Professor;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IChamadaRepository;

@Controller
public class ProfessorChamadaController {
	@Autowired
	private IChamadaRepository cRep;

	@RequestMapping(name = "professorChamada", value = "/professorChamada", method = RequestMethod.GET)
	public ModelAndView professorChamadaGet(@RequestParam Map<String, String> param, ModelMap model) {
		return new ModelAndView("professorChamada");
	}

	@RequestMapping(name = "professorChamada", value = "/professorChamada", method = RequestMethod.POST)
	public ModelAndView professorChamadaPost(@RequestParam Map<String, String> param, ModelMap model) {
		// Entrada
		String cmd = param.get("botao");
		String matricula = param.get("matricula");
		String codigo_disciplina = param.get("codigo_disciplina");
		String nome_disciplina = param.get("nome_disciplina");
		String ra_aluno = param.get("ra_aluno");
		String data_chamada = param.get("data_chamada");
		
		// Saida
		String saida = "";
		String erro = "";
		Professor professor = new Professor();
		Disciplina disciplina = new Disciplina();
		Chamada chamada = new Chamada();
		List<Disciplina> disciplinas = new ArrayList<>();
		List<Chamada> chamadas = new ArrayList<>();
		List<Matricula> matriculas = new ArrayList<>();
		String horarios[] = new String[4];
		List<Chamada> editar_chamada = new ArrayList<>();
		
		if (cmd.equals("Listar Disciplinas") || cmd.equals("Acessar") || cmd.equals("Realizar nova chamada") 
				|| cmd.equals("Confirmar chamada") || cmd.equals("Editar") || cmd.equals("Editar chamada") && !matricula.trim().isEmpty()) {
			professor.setMatricula(Integer.parseInt(matricula));
		}
		if(cmd.equals("Acessar")) {
			disciplina.setCodigo(Integer.parseInt(codigo_disciplina));
			disciplina.setNome(nome_disciplina);
		}
		if(cmd.equals("Realizar nova chamada")) {
			disciplina.setCodigo(Integer.parseInt(codigo_disciplina.trim()));
		}
		if(cmd.equals("Confirmar chamada")) {
			disciplina.setCodigo(Integer.parseInt(codigo_disciplina.trim()));
		}
		if(cmd.equals("Editar") || cmd.equals("Editar chamada")) {
			disciplina.setCodigo(Integer.parseInt(codigo_disciplina.trim()));
			chamada.setData_aula(LocalDate.parse(data_chamada));
			Matricula m = new Matricula();
			m.setDisciplina(disciplina);
			chamada.setMatricula(m);
		}
		
		

		try {
			if (cmd.equals("Listar Disciplinas")) {
				if (!matricula.trim().isEmpty()) {
					disciplinas = listarDisciplinas(professor);
					chamadas = null;
				} else {
					saida = "Matricula não pode estar vazia";
				}
			}
			if(cmd.equals("Acessar")) {
				chamadas = listar_chamadas(disciplina);
				if(chamadas.isEmpty()) {
					Matricula m = new Matricula();
					m.setDisciplina(disciplina);
					chamada.setMatricula(m);
					chamadas.add(chamada);
				}
			}
			if(cmd.equals("Realizar nova chamada")) {
				List<Object[]> saidas = listar_alunosChamada(disciplina);
				matriculas = montaMatriculas(saidas, disciplina);
				horarios = lista_horarios(saidas);
				if(matriculas.isEmpty()) {
					saida = "Não há alunos matriculados nessa turma";
				}
			}
			if(cmd.equals("Confirmar chamada") || cmd.equals("Editar chamada")) {
				List<Object[]> saidas = listar_alunosChamada(disciplina);
				matriculas = montaMatriculas(saidas, disciplina);
				while(!matriculas.isEmpty()) {
					ra_aluno = matriculas.get(0).getAluno().getRa();
					
					String primeira_aula = param.get("check_"+ra_aluno+"_primeira_aula");
					String segunda_aula = param.get("check_"+ra_aluno+"_segunda_aula");
					String terceira_aula = param.get("check_"+ra_aluno+"_terceira_aula");
					String quarta_aula = param.get("check_"+ra_aluno+"_quarta_aula");
					
					if(primeira_aula == null) {
						primeira_aula = "0";
					}
					if(segunda_aula == null) {
						segunda_aula = "0";
					}
					if(terceira_aula == null) {
						terceira_aula = "0";
					}
					if(quarta_aula == null) {
						quarta_aula = "0";
					}
					
					Aluno a = new Aluno();
					a.setRa(ra_aluno);
					
					Matricula m = new Matricula();
					m.setAluno(a);
					m.setDisciplina(disciplina);
					
					chamada.setPresenca_primeira_aula(Integer.parseInt(primeira_aula));
					chamada.setPresenca_segunda_aula(Integer.parseInt(segunda_aula));
					chamada.setPresenca_terceira_aula(Integer.parseInt(terceira_aula));
					chamada.setPresenca_quarta_aula(Integer.parseInt(quarta_aula));
					
					chamada.setMatricula(m);
					if(cmd.equals("Confirmar chamada")) {
						saida = insere_chamada(chamada);
					}
					if(cmd.equals("Editar chamada")) {
						saida = atualiza_chamada(chamada);
					}
					matriculas.remove(0);
						}
					}
				if(cmd.equals("Editar")) {
					editar_chamada = editar_chamada(chamada);
					List<Object[]> saidas1 = listar_alunosChamada(disciplina);
					horarios = lista_horarios(saidas1);
				}
		} catch (Exception e) {
			erro = e.getMessage();
			professor = null;

		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("professor", professor);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("chamadas", chamadas);
			model.addAttribute("matriculas", matriculas);
			model.addAttribute("horarios", horarios);
			model.addAttribute("editar_chamada", editar_chamada);
			return new ModelAndView("professorChamada");
		}

	}

	
	private String atualiza_chamada(Chamada chamada) throws ClassNotFoundException, SQLException {
		return cRep.sp_atualizaChamada(chamada.getMatricula().getAluno().getRa(), chamada.getMatricula().getDisciplina().getCodigo(),
				chamada.getPresenca_primeira_aula(), chamada.getPresenca_segunda_aula(), chamada.getPresenca_terceira_aula(), 
				chamada.getPresenca_quarta_aula(), chamada.getData_aula().toString());
	}

	private List<Chamada> editar_chamada(Chamada chamada) throws ClassNotFoundException, SQLException {
		List<Object[]> objetos = cRep.listar_editar_chamada(chamada.getMatricula().getDisciplina().getCodigo(), chamada.getData_aula());
		List<Chamada> chamadas = new ArrayList<>();
		for (Object[] row : objetos) {
			Chamada c = new Chamada();
			Matricula m = new Matricula();
			Aluno a = new Aluno();
			Disciplina d = new Disciplina();
			
			a.setNome((String) row[7]);
			a.setRa((String)row[6]);
			m.setAluno(a);
			
			d.setCodigo((Integer) row[5]);
			m.setDisciplina(d);
			
			c.setPresenca_primeira_aula((Integer) row[0]);
			c.setPresenca_segunda_aula((Integer) row[1]);
			if(row[2] != null) {
			c.setPresenca_terceira_aula((Integer) row[2]);
			}
			if(row[3] !=null) {
			c.setPresenca_quarta_aula((Integer) row[3]);
			}
			Date data = ((Date) row[4]);
			c.setData_aula(data.toLocalDate());
			
			m.setAno_semestre((String)row[8]);
			c.setMatricula(m);
			
			chamadas.add(c);
		}
		return chamadas;
	}

	private String insere_chamada(Chamada chamada) throws ClassNotFoundException, SQLException {
		return cRep.sp_insereChamada(chamada.getMatricula().getAluno().getRa(), chamada.getMatricula().getDisciplina().getCodigo(),
				chamada.getPresenca_primeira_aula(), chamada.getPresenca_segunda_aula(), chamada.getPresenca_terceira_aula(), 
				chamada.getPresenca_quarta_aula());
	}

	private List<Object[]> listar_alunosChamada(Disciplina disciplina) throws ClassNotFoundException, SQLException {
		List<Object[]> objetos =  cRep.fn_alunosChamada(disciplina.getCodigo());
		return objetos; 
	}

	private List<Chamada> listar_chamadas(Disciplina disciplina) throws ClassNotFoundException, SQLException {
		List<Object[]> objetos = cRep.listar_chamadas(disciplina.getCodigo());
		List<Chamada> chamadas = new ArrayList<>();
		for (Object[] row : objetos) {
			Matricula m = new Matricula();
			m.setDisciplina(disciplina);
			Chamada c = new Chamada();
			Date data = ((Date) row[0]);
			c.setData_aula(data.toLocalDate());
			c.setMatricula(m);
			chamadas.add(c);
		}
		return chamadas;
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
		    disciplina.setSemestre((Integer)row[6]);
		    
		    Curso curso = new Curso();
			curso.setNome((String)row[7]);
			disciplina.setCurso(curso);
			
		    disciplinas.add(disciplina);
		}
		return disciplinas;
	}
	private String[] lista_horarios(List<Object[]> saidas) {
		String vt[] = new String[4];
		for(Object[] row: saidas ) {
			vt[0] = (String)row[4];
			vt[1] = (String)row[5];
			vt[2] = (String)row[6];
			vt[3] = (String)row[7];
			break;
		}
		return vt;
	}
	
	private List<Matricula> montaMatriculas(List<Object[]> saidas, Disciplina d) {
		List<Matricula> matriculas = new ArrayList<>();
		for(Object[] row: saidas ){
			Matricula m = new Matricula();
			Aluno a = new Aluno();
			Disciplina disciplina = new Disciplina();
			
			a.setNome((String) row[2]);
			a.setRa((String) row[1]);
			m.setAluno(a);
			
			disciplina.setAulas_semanais((Integer) row[3]);
			disciplina.setCodigo(d.getCodigo());
			m.setDisciplina(disciplina);
			
			m.setAno_semestre((String) row[0]);
			matriculas.add(m);
		}
		return matriculas;
	}
}
