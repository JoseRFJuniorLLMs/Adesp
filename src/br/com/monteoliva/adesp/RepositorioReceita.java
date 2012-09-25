package br.com.monteoliva.adesp;

//imports da API do JAVA
import java.util.ArrayList;
import java.util.List;

// imports da API do ANDROID
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RepositorioReceita {
	   private static final String CATEGORIA = "adesp";

	   // nome do banco e da tabela
	   private static final String NOME_BANCO   = "adesp";
	   public  static final String NOME_TABELA  = "receita";
	   public  static final String NOME_TABELA1 = "categoria";

	   // pega o SQLite
	   protected SQLiteDatabase dba;

	   // constructor
	   public RepositorioReceita(Context context) {
	      // abre o banco de dados
	      dba = context.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
	   }

	   // salva os dados, insere novo ou atualiza
	   public long salvar(TabelaReceita receita) {
	      long id = receita.id;
	      
	      // verifica o ID
	      if (id > 0) {
	         id = atualizar(receita);
	      }
	      else {
	         id = inserir(receita);
	      }
	      
	      // retorna o ID
	      return id;
	   }
	   
	   // Insere um nova Receita
	   public long inserir(TabelaReceita receita) {
	      // seta os valores
	      ContentValues valores = new ContentValues();
	      valores.put("id_categoria",receita.id_categoria);
	      valores.put("valor"       ,receita.valor       );
	      valores.put("mes_ano"     ,receita.mes_ano     );
	      valores.put("descricao"   ,receita.descricao   );
	      
	      // insere os dados na tabela
	      long id = dba.insert(NOME_TABELA, "", valores);
	   
	      // retorna o ID gerado
	      return id;
	   }
	   
	   // Atualiza uma receita no banco. O id eh utilizado.
	   public long atualizar(TabelaReceita receita) {
	      // seta os valores
	      ContentValues valores = new ContentValues();
	      valores.put("id_categoria",receita.id_categoria);
	      valores.put("valor"       ,receita.valor       );
	      valores.put("mes_ano"     ,receita.mes_ano     );
	      valores.put("descricao"   ,receita.descricao   );

	      // seta o WHERE
	      String where = "(_id=" + receita.id + ")";

	      // atualiza a tabela
	      long count = dba.update(NOME_TABELA, valores, where, null);
	      
	      // retorna o numero de linhas atualizadas
	      return count;
	   }
 
	   // Apaga um lancamento com o id fornecido
	   public int deletar(TabelaReceita receita) {
	      // seta o WHERE
	      String where = "(_id=" + receita.id + ")";

	      // atualiza a tabela
	      int count = dba.delete(NOME_TABELA, where, null);
	      
	      // retorna o numero de linhas atualizadas
	      return count;
	   }

	   // retorna um Cursos com a receita do mes
	   public Cursor getCursor(String mes_ano, long id_categoria) {
	      try {
	          // seta o where
	          String where = "(mes_ano = " + mes_ano + ")";
	          
	          // executa o SELECT
	          return dba.query(NOME_TABELA, TabelaReceita.colunas, where, null, null, null, null);
	      
	      } catch (SQLException e) {
	          Log.e(CATEGORIA,"Erro ao buscar a Receita do mes: " + e.toString());
	        return null;
	      }
	   }

	   // retorna um Cursos com os lancamentos
	   public Cursor getCursor(long id) {
	      try {
	    	  // seta o where
	          String where = "(_id = " + id + ")";
	      
	          // executa o SELECT
	          return dba.query(NOME_TABELA, TabelaReceita.colunas, where, null, null, null, null);
	      
	      } catch (SQLException e) {
	          Log.e(CATEGORIA,"Erro ao buscar a Receita selecionada: " + e.toString());
	        return null;
	      }
	   }

	   // retorna um Cursos com os lancamentos
	   public Cursor getCursor() {
	      try {
	          // executa o SELECT
	          return dba.query(NOME_TABELA, TabelaReceita.colunas, null, null, null, null, null);
	      
	      } catch (SQLException e) {
	          Log.e(CATEGORIA,"Erro ao buscar a Receita selecionada: " + e.toString());
	        return null;
	      }
	   }

	   // retorna um Cursos com os lancamentos
	   public Cursor getCursorCategoria(long id) {
	      try {
	    	  // seta as colunas
	    	  String[] colunas = new String[] { "descricao" };

	    	  // seta o where
	          String where = "(_id = " + id + ")";
	      
	          // executa o SELECT
	          return dba.query(NOME_TABELA1, colunas, where, null, null, null, null);
	      
	      } catch (SQLException e) {
	          Log.e(CATEGORIA,"Erro ao buscar a Categoria selecionada: " + e.toString());
	        return null;
	      }
	   }
 
	   // retorna o lancamento de um ID
	   public TabelaReceita editar(long id) {
	      // pega os dados
	      Cursor dados = getCursor(id);
	      
	      // instancia
	      TabelaReceita tabela = new TabelaReceita();

	      // verifica o total delinhas
	      if (dados.getCount() > 0) {
	         // move para o primeiro registro
	         dados.moveToFirst();

	         // recupera os atributos das colunas
	         tabela.id           = dados.getLong(0);
	         tabela.id_categoria = dados.getLong(1);
	         tabela.valor        = dados.getFloat(2);
	         tabela.mes_ano      = dados.getLong(3);
	         tabela.descricao    = dados.getString(4);
	      }
	      
	      // retorna a lista
	      return tabela;
	   }
 
	   // retorna o lancamento
	   public List<TabelaReceita> listarLancamento(String mes_ano, long id_categoria) {
	      // pega os dados
	      Cursor dados = getCursor(mes_ano, id_categoria);

	      // monta a lista
	      List<TabelaReceita> lista = new ArrayList<TabelaReceita>();

	      // verifica o total delinhas
	      if (dados.getCount() > 0) {
	         // move para o primeiro registro
	         dados.moveToFirst();
	         
	         // Loop
	         do {
	             // instancia
	             TabelaReceita tabela = new TabelaReceita();
	              
	             // adiciona na lista
	             lista.add(tabela);
	             
	             // recupera os atributos das colunas
	             tabela.id              = dados.getLong(0);
	             tabela.id_categoria    = dados.getLong(1);
	             tabela.valor           = dados.getFloat(2);
		         tabela.mes_ano         = dados.getLong(3);
	             tabela.descricao       = dados.getString(4);
	             tabela.categ_descricao = this.getCategoriaDescricao(tabela.id_categoria);
	         } while (dados.moveToNext());
	      }
	      
	      // retorna a lista
	      return lista;
	   }

	   public String getCategoriaDescricao(long id) {
		   // seta o retorno
		   String descricao = "";

	       // pega os dados
		   Cursor dados = getCursorCategoria(id);

		   // verifica o total delinhas
		   if (dados.getCount() > 0) {
		       // move para o primeiro registro
		       dados.moveToFirst();

		       // recupera os atributos das colunas
		       descricao = dados.getString(0);
		   }

		   // retornas
		   return descricao;
	   }
	   
	   // retorna o lancamento
	   public List<TabelaReceita> listarTudo() {
	      // pega os dados
	      Cursor dados = getCursor();

	      // monta a lista
	      List<TabelaReceita> lista = new ArrayList<TabelaReceita>();

	      // verifica o total delinhas
	      if (dados.getCount() > 0) {
	         // move para o primeiro registro
	         dados.moveToFirst();
	         
	         // Loop
	         do {
	             // instancia
	             TabelaReceita tabela = new TabelaReceita();
	              
	             // adiciona na lista
	             lista.add(tabela);
	             
	             // recupera os atributos das colunas
	             tabela.id           = dados.getLong(0);
	             tabela.id_categoria = dados.getLong(1);
	             tabela.valor        = dados.getFloat(2);
		         tabela.mes_ano      = dados.getLong(3);
	             tabela.descricao    = dados.getString(4);
	         } while (dados.moveToNext());
	      }
	      
	      // retorna a lista
	      return lista;
	   }

	   // fechar o banco de dados
	   public void fechar() {
	      if (dba != null) { dba.close(); }
	   }
}