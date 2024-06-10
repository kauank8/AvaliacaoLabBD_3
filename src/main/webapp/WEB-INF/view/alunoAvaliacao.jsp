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
		<li><a href="aluno" > Área Aluno</a>
		<li><a href="alunoMatricula"  >Matricular</a>
		<li><a href="dispensaAluno">Dispensas</a>
		<li><a href="alunoAvaliacao" class="ativa">Avaliacoes</a>
	</ul>
	</nav>
	<div class="container_aluno">
		<h1>Consultar Avaliacao</h1>
		<form action="alunoAvaliacao" method="post" >
		<table>
				<tr>
					<td colspan="2">
						<input class="id_input_data" type="text"  id="ra" name="ra" placeholder="Ra" 
						pattern="[0-9]*" title="Por favor, digite apenas números."
						 value='<c:out value="${aluno.ra }"></c:out>'> 
					</td>
				</tr>
				<tr>
					<td>
						<input class="botao" type="submit" id="botao" name="botao" value="Listar Avaliacoes">
						<input class="botao" type="submit" id="botao" name="botao" value="Listar Notas">
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
	
	
	<div align="center" >
	<c:if test="${not empty avaliacoes }">
		<h1>Avaliacões Marcadas</h1>
   		<table class="table_round">
       		<thead>
            	<tr>
                	<th class="lista">Disciplina</th>
                	<th class="lista">Datas </th>
                	<th class="lista">Pesos</th>
            	</tr>
        	</thead>
        	<tbody>
            	<c:forEach var="a" items="${avaliacoes}">
                    <tr>
                        <td class="lista"><c:out value="${a[0]}" /></td> 
                        <td class="lista"><c:out value="${a[2].replace('; ', '<br/>')}" escapeXml="false" /></td>
                        <td class="lista"><c:out value="${a[3].replace('; ', '<br/>')}" escapeXml="false" /></td> 
                    </tr>
                </c:forEach>
       		 </tbody>
   		 </table>
   		 <p> Disciplina matriculadas que não são exibidas, estão sem data prevista, entre em contato com o professor </p>
	</c:if>
	</div>
	
	<div align="center" >
	<c:if test="${not empty notas }">
		<h1>Notas ${aluno.nome }</h1>
   		<table class="table_round">
       		<thead>
            	<tr>
                	<th class="lista">Disciplina</th>
                	<th class="lista">Notas </th>
                	<th class="lista">Média</th>
                	<th class="lista">Situação Atual</th>
            	</tr>
        	</thead>
        	<tbody>
            	<c:forEach var="n" items="${notas}">
                    <tr>
                        <td class="lista"><c:out value="${n[0]}" /></td> 
                        <td class="lista"> <c:out value="${n[4].replace('; ', '<br/>')}" escapeXml="false" /></td>
                         <td class="lista"><c:out value="${n[2]}" /></td> 
                         <td class="lista">
                          <c:out value="${n[3]}" />
                         </td>
                    </tr>
                </c:forEach>
       		 </tbody>
   		 </table>
   		 <p> Disciplina matriculadas que não são exibidas, estão sem notas, entre em contato com o professor </p>
	</c:if>
	</div>

	<div id="rodape">
		<p> Desenvolvido por: Kauan Paulino Farias. &copy Todos os Direitos reservados</p>
		<p>Contato para suporte: kauan.farias01@fatecsp.gov.br</p>
	</div>

</body>
</html>