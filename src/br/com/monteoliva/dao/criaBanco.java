package br.com.monteoliva.dao;

// imports da API do ANDROID
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class criaBanco {
    // seta o Banco de Dados
    protected SQLiteDatabase db;

	// Script para fazer drop na tabela
	private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS lancamento; DROP TABLE IF EXISTS receita; DROP TABLE IF EXISTS categoria; DROP TABLE IF EXISTS alarme;";

	// Cria a tabela com o "_id" sequencial
	private static final String[] SCRIPT_DATABASE_CREATE = new String[] {
		    "CREATE TABLE categoria    ( _id integer not null primary key autoincrement, descricao varchar(80), st_tipo smallint default '0');",
			"CREATE TABLE lancamento   ( _id integer not null primary key autoincrement, id_categoria integer not null default '0', descricao text default '', valor decimal(14,2) default '0.00', vencimento date default NULL, pago int(1) default '0'); ",
			"CREATE TABLE receita      ( _id integer not null primary key autoincrement, id_categoria integer not null default '0', valor decimal(14,2) default '0.00', mes_ano integer default '0', descricao text default '');",
			"CREATE TABLE alarme       ( _id integer not null primary key autoincrement, data date default NULL, notificacao smallint default '0');"};

	// Nome do banco
	private static final String NOME_BANCO = "adesp";

	// Controle de versao
	private static final int VERSAO_BANCO = 1;

	// Classe utilitaria para abrir, criar, e atualizar o banco de dados
	private SQLiteHelper dbHelper;

	// Cria o banco de dados com um script SQL
	public criaBanco(Context ctx) {
		// Criar utilizando um script SQL
		dbHelper = new SQLiteHelper(ctx, criaBanco.NOME_BANCO, criaBanco.VERSAO_BANCO, criaBanco.SCRIPT_DATABASE_CREATE, criaBanco.SCRIPT_DATABASE_DELETE);

		// abre o banco no modo escrita para poder alterar tambem
		db = dbHelper.getWritableDatabase();
	}

	// Fecha o banco
	public void fechar() {
		if (dbHelper != null) { dbHelper.close(); }
	}
}