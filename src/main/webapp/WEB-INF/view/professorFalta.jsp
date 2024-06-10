<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href='<c:url value = "./resources/professorAvaliacao.css"/>'>
<title>Area de faltas</title>
</head>
<body>
	<nav id = menu>
	<ul>
		<li><a href="index">Home</a>
		<li><a href="professorChamada">Chamada</a>
		<li><a href="professorAvaliacao">Avaliacao</a>
		<li><a href="professorNotas" >Notas</a>
		<li><a href="professorFalta" class="ativa">Faltas</a>
	</ul>
	</nav>
	<div class="container_aluno">
		<h1>Consultar Faltas</h1>
		<form action="professorFalta" method="post" >
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
                       			 	<form class="lista" action="professorFalta" method="post">
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
   	
   	<div align="center">
		<c:if test="${not empty chamadas }">
		<h1>Disciplina: ${nome_disciplina.nome}</h1>
   		<table class="table_round">
    
        	<tbody>
            	 <c:set var="lastRa" value="" />
                 <c:set var="currentStatus" value="" />
                <c:forEach var="c" items="${chamadas}" varStatus="status">
                    <c:if test="${lastRa != c[0] && lastRa != ''}">
                        <tr>
                            <td colspan="3" class="status-message">
                                <c:out value="${currentStatus}" />
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${lastRa != c[0]}">
                        <tr>
                            <td colspan="3" class="title">
                                Aluno Ra: <c:out value="${c[0]}" /> - Nome: <c:out value="${c[1]}" />
                            </td>
                        </tr>
                        <tr>
                            <th class="lista">Data da Aula</th>
                            <th class="lista">Faltas na Semana</th>
                            <th class="lista">Total de Faltas</th>
                        </tr>
                        <c:set var="lastRa" value="${c[0]}" />
                        <c:set var="currentStatus" value="${c[5]}" />
                    </c:if>
                    <tr>
                        <td class="lista"><c:out value="${c[2]}" /></td>
                        <td class="lista"><c:out value="${c[3]}" /></td>
                        <td class="lista"><c:out value="${c[4]}" /></td>
                    </tr>
                    <c:if test="${status.last}">
                        <tr>
                            <td colspan="3" class="status-message">
                                <c:out value="${c[5]}" />
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
       		 </tbody>
   		 </table>
   		 <form action="faltaRelatorio" method = "post" target="_blank">
   		 	 <input type="hidden" id="codigo_disciplina" name="codigo_disciplina" value="${nome_disciplina.codigo}" />
   		 	 <input type="hidden" id="nome_disciplina" name="nome_disciplina" value="${nome_disciplina.nome}" />
   		 	 <input class = "botao" type="submit" name="botao" value="Gerar Relatorio">
   		 </form>
   		</c:if>
   	</div>
	
	

	<div id="rodape">
		<p> Desenvolvido por: Kauan Paulino Farias. &copy Todos os Direitos reservados</p>
		<p>Contato para suporte: kauan.farias01@fatecsp.gov.br</p>
	</div>
	
</body>
</html>