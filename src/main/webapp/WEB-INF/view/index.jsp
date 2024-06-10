<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href='<c:url value = "./resources/style1.css"/>'>
<title>Sistema Agis</title>
</head>
<body>
	<nav id = menu>
	<ul>
		<li><a href="index">Home</a>
		<li><a href="aluno">Aluno</a>
		<li><a href="secretaria">Secretaria</a>
		<li><a href="professorChamada">Professor</a>
	</ul>
	</nav>
	<div id="principal">
		<div id="logo">
			<h1>
				<span class="blue">Sistema</span> AGIS
			</h1>
		</div>
		<div id="conteudo">
			<p>Bem-vindo ao Sistema AGIS, uma solu��o inovadora projetada
				para simplificar e aprimorar o gerenciamento de universidades de
				forma eficiente e eficaz. O AGIS, que significa "Administra��o e
				Gest�o Integrada de Sistemas", foi desenvolvido com o objetivo de
				fornecer uma plataforma abrangente e intuitiva para facilitar as
				opera��es administrativas, acad�micas e financeiras das institui��es
				de ensino superior.</p>
				<p> Com o AGIS, as universidades podem gerenciar uma
				ampla gama de processos de maneira centralizada, garantindo uma
				experi�ncia integrada para administradores, professores,
				funcion�rios e alunos. Al�m disso, o sistema oferece uma s�rie de
				recursos avan�ados para melhorar a efici�ncia operacional e promover
				o sucesso acad�mico dos estudantes.</p>
				<p> <Strong>Principais Funcionalidade</Strong></p>
				<p><Strong>1. Gest�o de Alunos:</Strong> Permite cadastrar, alterar dados de um aluno </p>
				<p><Strong>2. Gest�o de Matricula:</Strong> Permite aos alunos, realizarem suas maticulas em determinada disciplina, manipulando seus horario </p>
				<p><Strong>3. Gest�o de Disciplina:</Strong> Permite cadastrar, alterar dados de uma disciplina </p>
				<p><Strong>4. Gest�o de Curso:</Strong> Permite cadastrar, alterar dados de um Curso </p>
				<p><Strong>5. Area do aluno:</Strong> Permite aos alunos, visualizar e consultar suas informa��es como as discplinas aprovadas, e as em andamento </p>
		</div>
	</div>
	<div id="rodape">
		<p> Desenvolvido por: Kauan Paulino Farias. &copy Todos os Direitos reservados</p>
		<p>Contato para suporte: kauan.farias01@fatecsp.gov.br</p>
	</div>
</body>
</html>