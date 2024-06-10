package farias.paulino.kauan.AvaliacaoLabBD_3.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import farias.paulino.kauan.AvaliacaoLabBD_3.model.Aluno;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Avaliacao;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Curso;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Disciplina;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Matricula;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Notas;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Professor;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IAvaliacaoRepository;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IChamadaRepository;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.INotasRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
public class ProfessorNotasController {
	@Autowired
	private IChamadaRepository cRep;

	@Autowired
	private INotasRepository nRep;

	@Autowired
	private IAvaliacaoRepository aRep;
	
	@Autowired
	DataSource ds;

	@RequestMapping(name = "professorNotas", value = "/professorNotas", method = RequestMethod.GET)
	public ModelAndView professorNotasGet(@RequestParam Map<String, String> param, ModelMap model) {
		return new ModelAndView("professorNotas");
	}

	@RequestMapping(name = "notasRelatorio", value = "/notasRelatorio", method = RequestMethod.POST)
	public ResponseEntity notasRelatoriPost(@RequestParam Map<String, String> allparam, ModelMap model) {
		String codigo_disciplina = allparam.get("codigo_disciplina");
		String erro = "";
		
		// Definindo os elementos que serão passas como parâmetros para o Jasper
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("disc", codigo_disciplina);

		byte[] bytes = null;

		// Inicializando elementos do response
		InputStreamResource resource = null;
		HttpStatus status = null;
		HttpHeaders header = new HttpHeaders();

		try {
			Connection conn = DataSourceUtils.getConnection(ds);
			File arquivo = ResourceUtils.getFile("classpath:reports/ProfessorNotas.jasper");
			JasperReport report = (JasperReport) JRLoader.loadObjectFromFile(arquivo.getAbsolutePath());
			bytes = JasperRunManager.runReportToPdf(report, param, conn);
		} catch (FileNotFoundException | JRException e) {
			e.printStackTrace();
			erro = e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		} finally {
			if (erro.equals("")) {
				InputStream inputStream = new ByteArrayInputStream(bytes);
				resource = new InputStreamResource(inputStream);
				header.setContentLength(bytes.length);
				header.setContentType(MediaType.APPLICATION_PDF);
				status = HttpStatus.OK;
			}
		}

		return new ResponseEntity(resource, header, status);
	}

	

	@RequestMapping(name = "professorNotas", value = "/professorNotas", method = RequestMethod.POST)
	public ModelAndView professorNotasPost(@RequestParam Map<String, String> param, ModelMap model) {
		// Entrada
		String cmd = param.get("botao");
		String matricula_professor = param.get("matricula");
		String codigo_disciplina = param.get("codigo_disciplina");
		String nome_disciplina = param.get("nome_disciplina");
		String codigo_avaliacao = param.get("codigo_avaliacao");
		String aluno_ra = param.get("aluno_ra");
		String nome_aluno = param.get("nome_aluno");
		String ano_semestre = param.get("ano_semestre");
		String valor_nota = param.get("valor_nota");

		// Saida
		String saida = "";
		String erro = "";
		Professor professor = new Professor();
		Disciplina disciplina = new Disciplina();
		Avaliacao avaliacao = new Avaliacao();
		List<Disciplina> disciplinas = new ArrayList<>();
		List<Matricula> matriculas = new ArrayList<>();
		List<Avaliacao> avaliacoes = new ArrayList<>();
		List<Notas> notas = new ArrayList<>();
		Notas nota = new Notas();

		if (cmd != null) {
			if (cmd.equals("Listar Disciplinas") || cmd.equals("Acessar") || cmd.equals("Atualizar")
					|| cmd.equals("Inserir") || cmd.equals("Lancar Notas") && !matricula_professor.trim().isEmpty()) {
				professor.setMatricula(Integer.parseInt(matricula_professor));
			}
			if (cmd.equals("Acessar") || cmd.equals("Lancar Notas") || cmd.equals("Inserir")
					|| cmd.equals("Atualizar")) {
				disciplina.setCodigo(Integer.parseInt(codigo_disciplina));
				disciplina.setNome(nome_disciplina);
			}
			if (cmd.equals("Lancar Notas") || cmd.equals("Inserir") || cmd.equals("Atualizar")) {
				Matricula m = new Matricula();
				Aluno a = new Aluno();
				a.setRa(aluno_ra);
				a.setNome(nome_aluno);

				m.setAluno(a);
				m.setDisciplina(disciplina);
				m.setAno_semestre(ano_semestre);

				nota.setMatricula(m);
			}
			if (cmd.equals("Inserir") || cmd.equals("Atualizar")) {
				try {
					avaliacao.setCodigo(Integer.parseInt(codigo_avaliacao));
				} catch (Exception e) {
					saida = "Selecione uma avaliacao valida";
					model.addAttribute("saida", saida);
					return new ModelAndView("professorNotas");
				}

				nota.setAvaliacao(avaliacao);
				nota.setNota(Double.parseDouble(valor_nota));
			}
		}
		try {
			if (cmd != null) {
				if (cmd.equals("Listar Disciplinas")) {
					if (!matricula_professor.trim().isEmpty()) {
						disciplinas = listarDisciplinas(professor);
					} else {
						saida = "Matricula não pode estar vazia";
					}
				}
				if (cmd.equals("Acessar")) {
					matriculas = lista_matriculas(disciplina);
					avaliacoes = lista_avaliacoes(disciplina);
					notas = listar_notas(disciplina);
					if (avaliacoes.isEmpty()) {
						saida = "Esta materia não tem avalições cadastrada para lancamento de notas!";
						matriculas = new ArrayList<>();
						notas = new ArrayList<>();
					}
					if (matriculas.isEmpty()) {
						saida = "Esta materia não tem matriculas ativas";
						notas = new ArrayList<>();
						avaliacoes = new ArrayList<>();
					}
				}
				if (cmd.equals("Lancar Notas")) {
					avaliacoes = lista_avaliacoes(disciplina);
					model.addAttribute("nota", nota);
				}
				if (cmd.contains("Inserir")) {
					if (nota.getNota() >= 0 && nota.getNota() <= 10) {
						saida = inserir_atualizar_nota(nota, "i");
						avaliacoes = lista_avaliacoes(disciplina);
						notas = listar_notas(disciplina);
						matriculas = lista_matriculas(disciplina);
						model.addAttribute("nota", nota);
					} else {
						saida = "Insira notas validas, de 0 a 10";
						avaliacoes = lista_avaliacoes(disciplina);
						notas = listar_notas(disciplina);
						matriculas = lista_matriculas(disciplina);
					}
				}
				if (cmd.contains("Atualizar")) {
					if (nota.getNota() >= 0 && nota.getNota() <= 10) {
						saida = inserir_atualizar_nota(nota, "u");
						avaliacoes = lista_avaliacoes(disciplina);
						notas = listar_notas(disciplina);
						matriculas = lista_matriculas(disciplina);
						model.addAttribute("nota", nota);
					} else {
						saida = "Insira notas validas, de 0 a 10";
						avaliacoes = lista_avaliacoes(disciplina);
						notas = listar_notas(disciplina);
						matriculas = lista_matriculas(disciplina);
					}
				}
			} else {
				professor.setMatricula(Integer.parseInt(matricula_professor));
				disciplina.setCodigo(Integer.parseInt(codigo_disciplina));
				disciplina.setNome(nome_disciplina);
				nota = nRep.buscar_nota(aluno_ra, Integer.parseInt(codigo_disciplina),
						Integer.parseInt(codigo_avaliacao), ano_semestre);
				if (nota == null) {
					nota = new Notas();
				}
				Matricula m = new Matricula();
				Aluno a = new Aluno();
				a.setRa(aluno_ra);
				a.setNome(nome_aluno);

				m.setAluno(a);
				m.setDisciplina(disciplina);
				m.setAno_semestre(ano_semestre);

				nota.setMatricula(m);
				avaliacao.setCodigo(Integer.parseInt(codigo_avaliacao));
				nota.setAvaliacao(avaliacao);

				avaliacao = aRep.findById(Integer.parseInt(codigo_avaliacao)).orElse(new Avaliacao());
				avaliacoes = lista_avaliacoes(disciplina);

				model.addAttribute("nota", nota);
			}

		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
			professor = null;
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("professor", professor);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("matriculas", matriculas);
			model.addAttribute("avaliacoes", avaliacoes);
			model.addAttribute("avaliacao", avaliacao);
			model.addAttribute("notas", notas);
			model.addAttribute("nome_disciplina", nome_disciplina);
		}
		return new ModelAndView("professorNotas");
	}

	private List<Notas> listar_notas(Disciplina disciplina) {
		List<Notas> notas = new ArrayList<>();
		List<Object[]> objetos = nRep.listar_notas(disciplina.getCodigo());
		for (Object[] row : objetos) {
			Notas n = new Notas();
			Matricula m = new Matricula();
			n = nRep.findById((Integer) row[0]).orElse(new Notas());
			m = n.getMatricula();
			m.setSituacao((String) row[7]);

			notas.add(n);
		}
		return notas;
	}

	private String inserir_atualizar_nota(Notas nota, String acao) {
		return nRep.sp_iuNotas(acao, nota.getMatricula().getAluno().getRa(),
				nota.getMatricula().getDisciplina().getCodigo(), nota.getNota(), nota.getAvaliacao().getCodigo(),
				nota.getMatricula().getAno_semestre());
	}

	private List<Avaliacao> lista_avaliacoes(Disciplina disciplina) {
		List<Object[]> objetos = nRep.listar_avaliacoes(disciplina.getCodigo());
		List<Avaliacao> avaliacoes = new ArrayList<>();

		for (Object[] row : objetos) {
			Avaliacao av = new Avaliacao();
			av.setCodigo((Integer) row[0]);
			av.setTipo((String) row[1]);
			avaliacoes.add(av);
		}
		return avaliacoes;
	}

	private List<Matricula> lista_matriculas(Disciplina disciplina) {
		List<Object[]> objetos = nRep.listar_matriculas(disciplina.getCodigo());
		List<Matricula> matriculas = new ArrayList<>();
		for (Object[] row : objetos) {
			Matricula m = new Matricula();
			Aluno a = new Aluno();

			a.setRa((String) row[0]);
			a.setNome((String) row[1]);
			m.setAluno(a);

			m.setAno_semestre((String) row[3]);
			m.setDisciplina(disciplina);

			matriculas.add(m);
		}
		return matriculas;
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

}
