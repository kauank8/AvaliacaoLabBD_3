package farias.paulino.kauan.AvaliacaoLabBD_3.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
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
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Chamada;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Curso;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Disciplina;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Matricula;
import farias.paulino.kauan.AvaliacaoLabBD_3.model.Professor;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IAlunoRepository;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IChamadaRepository;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IDisciplinaRepository;
import farias.paulino.kauan.AvaliacaoLabBD_3.repository.IMatriculaRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
public class ProfessorFaltasController {

	@Autowired
	private IChamadaRepository cRep;
	@Autowired
	private IAlunoRepository aRep;
	@Autowired
	private IMatriculaRepository mtRep;
	@Autowired
	IDisciplinaRepository discRep;
	@Autowired
	DataSource ds;

	@RequestMapping(name = "professorFalta", value = "/professorFalta", method = RequestMethod.GET)
	public ModelAndView professorFaltaGet(@RequestParam Map<String, String> param, ModelMap model) {
		return new ModelAndView("professorFalta");
	}
	
	@RequestMapping(name = "faltaRelatorio", value = "/faltaRelatorio", method = RequestMethod.POST)
	public ResponseEntity faltaRelatorioPost(@RequestParam Map<String, String> allparam, ModelMap model) {
		String codigo_disciplina = allparam.get("codigo_disciplina");
		Disciplina disciplina = discRep.findById(Integer.parseInt(codigo_disciplina)).orElse(new Disciplina());
		String erro = "";
		String turma = Integer.toString(mtRep.buscar_turma());
		
		// Definindo os elementos que serão passas como parâmetros para o Jasper
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("disc", codigo_disciplina);
		param.put("nome_disc", disciplina.getNome());
		param.put("turma", turma);

		byte[] bytes = null;

		// Inicializando elementos do response
		InputStreamResource resource = null;
		HttpStatus status = null;
		HttpHeaders header = new HttpHeaders();

		try {
			Connection conn = DataSourceUtils.getConnection(ds);
			File arquivo = ResourceUtils.getFile("classpath:reports/ProfessorFalta.jasper");
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
	
	@RequestMapping(name = "professorFalta", value = "/professorFalta", method = RequestMethod.POST)
	public ModelAndView professorFaltaPost(@RequestParam Map<String, String> param, ModelMap model) {
		// Entrada
		String cmd = param.get("botao");
		String matricula_professor = param.get("matricula");
		String codigo_disciplina = param.get("codigo_disciplina");
		String nome_disciplina = param.get("nome_disciplina");

		// Saida
		String saida = "";
		String erro = "";
		Professor professor = new Professor();
		Disciplina disciplina = new Disciplina();
		List<Disciplina> disciplinas = new ArrayList<>();
		List<Chamada> chamadas = new ArrayList<>();

		if (cmd.equals("Listar Disciplinas") || cmd.equals("Acessar") && !matricula_professor.trim().isEmpty()) {
			professor.setMatricula(Integer.parseInt(matricula_professor));
		}
		if(cmd.equals("Acessar")) {
			disciplina.setCodigo(Integer.parseInt(codigo_disciplina));
			disciplina.setNome(nome_disciplina);
		}
		try {
			if (cmd.equals("Listar Disciplinas")) {
				if (!matricula_professor.trim().isEmpty()) {
					disciplinas = listarDisciplinas(professor);
				} else {
					saida = "Matricula não pode estar vazia";
				}
			}
			if(cmd.equals("Acessar")) {
				List<Object[]> objetos = listar_chamadas(disciplina);
				disciplina = discRep.findById(disciplina.getCodigo()).orElse(new Disciplina());
				
				model.addAttribute("nome_disciplina", disciplina);
				model.addAttribute("chamadas", objetos);
			}
			
		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
			professor = null;
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("professor", professor);
			model.addAttribute("disciplinas", disciplinas);
		}
		return new ModelAndView("professorFalta");
	}

	private List<Object[]> listar_chamadas(Disciplina disciplina) {
		List<Object[]> objetos = cRep.listar_falta_semana(disciplina.getCodigo());
		return objetos;
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