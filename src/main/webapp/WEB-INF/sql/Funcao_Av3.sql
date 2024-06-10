Use AvLabBd
go
-- Criações de UDF e Procedures Para avaliação 3;

-- Procedure que controla cadastro de avaliações
Create Procedure sp_insereAvaliacao(@tipo varchar(30), @peso decimal(7,2), @data_avaliacao varchar(20), @codigo_disciplina int, @saida varchar(100) output)
as
	Declare @consultaAv int,
			@qtdAv int,
			@ano_semestre varchar(10)
	
	set @consultaAv = (Select codigo from Avaliacao where tipo = @tipo and peso = @peso and data_avaliacao = @data_avaliacao and 
	codigo_disciplina = @codigo_disciplina)

	If(@consultaAv is null) Begin
		set @qtdAv = (Select Count(codigo) from Avaliacao where codigo_disciplina = @codigo_disciplina) + 1

		If(@qtdAv<=3) Begin
			set @ano_semestre = (Select dbo.fn_turma())
			Begin try
				Insert into Avaliacao(tipo, peso, data_avaliacao, codigo_disciplina, ano_semestre) 
				Values(@tipo, @peso, @data_avaliacao, @codigo_disciplina, @ano_semestre)
				set @saida = 'Avaliacao cadastrada com sucesso'
			End Try
			Begin Catch
				set @saida = ERROR_MESSAGE()
			End catch
		End
		Else Begin
			set @saida = 'So e possivel cadastrar no maximo 3 metodos avaliativos'
		End

	End
	Else Begin
		set @saida = 'Ja existe um metodo cadastrado nesses moldes, para atualizar chame a operacao devida'
	End

Go
--Procedure que controla a inserção de nota e atualizacao da mesma
Create Procedure sp_iuNotas(@acao varchar(1), @aluno_ra varchar(9), @codigo_disciplina int, 
@nota decimal(7,2), @codigo_avaliacao int, @ano_semestre varchar(10), @saida varchar(100) output)
as

	If(UPPER(@acao) = 'I') Begin
		Declare @cons_nota int
		
		set @cons_nota = (Select codigo from Notas where aluno_ra = @aluno_ra and codigo_avaliacao = @codigo_avaliacao 
		and codigo_disciplina = @codigo_disciplina and ano_semestre = @ano_semestre)

		If(@cons_nota is null) Begin
			Begin Try
				Insert Into Notas (aluno_ra, codigo_disciplina, ano_semestre, codigo_avaliacao, nota) 
				Values(@aluno_ra, @codigo_disciplina, @ano_semestre, @codigo_avaliacao, @nota)
				set @saida = 'Nota lancada com sucesso'
			End Try
			Begin Catch
				set @saida = ERROR_MESSAGE()
			End Catch
		End
		Else Begin
			set @saida = 'Essa nota ja esta cadastrada, se deseja atualizar chame a operacao devida'
		End
	End

	If(UPPER(@acao) = 'U') Begin
		Begin Try
			Update Notas set nota = @nota where codigo_disciplina = @codigo_disciplina and aluno_ra = @aluno_ra 
			and ano_semestre = @ano_semestre and codigo_avaliacao = @codigo_avaliacao
			set @saida = 'Nota atualizado com sucesso'
		End Try
		Begin Catch
				set @saida = ERROR_MESSAGE()
		End Catch
	End



--Function que lista chamada de alunos por semana e total
Go
Create function fn_lista_chamada(@codigo_disciplina int)
returns @tabela Table(
ra_aluno char(9),
nome_aluno varchar(150),
data_aula date,
faltas_semanais int,
total_faltas int,
status_falta varchar(50)
)
Begin
	Declare @turma varchar(10),
			@ra_aluno varchar(9),
			@nome_aluno varchar(170),
			@codigo_disc int,
			@primeira_aula int,
			@segunda_aula int,
			@terceira_aula int,
			@quarta_aula int,
			@data_aula date,
			@falta_semanais int,
			@total_faltas int,
			@frequencia decimal(5,2),
			@total_aulas int,
			@situacao varchar(50)
	
	DECLARE c CURSOR
		FOR Select c.*, a.nome from Chamada c, Aluno a, Matricula m 
			where c.codigo_disciplina = @codigo_disciplina and c.semestre = (Select dbo.fn_turma())
			and c.aluno_ra = a.ra and c.aluno_ra = m.aluno_ra
			and c.semestre = m.ano_semestre and m.codigo_disciplina = c.codigo_disciplina
			and m.situacao = 'Em andamento'
			order by c.aluno_ra, c.data_aula asc
	OPEN c
	FETCH NEXT FROM c INTO @primeira_aula, @segunda_aula, @terceira_aula, @quarta_aula, @data_aula, @codigo_disc, @ra_aluno, @turma, @nome_aluno
	WHILE @@FETCH_STATUS = 0 
	Begin
		Declare @cont int
		set @cont=0
		If(@primeira_aula = 0) Begin
			set @cont = @cont + 1
		End
		If(@segunda_aula = 0) Begin
			set @cont = @cont + 1
		End
		If(@terceira_aula is not null and @terceira_aula = 0) Begin
			set @cont = @cont + 1
		End
		If(@quarta_aula is not null and @terceira_aula = 0) Begin
			set @cont = @cont + 1
		End

		set @falta_semanais = @cont
		set @total_faltas = (Select Sum(faltas_semanais) from @tabela where ra_aluno = @ra_aluno)
		If(@total_faltas is null) Begin
			set @total_faltas = @cont
		End
		Else begin
			set @total_faltas = @total_faltas + @cont
		End


		set @total_aulas = (Select quantidade_presenca from Matricula where aluno_ra = @ra_aluno and ano_semestre = @turma and codigo_disciplina = @codigo_disc and situacao = 'Em Andamento') +
							(Select quantidade_falta from Matricula where aluno_ra = @ra_aluno and ano_semestre = @turma and codigo_disciplina = @codigo_disc and situacao = 'Em Andamento')


		set @frequencia =(Select quantidade_presenca from Matricula where aluno_ra = @ra_aluno and ano_semestre = @turma and codigo_disciplina = @codigo_disc and situacao = 'Em Andamento') 
		set @frequencia = @frequencia / @total_aulas			 
		set @frequencia = @frequencia * 100

		if(@frequencia < 75) Begin
			set @situacao = 'Reprovado pela frequência inferior a 75%'
		End
		Else Begin 
			set @situacao = 'Aprovado pela frequência superior a 75%'
		End

		Insert into @tabela values(@ra_aluno, @nome_aluno, @data_aula, @falta_semanais, @total_faltas, @situacao)

		FETCH NEXT FROM c INTO @primeira_aula, @segunda_aula, @terceira_aula, @quarta_aula, @data_aula, @codigo_disc, @ra_aluno, @turma, @nome_aluno
	End
	CLOSE c
	DEALLOCATE c
	Return
End

--Função que lista chamada para relatorio adaptada
Go
Create function fn_lista_chamada_relatorio(@codigo_disciplina int)
returns @tabela Table(
ra_aluno char(9),
nome_aluno varchar(150),
data_aula date,
faltas_semanais int,
total_faltas int,
status_falta varchar(50)
)
Begin
	Declare @turma varchar(10),
			@ra_aluno varchar(9),
			@nome_aluno varchar(170),
			@codigo_disc int,
			@primeira_aula int,
			@segunda_aula int,
			@terceira_aula int,
			@quarta_aula int,
			@data_aula date,
			@falta_semanais int,
			@total_faltas int,
			@frequencia decimal(5,2),
			@total_aulas int,
			@situacao varchar(50)
	
	DECLARE c CURSOR
		FOR Select c.*, a.nome from Chamada c, Aluno a, Matricula m 
			where c.codigo_disciplina = @codigo_disciplina and c.semestre = (Select dbo.fn_turma())
			and c.aluno_ra = a.ra and c.aluno_ra = m.aluno_ra
			and c.semestre = m.ano_semestre and m.codigo_disciplina = c.codigo_disciplina
			and m.situacao = 'Em andamento'
			order by c.aluno_ra, c.data_aula asc
	OPEN c
	FETCH NEXT FROM c INTO @primeira_aula, @segunda_aula, @terceira_aula, @quarta_aula, @data_aula, @codigo_disc, @ra_aluno, @turma, @nome_aluno
	WHILE @@FETCH_STATUS = 0 
	Begin
		Declare @cont int
		set @cont=0
		If(@primeira_aula = 0) Begin
			set @cont = @cont + 1
		End
		If(@segunda_aula = 0) Begin
			set @cont = @cont + 1
		End
		If(@terceira_aula is not null and @terceira_aula = 0) Begin
			set @cont = @cont + 1
		End
		If(@quarta_aula is not null and @terceira_aula = 0) Begin
			set @cont = @cont + 1
		End

		set @falta_semanais = @cont
		set @total_faltas = (Select Sum(faltas_semanais) from @tabela where ra_aluno = @ra_aluno)
		If(@total_faltas is null) Begin
			set @total_faltas = @cont
		End
		Else begin
			set @total_faltas = @total_faltas + @cont
		End


		set @total_aulas = (Select quantidade_presenca from Matricula where aluno_ra = @ra_aluno and ano_semestre = @turma and codigo_disciplina = @codigo_disc and situacao = 'Em Andamento') +
							(Select quantidade_falta from Matricula where aluno_ra = @ra_aluno and ano_semestre = @turma and codigo_disciplina = @codigo_disc and situacao = 'Em Andamento')


		set @frequencia =(Select quantidade_presenca from Matricula where aluno_ra = @ra_aluno and ano_semestre = @turma and codigo_disciplina = @codigo_disc and situacao = 'Em Andamento') 
		set @frequencia = @frequencia / @total_aulas			 
		set @frequencia = @frequencia * 100

		if(@frequencia < 75) Begin
			set @situacao = 'Reprovado'
		End
		Else Begin 
			set @situacao = 'Aprovado'
		End

		Insert into @tabela values(@ra_aluno, @nome_aluno, @data_aula, @falta_semanais, @total_faltas, @situacao)

		FETCH NEXT FROM c INTO @primeira_aula, @segunda_aula, @terceira_aula, @quarta_aula, @data_aula, @codigo_disc, @ra_aluno, @turma, @nome_aluno
	End
	CLOSE c
	DEALLOCATE c
	Return
End
