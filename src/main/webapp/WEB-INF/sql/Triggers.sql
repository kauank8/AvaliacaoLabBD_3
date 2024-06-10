use AvLabBd
go

--Criando primeira trigger, assim que o aluno for cadastrado, ja se matricula na disciplina do 1º semestre
Create Trigger t_insereDisciplinasPrimeiroSemestre on Aluno
after insert 
as
Begin
	declare @ra char(9),
			@cod_curso int,
			@cod_disciplina int,
			@qtd_disciplinas int,
			@ano_atual varchar(4),
			@mes_atual varchar(2),
			@dia_semana varchar(25),
			@contador int

	set @ano_atual = (Select YEAR(GETDATE()))
	set @mes_atual = (Select MONTH(GETDATE()))
	set @ra = (select ra from inserted)
	set @contador = 1
	set @cod_curso = (select curso_codigo from inserted)

	If(@mes_atual > 6) Begin
		set @mes_atual = 2
	End
	Else Begin
		set @mes_atual = 1
	End

	create table #listaDisciplina(
		codigo int not null identity(1,1) Primary Key,
		codigo_disciplina int not null,
		dia_semana varchar(25) not null
	)
	Insert Into #listaDisciplina(codigo_disciplina, dia_semana)
	select codigo, dia_semana from Disciplina where codigo_curso = @cod_curso and semestre = 1

	set @qtd_disciplinas = (Select COUNT(codigo) from #listaDisciplina)

	while(@contador<=@qtd_disciplinas) Begin
		set @cod_disciplina = (select codigo_disciplina from #listaDisciplina where codigo = @contador)
		set @dia_semana = (select dia_semana from #listaDisciplina where codigo = @contador)

		Insert into Matricula Values
		(@ra, @cod_disciplina, @ano_atual+@mes_atual, @dia_semana, 0, 'Em Andamento', 0,0,0)

		set @contador = @contador +1
	End
End

--Segunda trigger, verifica se a dispensa do aluno foi aceita ou recusada, se aceita insire aprovado na matricula
GO
Create Trigger t_verificaDispensa on Dispensa
after update
as
Begin
	Declare @status varchar(20)

	set @status = (Select status_dispensa from inserted)

	if(@status = 'Aprovada') Begin 
		Declare @aluno_ra char(9),
				@codigo_disciplina int,
				@ano_semestre int,
				@dia_semana varchar(25),
				@consulta_matricula char(9)

		set @aluno_ra = (Select aluno_ra from inserted)
		set @codigo_disciplina = (Select codigo_disciplina from inserted)
		set @ano_semestre = (Select dbo.fn_turma())
		set @dia_semana = (Select dia_semana from Disciplina where codigo = @codigo_disciplina)
		set @consulta_matricula = (Select aluno_ra from Matricula where codigo_disciplina = @codigo_disciplina 
		and aluno_ra = @aluno_ra and ano_semestre = @ano_semestre)
		If(@consulta_matricula is not null) Begin
			Update Matricula set nota = 'D', situacao = 'Aprovado' where codigo_disciplina = @codigo_disciplina 
			and aluno_ra = @aluno_ra
		End
		Else Begin
			Insert into Matricula Values
			(@aluno_ra, @codigo_disciplina, @ano_semestre, @dia_semana, 'D', 'Aprovado',0,0,0)
		End
	End
End

-- criando uma triggers que ja registra quantas faltas e presencas o aluno tem assim que uma chamada é feita
GO
Create Trigger t_faltas_presencas on Chamada
after insert, update
as
Begin
	Declare @consulta_ra char(9),
			@ra char(9),
			@codigo_disciplina int,
			@semestre int,
			@primeira_aula int,
			@segunda_aula int,
			@terceira_aula int,
			@quarta_aula int,
			@qtd_presenca int,
			@qtd_falta int

	set @consulta_ra = (Select aluno_ra from deleted)
	set @ra = (Select aluno_ra from inserted)
	set @codigo_disciplina = (Select codigo_disciplina from inserted)
	set @semestre = (Select semestre from inserted)

	if(@consulta_ra is not null) Begin
		Declare @del_primeira_aula int,
				@del_segunda_aula int,
				@del_terceira_aula int,
				@del_quarta_aula int,
				@del_presenca int,
				@del_falta int,
				@soma_presenca int,
				@soma_falta int


		set @primeira_aula = (Select presenca_primeira_aula from inserted)
		set @segunda_aula = (Select presenca_segunda_aula from inserted)
		set @terceira_aula = (Select presenca_terceira_aula from inserted)
		set @quarta_aula = (Select presenca_quarta_aula from inserted)

		set @del_primeira_aula = (Select presenca_primeira_aula from deleted)
		set @del_segunda_aula = (Select presenca_segunda_aula from deleted)
		set @del_terceira_aula = (Select presenca_terceira_aula from deleted)
		set @del_quarta_aula = (Select presenca_quarta_aula from deleted)

		if(@terceira_aula is not null) Begin
			set @qtd_presenca = @primeira_aula + @segunda_aula + @terceira_aula + @quarta_aula
			set @qtd_falta = 4 - @qtd_presenca

			set @del_presenca = @del_primeira_aula + @del_segunda_aula + @del_terceira_aula + @del_quarta_aula
			set @del_falta = 4 - @del_presenca

			set @soma_presenca = @qtd_presenca - @del_presenca
			set @soma_falta = @qtd_falta - @del_falta
		End
		Else Begin
			set @qtd_presenca = @primeira_aula + @segunda_aula 
			set @qtd_falta = 2 - @qtd_presenca

			set @del_presenca = @del_primeira_aula + @del_segunda_aula
			set @del_falta = 2 - @del_presenca

			set @soma_presenca = @qtd_presenca - @del_presenca
			set @soma_falta = @qtd_falta - @del_falta
		End

		Update Matricula set quantidade_presenca = quantidade_presenca + @soma_presenca, quantidade_falta = quantidade_falta + @soma_falta
		where aluno_ra = @ra and codigo_disciplina = @codigo_disciplina and ano_semestre = @semestre
	End


	Else Begin
		set @primeira_aula = (Select presenca_primeira_aula from inserted)
		set @segunda_aula = (Select presenca_segunda_aula from inserted)
		set @terceira_aula = (Select presenca_terceira_aula from inserted)
		set @quarta_aula = (Select presenca_quarta_aula from inserted)

		if(@terceira_aula is not null) Begin
			set @qtd_presenca = @primeira_aula + @segunda_aula + @terceira_aula + @quarta_aula
			set @qtd_falta = 4 - @qtd_presenca

		End
		Else Begin
			set @qtd_presenca = @primeira_aula + @segunda_aula
			set @qtd_falta = 2 - @qtd_presenca
		End

		Update Matricula set quantidade_presenca = quantidade_presenca + @qtd_presenca, quantidade_falta = quantidade_falta + @qtd_falta 
		where aluno_ra = @ra and codigo_disciplina = @codigo_disciplina and ano_semestre = @semestre
	End
End

-- Trigger que controla insercao e atuliazacao da media final do Aluno
Go
Create trigger t_notas on Notas
after insert, update
as
Begin
	Declare @codigo_nota int, 
			@nota float,
			@cod_avaliacao int,
			@peso float,
			@aluno_ra varchar(9),
			@cod_disciplina  int,
			@ano_semestre varchar(10),
			@nota_peso float
	
	set @codigo_nota = (Select codigo from deleted)
	set @cod_avaliacao = (Select codigo_avaliacao from inserted)
	set @peso = (Select peso from Avaliacao where codigo = @cod_avaliacao)
	set @aluno_ra = (Select aluno_ra from inserted)
	set @cod_disciplina = (Select codigo_disciplina from inserted)
	set @ano_semestre = (Select dbo.fn_turma())
	set @nota = (Select nota from inserted)
	set @nota_peso = (@nota * @peso)
	--Insert
	If(@codigo_nota is null) Begin
		Update Matricula set nota = @nota_peso + nota where aluno_ra = @aluno_ra 
		and codigo_disciplina = @cod_disciplina and ano_semestre = @ano_semestre
	End
	Else Begin
		Declare @nota_del float,
				@nota_peso_del float
		
		set @nota_del = (Select nota from deleted)
		set @nota_peso_del = (@nota_del * @peso) 

		Update Matricula set nota = (nota - @nota_peso_del) + @nota_peso where aluno_ra = @aluno_ra 
		and codigo_disciplina = @cod_disciplina and ano_semestre = @ano_semestre
	End
End

Go
--Criando trigger que controla inserção de pesos nas avaliações da mesma disciplina
Create trigger t_peso_av on Avaliacao
after insert, update
as
Begin
	Declare @codigo int,
			@codigo_disciplina int,
			@soma_peso decimal(5,2)

	set @codigo = (Select codigo from deleted)
	set @codigo_disciplina = (Select codigo_disciplina from inserted)
	set @soma_peso = 0
	--Update
	If(@codigo is not null) Begin
		set @soma_peso = (Select Sum(peso) from Avaliacao where codigo_disciplina = @codigo_disciplina)
		if(@soma_peso > 1) Begin
			ROLLBACK TRANSACTION
			RAISERROR('A soma dos pesos das avaliações não pode ser maior que 1', 16, 1)
		End
	End
	--Insert
	Else Begin
		set @soma_peso = (Select Sum(peso) from Avaliacao where codigo_disciplina = @codigo_disciplina)
		if(@soma_peso > 1) Begin
			ROLLBACK TRANSACTION
			RAISERROR('A soma dos pesos das avaliações não pode ser maior que 1', 16, 1)
		End
	End
End

Go
--Criando trigger que controla inserção de datas nas avaliações da mesma disciplina
Create trigger t_data_av on Avaliacao
after insert, update
as
Begin
	Declare @codigo int,
			@codigo_disciplina int,
			@data_av date,
			@verifica_data date

	set @codigo = (Select codigo from deleted)
	set @codigo_disciplina = (Select codigo_disciplina from inserted)
	set @data_av = (Select data_avaliacao from inserted )
	--Update
	If(@codigo is not null) Begin
		set @verifica_data = (Select data_avaliacao from Avaliacao where data_avaliacao = @data_av and codigo_disciplina = @codigo_disciplina and codigo != @codigo)
		if(@verifica_data = @data_av) Begin
			ROLLBACK TRANSACTION
			RAISERROR('Ja existe uma prova nessa data, para essa disciplina', 16, 1)
		End
	End
	--Insert
	Else Begin
		set @codigo = (Select codigo from inserted)
		set @verifica_data = (Select data_avaliacao from Avaliacao where data_avaliacao = @data_av and codigo_disciplina = @codigo_disciplina and codigo != @codigo)
		if(@verifica_data = @data_av) Begin
			ROLLBACK TRANSACTION
			RAISERROR('Ja existe uma prova nessa data, para essa disciplina', 16, 1)
		End
	End
End
