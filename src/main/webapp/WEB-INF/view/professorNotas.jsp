<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href='<c:url value = "./resources/professorAvaliacao.css"/>'>
<title>Area de notas</title>
</head>
<body>
	<nav id = menu>
		<ul>
			<li><a href="index">Home</a>
			<li><a href="professorChamada" >Chamada</a>
			<li><a href="professorAvaliacao" >Avaliacao</a>
			<li><a href="professorNotas"  class="ativa">Notas</a>
			<li><a href="professorFalta" >Faltas</a>
		</ul>
	</nav>
	
	<div class="container_aluno">
		<h1>Aréa de Notas</h1>
		<form action="professorNotas" method="post" >
			<table>
				<tr>
					<td colspan="2">
						<input class="id_input_data" type="text"  id="matricula" name="matricula" placeholder="Matricula" 
						pattern="[0-9]*" title="Por favor, digite apenas números."
						 value='<c:out value="${professor.matricula }"></c:out>'> 
					</td>
				</tr>
				<tr>
					<td>
						<input class="botao" type="submit" id="botao" name="botao" value="Listar Disciplinas">
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div align="center">
		<c:if test="${not empty erro }">
			<h2><b> <c:out value="${erro }" /> </b></h2>
		</c:if>
	</div>
	
	<div align="center">
		<c:if test="${not empty saida }">
			<h3><b> <c:out value="${saida }" /> </b></h3>	
		</c:if>
	</div>
	
	<div align="center">
		<c:if test="${not empty disciplinas }">
		<h1>Disciplinas do professor</h1>
   		<table class="table_round">
       		<thead>
            	<tr>
            		<th class="lista">Disciplina</th>
            		<th class="lista">Dia semana</th>
                	<th class="lista">Aulas semanais</th>
                	<th class="lista">Hora de início</th>
                	<th class="lista">Hora de fim</th>
                	<th class="lista">Semestre</th>
                	<th class="lista">Curso</th>
                	<th class="lista">Acessar</th>
            	</tr>
        	</thead>
        	<tbody>
            	<c:forEach var="d" items="${disciplinas}">
                       	 	<tr>
                       	 		<td class="lista"><c:out value="${d.nome}" /></td>
                       	 		<td class="lista"><c:out value="${d.dia_semana}" /></td>
                            	<td class="lista"><c:out value="${d.aulas_semanais}" /></td>
                            	<td class="lista" ><c:out value="${d.hora_inicio}" /></td>
                            	<td class="lista"><c:out value="${d.hora_fim}" /></td>
                            	<td class="lista"><c:out value="${d.semestre}" /></td>
                            	<td class="lista"><c:out value="${d.curso.nome}" /></td>
                       			<td>
                       			 	<form class="lista" action="professorNotas" method="post">
                       			 		<input type="hidden" id="nome_disciplina" name="nome_disciplina" value="${d.nome}" />
                            	 		<input type="hidden" id="codigo_disciplina" name="codigo_disciplina" value="${d.codigo}" />
                            	 		<input type="hidden" id="matricula" name="matricula" value="${param.matricula}" />
                                		<input class="botao_lista" type="submit" name="botao" value="Acessar">
                            		</form>
                            	</td>
                        	</tr>
           	 	</c:forEach>
       		 </tbody>
   		 </table>
   		</c:if>
   	</div>
   	
	
	<div  class="container_aluno">
		<c:if test="${not empty nota}">
			<h1>Notas de: ${nota.matricula.aluno.nome} </h1>
			<form id="professorNotas" action="professorNotas" method="post">
				<input type="hidden" id="codigo_disciplina" name="codigo_disciplina" value="${nota.matricula.disciplina.codigo}" />
				<input type="hidden" id="ano_semestre" name="ano_semestre" value="${nota.matricula.ano_semestre}" />
				<input type="hidden" id="matricula" name="matricula" value="${param.matricula}" />
				<input type="hidden" id="nome_aluno" name="nome_aluno" value="${nota.matricula.aluno.nome}" />
				<input type="hidden" id="nome_disciplina" name="nome_disciplina" value="${nome_disciplina}" />
			<table>
				<tr>
					<td colspan="2">
						<input class="input_data" type="text"  id="aluno_ra" name="aluno_ra" placeholder="aluno_ra" readonly="readonly"
						 value='<c:out value="${nota.matricula.aluno.ra}"></c:out>'> 
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<select name = "codigo_avaliacao" id="codigo_avaliacao" onchange="submitForm()">
							<option disabled ${avaliacao.tipo == null ? 'selected' : ''}>Tipo Avaliacao</option>
							<c:forEach var="av" items="${avaliacoes}">
								 <option value="${av.codigo}" 
                       				 ${avaliacao.codigo != null && avaliacao.codigo == av.codigo ? 'selected' : ''}>
                   					 <c:out value="${av.tipo}" />
                				</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="1">
						<label class="label_data"> Nota:</label>
					</td>
					<td colspan="1">
						<input class="input_data" type="text"  id="valor_nota" name="valor_nota"
						pattern="[0-9.]*" title="Por favor, digite apenas números, e ponto para decimais."  
						 value='<c:out value="${nota.nota }"></c:out>'> 
					</td>
				</tr>
				<tr>
					<td>
						<input class="botao" type="submit" id="botao" name="botao" value="Inserir">
					</td>
					<td>
						<input class="botao" type="submit" id="botao" name="botao" value="Atualizar">
					</td>
				</tr>
			</table>
			<script>
			function submitForm() {
			document.getElementById("professorNotas")
			.submit();
		}
		</script>
			
			</form>
			<br/>
			<br/>
   		</c:if>
   	</div>
   	
  	<div align="center">
		<c:if test="${not empty matriculas }">
			<h1>Notas de: ${matriculas[0].disciplina.nome} </h1>
			<table class="table_round">
			<thead>
            	<tr>
            		<th class="lista">Ra</th>
            		<th class="lista">Aluno</th>
            		<c:forEach var="a" items="${avaliacoes}">
            			<th class="lista"><c:out value="${a.tipo}" /></th>
            		</c:forEach>
            		<th class="lista">Media</th>
            		<th class="lista">Situacao</th>
            		<th class="lista">Manipular Notas</th>
            	</tr>
        	</thead>
        	<tbody>
            	<c:forEach var="m" items="${matriculas}">
				    <tr>
				        <td class="lista"><c:out value="${m.aluno.ra}" /></td>
				        <td class="lista"><c:out value="${m.aluno.nome}" /></td>
				        <c:forEach var="a" items="${avaliacoes}">
				            <td class="lista">
				                <c:set var="notaEncontrada" value="false" />
				                <c:forEach var="nota" items="${notas}">
				                    <c:if test="${nota.matricula.aluno.ra == m.aluno.ra && nota.avaliacao.codigo == a.codigo}">
				                        <c:out value="${nota.nota}" />
				                        <c:set var="notaEncontrada" value="true" />
				                    </c:if>
				                </c:forEach>
				                <c:if test="${!notaEncontrada}">
				                    Nota não lançada
				                </c:if>
				            </td>
				        </c:forEach>
				        <c:set var="situacaoExibida" value="false" />
				        <c:set var="notaExibida" value="false" />
			            <c:forEach var="nota" items="${notas}">
			                <c:if test="${nota.matricula.aluno.ra == m.aluno.ra && !situacaoExibida}">
			               	    <td class="lista"><c:out value="${nota.matricula.nota}" /></td>
			                    <td class="lista"><c:out value="${nota.matricula.situacao}" /></td>
			                    <c:set var="situacaoExibida" value="true" />
			                    <c:set var="notaExibida" value="true" />
			                </c:if>
			            </c:forEach>
			            <c:if test="${!notaExibida}">
		                    <td class="lista">Aluno sem notas</td>
		                </c:if>
			            <c:if test="${!situacaoExibida}">
		                    <td class="lista">Aluno sem notas</td>
		                </c:if>
				        <td>
				            <form class="lista" action="professorNotas" method="post">    
				                <input type="hidden" id="codigo_disciplina" name="codigo_disciplina" value="${m.disciplina.codigo}" />
				                <input type="hidden" id="aluno_ra" name="aluno_ra" value="${m.aluno.ra}" />
				                <input type="hidden" id="nome_aluno" name="nome_aluno" value="${m.aluno.nome}" />
				                <input type="hidden" id="ano_semestre" name="ano_semestre" value="${m.ano_semestre}" />
				                <input type="hidden" id="matricula" name="matricula" value="${param.matricula}" />
				                <input type="hidden" id="nome_disciplina" name="nome_disciplina" value="${m.disciplina.nome}" />
				                <input class="botao_lista" type="submit" name="botao" value="Lancar Notas">
				            </form>
				        </td>
				    </tr>
				</c:forEach>
       		 </tbody>
   		 </table>
   		 <form action="notasRelatorio" method = "post" target="_blank">
   		 	 <input type="hidden" id="codigo_disciplina" name="codigo_disciplina" value="${param.codigo_disciplina}" />
   		 	 <input class = "botao" type="submit" name="botao" value="Gerar Relatorio">
   		 </form>
		</c:if>
	</div>

</body>
</html>