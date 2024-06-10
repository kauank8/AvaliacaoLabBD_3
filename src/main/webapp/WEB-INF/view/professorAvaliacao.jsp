<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href='<c:url value = "./resources/professorAvaliacao.css"/>'>
<title>Area Avaliacao</title>
</head>
<body>
	<nav id = menu>
		<ul>
			<li><a href="index">Home</a>
			<li><a href="professorChamada" >Chamada</a>
			<li><a href="professorAvaliacao" class="ativa">Avaliacao</a>
			<li><a href="professorNotas" >Notas</a>
			<li><a href="professorFalta">Faltas</a>
		</ul>
	</nav>
	
	<div class="container_aluno">
		<h1>Aréa de Avaliacao</h1>
		<form action="professorAvaliacao" method="post" >
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
                       			 	<form class="lista" action="professorAvaliacao" method="post">
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
		<c:if test="${not empty avaliacao}">
			<h1>Avaliacao de: ${avaliacao.disciplina.nome} </h1>
			<form action="professorAvaliacao" method="post">
				<input type="hidden" id="codigo_disciplina" name="codigo_disciplina" value="${avaliacao.disciplina.codigo}" />
				<input type="hidden" id="codigo_avaliacao" name="codigo_avaliacao" value="${avaliacao.codigo}" />
				<input type="hidden" id="matricula" name="matricula" value="${param.matricula}" />
			<table>
				<tr>
					<td colspan="2">
						<input class="input_data" type="text"  id="tipo" name="tipo" placeholder="Tipo" 
						 value='<c:out value="${avaliacao.tipo }"></c:out>'> 
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<input class="input_data" type="text"  id=peso name="peso" placeholder="Peso" 
						pattern="[0-9,.]*" title="Por favor, digite apenas números."
						 value='<c:out value="${avaliacao.peso }"></c:out>'> 
					</td>
				</tr>
				<tr>
					<td colspan="1">
					<label class="label_data"> Data Prevista:</label>
					</td>
					<td colspan="1">
						<input class="input_data" type="date"  id=data_avaliacao name="data_avaliacao"  
						 value='<c:out value="${avaliacao.data_avaliacao }"></c:out>'> 
					</td>
				</tr>
				<tr>
					<td>
						<input class="botao" type="submit" id="botao" name="botao" value="Cadastrar">
					</td>
					<td>
						<input class="botao" type="submit" id="botao" name="botao" value="Atualizar">
					</td>
				</tr>
			</table>
			</form>
			<br/>
			<br/>
   		</c:if>
   	</div>
   	
   	<div align="center">
		<c:if test="${not empty avaliacoes}">
		<h1>Metodologias Ativas </h1>
   		<table class="table_round">
       		<thead>
            	<tr>
            		<th class="lista">Tipo</th>
            		<th class="lista">Peso</th>
            		<th class="lista">Data Prevista</th>
            		<th class="lista"></th>
            	</tr>
        	</thead>
        	<tbody>
            	<c:forEach var="a" items="${avaliacoes}">
                       	 	<tr>
                       	 		<td class="lista"><c:out value="${a.tipo}" /></td>
                       	 		<td class="lista"><c:out value="${a.peso}" /></td>
                       	 		<td class="lista"><c:out value="${a.data_avaliacao}" /></td>
                       			<td>
                       			 	<form class="lista" action="professorAvaliacao" method="post">
                            	 		<input type="hidden" id="codigo_avaliacao" name="codigo_avaliacao" value="${a.codigo}" />
                            	 		<input type="hidden" id="tipo" name="tipo" value="${a.tipo}" />
                            	 		<input type="hidden" id="peso" name="peso" value="${a.peso}" />
                            	 		<input type="hidden" id="data_avaliacao" name="data_avaliacao" value="${a.data_avaliacao}" />
                            	 		<input type="hidden" id="matricula" name="matricula" value="${param.matricula}" />
                            	 		<input type="hidden" id="codigo_disciplina" name="codigo_disciplina" value="${a.disciplina.codigo}" />
                                		<input class="botao_lista" type="submit" name="botao" value="Editar">
                            		</form>
                            	</td>
                        	</tr>
           	 	</c:forEach>
       		 </tbody>
   		 </table>
   		</c:if>
   	</div>
</body>
</html>