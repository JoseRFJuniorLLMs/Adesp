package br.com.monteoliva.adesp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RepositorioCategoria {
       private static final String CATEGORIA = "adesp";

       // nome do banco e da tabela
	   private static final String NOME_BANCO   = "adesp";
	   public  static final String NOME_TABELA  = "categoria";
	   public  static final String NOME_TABELA1 = "lancamento";
	   public  static final String NOME_TABELA2 = "receita";

	   // pega o SQLite
	   protected static SQLiteDatabase dba;
	   
	   // pega o context
	   Context contexto;

	   // constructor
	   public RepositorioCategoria(Context context) {
	      // abre o banco de dados
	      dba = context.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
	      
	      // seta o contexto
	      this.contexto = context;
	   }

	   // salva os dados, insere novo ou atualiza
	   public long salvar(TabelaCategoria categoria) {
	      long id = categoria.id;
	      
	      // verifica o ID
	      if (id != 0) {
	         atualizar(categoria);
	      }
	      else {
	         id = inserir(categoria);
	      }
	      
	      // retorna o ID
	      return id;
	   }

	   // Insere um novo lancamento
	   public long inserir(TabelaCategoria categoria) {
	      // seta os valores
	      ContentValues valores = new ContentValues();
	      valores.put("descricao",categoria.descricao);
	      valores.put("st_tipo"  ,categoria.st_tipo  );
	      
	      // insere os dados na tabela
	      long id = dba.insert(NOME_TABELA, "", valores);
	   
	      // retorna o ID gerado
	      return id;
	   }
	   
	   // Atualiza um lancamento no banco. O id eh utilizado.
	   public int atualizar(TabelaCategoria categoria) {
	      // seta os valores
	      ContentValues valores = new ContentValues();
	      valores.put("descricao",categoria.descricao);
	      valores.put("st_tipo"  ,categoria.st_tipo  );

	      // seta o WHERE
	      String where = "(_id=" + categoria.id + ")";

	      // atualiza a tabela
	      int count = dba.update(NOME_TABELA, valores, where, null);
	      
	      // retorna o numero de linhas atualizadas
	      return count;
	   }

	   // Apaga um lancamento com o id fornecido
	   public int deletar(TabelaCategoria categoria) {
	      // seta o WHERE
	      String where  = "(_id=" + categoria.id + ")";
	      String where1 = "(id_categoria=" + categoria.id + ")";

	      // exclui os dados do Lancamento e da Receita
	      dba.delete(NOME_TABELA1, where1, null);    // Lancamento
	      dba.delete(NOME_TABELA2, where1, null);    // Receita

	      // exclui os dados da tabela
	      int count = dba.delete(NOME_TABELA, where, null);
	      
	      // retorna o numero de linhas atualizadas
	      return count;
	   }

	   // retorna um Cursos com os lancamentos
	   public Cursor getCursor() {
	      try {
	          // seta o Order By
	          String orderBy = "st_tipo ASC, descricao ASC";
	          
	          // executa o SELECT
	          return dba.query(NOME_TABELA, TabelaCategoria.colunas, null, null, null, null, orderBy);
	      
	      } catch (SQLException e) {
	          Log.e(CATEGORIA,"Erro ao buscar as Categorias: " + e.toString());
	        return null;
	      }
	   }

	   // retorna um Cursos com os lancamentos
	   public Cursor getCursorCombo(int onde) {
	      try {
	          // seta o Order By
	          String orderBy = "descricao ASC";

	          // monta o where
	          String where = (onde == 2 || onde == 3) ? "(st_tipo = 1)" : "(st_tipo = 0)";
	          
	          // executa o SELECT
	          return dba.query(NOME_TABELA, TabelaCategoria.colunas, where, null, null, null, orderBy);
	      
	      } catch (SQLException e) {
	          Log.e(CATEGORIA,"Erro ao buscar as Categorias: " + e.toString());
	        return null;
	      }
	   }

	   // retorna um Cursos com os lancamentos
	   public Cursor getCursorTotal() {
	      try {
	          // seta o Order By
	          String orderBy = "st_tipo ASC, descricao ASC";
	          
	          // executa o SELECT
	          return dba.query(NOME_TABELA, TabelaCategoria.colunas, null, null, null, null, orderBy);
	      
	      } catch (SQLException e) {
	          Log.e(CATEGORIA,"Erro ao buscar as Categorias: " + e.toString());
	        return null;
	      }
	   }

	   // retorna um Cursos com os lancamentos
	   public Cursor getCursor(long id) {
	      try {
	          // seta o where
	          String where = "(_id = " + id + ")";
	      
	          // executa o SELECT
	          return dba.query(NOME_TABELA, TabelaCategoria.colunas, where, null, null, null, null);
	      
	      } catch (SQLException e) {
	          Log.e(CATEGORIA,"Erro ao buscar a Categoria selecionado: " + e.toString());
	        return null;
	      }
	   }

	   // retorna um Cursos com os lancamentos
	   public Cursor getCursorLancamento(String dataIni, String dataFim, long id_categoria) {
	      try {
	          // seta o where
	          String where  = "(vencimento BETWEEN '" + dataIni + "' AND '" + dataFim + "')";
	        	     where += " AND (id_categoria = " + id_categoria + ")";

	          // colunas
	          String[] colunas = new String[] { "sum(valor) AS total" };

	          // executa o SELECT
	          return dba.query(NOME_TABELA1, colunas, where, null, null, null, null);
	      
	      } catch (SQLException e) {
	          Log.e(CATEGORIA,"Erro ao buscar os lancamentos: " + e.toString());
	          return null;
	      }
	   }

	   // retorna um Cursos com os lancamentos
	   public Cursor getCursorReceita(String mes_ano, long id_categoria) {
	      try {
	          // seta o where
	          String where  = "(mes_ano = " + mes_ano + ")";
	        	     where += " AND (id_categoria = " + id_categoria + ")";

	          // colunas
	          String[] colunas = new String[] { "sum(valor) AS total" };

	          // executa o SELECT
	          return dba.query(NOME_TABELA2, colunas, where, null, null, null, null);
	      
	      } catch (SQLException e) {
	          Log.e(CATEGORIA,"Erro ao buscar as receitas: " + e.toString());
	          return null;
	      }
	   }

	   // retorna o lancamento por periodo
	   public List<TabelaCategoria> listarCategorias() {
	      // pega os dados
	      Cursor dados = getCursor();

	      // monta a lista
	      List<TabelaCategoria> lista = new ArrayList<TabelaCategoria>();

	      // verifica o total delinhas
	      if (dados.getCount() > 0) {
	         // move para o primeiro registro
	         dados.moveToFirst();
	         
	         // Loop
	         do {
	             // instancia
	             TabelaCategoria tabela = new TabelaCategoria();
	              
	             // adiciona na lista
	             lista.add(tabela);
	             
	             // recupera os atributos das colunas
	             tabela.id        = dados.getLong(0);
	             tabela.descricao = dados.getString(1);
	             tabela.st_tipo   = dados.getInt(2);
	             tabela.total     = 0;
	             tabela.mostra    = 'N';
	         } while (dados.moveToNext());
	      }
	      
	      // retorna a lista
	      return lista;
	   }

	   /**
	    * Combo de Categorias para o campo Spinner
	    * @param onde (0 - Listagem / 1 - Cadastro)
	    * @return
	    */
	   public String[] listarCategoriaCombo(int onde) {
	      // pega os dados
	      Cursor dados = getCursorCombo(onde);

	      // seta o total de itens
	      int totItem = dados.getCount() + 1;
	      
	      // inicia o Array de retorno
	      String[] retorno = new String[totItem];
	      
	      // inicia o cont
	      int cont = 1;
	      
	      // se for da Listagem, colocar o Texto (todos)
	      retorno[0] = (onde == 0 || onde == 2) ?  contexto.getText(R.string.txtTitulo7).toString() : contexto.getText(R.string.txtTitulo8).toString();
	      
	      // verifica o total delinhas
	      if (dados.getCount() > 0) {
	         // move para o primeiro registro
	         dados.moveToFirst();
	         
	         // Loop
	         do {
	             // pega a descricao
	        	 retorno[cont] = dados.getString(1);
	         
	             // incrementa o contador
	             cont++;
	         } while (dados.moveToNext());
	      }
	      
	      // retorna a lista
	      return retorno;
	   }

	   public long[] listarCategoriaComboID(int onde) {
		      // pega os dados
		      Cursor dados = getCursorCombo(onde);
		      
		      // seta o total de itens
		      int totItem = dados.getCount() + 1;

		      // inicia o Array de retorno
		      long[] retorno = new long[totItem];
		      
		      // seta o primeiro item
		      retorno[0] = 0;
		      
		      // inicia o cont
		      int cont = 1;
		      
		      // verifica o total delinhas
		      if (dados.getCount() > 0) {
		         // move para o primeiro registro
		         dados.moveToFirst();
		         
		         // Loop
		         do {
		             // pega o ID
		             retorno[cont] = dados.getLong(0);
		         
		             // incrementa o contador
		             cont++;
		         } while (dados.moveToNext());
		      }
		      
		      // retorna a lista
		      return retorno;
	   }

	   // retorna o lancamento de um ID
	   public TabelaCategoria editarCategoria(long id) {
	      // pega os dados
	      Cursor dados = getCursor(id);
	      
	      // instancia
	      TabelaCategoria tabela = new TabelaCategoria();

	      // verifica o total delinhas
	      if (dados.getCount() > 0) {
	         // move para o primeiro registro
	         dados.moveToFirst();

	         // recupera os atributos das colunas
	         tabela.id        = dados.getLong(0);
	         tabela.descricao = dados.getString(1);
	         tabela.st_tipo   = dados.getInt(2);
	      }
	      
	      // retorna a lista
	      return tabela;
	   }

	   public float getTotalLancamento(String dataIni, String dataFim, long id_categoria) {
		   // seta o retorno
		   float total = 0;

	       // pega os dados
		   Cursor dados = getCursorLancamento(dataIni, dataFim, id_categoria);

		   // verifica o total delinhas
		   if (dados.getCount() > 0) {
		       // move para o primeiro registro
		       dados.moveToFirst();

		       // recupera os atributos das colunas
		       total = dados.getFloat(0);
		   }

		   // retornas
		   return total;
	   }

	   public float getTotalReceita(String mes_ano, long id_categoria) {
		   // seta o retorno
		   float total = 0;

    	   // acerta o mes e ano
		   String[] ttData = mes_ano.split("-");
		   String cdata    = ttData[0] + ttData[1];

	       // pega os dados
		   Cursor dados = getCursorReceita(cdata, id_categoria);

		   // verifica o total delinhas
		   if (dados.getCount() > 0) {
		       // move para o primeiro registro
		       dados.moveToFirst();

		       // recupera os atributos das colunas
		       total = dados.getFloat(0);
		   }

		   // retornas
		   return total;
	   }

	   // retorna o lancamento por periodo
	   public List<TabelaCategoria> listarCategoriasTotal(String dataIni, String dataFim) {
	      // pega os dados
	      Cursor dados = getCursorTotal();

	      // monta a lista
	      List<TabelaCategoria> lista = new ArrayList<TabelaCategoria>();

	      // verifica o total delinhas
	      if (dados.getCount() > 0) {
	         // move para o primeiro registro
	         dados.moveToFirst();
	         
	         // Loop
	         do {
	             // instancia
	             TabelaCategoria tabela = new TabelaCategoria();
	              
	             // adiciona na lista
	             lista.add(tabela);
	             
	             // recupera os atributos das colunas
	             tabela.id        = dados.getLong(0);
	             tabela.descricao = dados.getString(1);
	             tabela.st_tipo   = dados.getInt(2);
	             tabela.mostra    = 'S';
	         
	             // pega o total
	             if (tabela.st_tipo == 0) {
	            	 tabela.total = this.getTotalLancamento(dataIni, dataFim, tabela.id);
	             }
	             else {
	            	 tabela.total = this.getTotalReceita(dataIni, tabela.id);
	             }
	         
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