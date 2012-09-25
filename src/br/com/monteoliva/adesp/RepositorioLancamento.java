/**
 * Classe para conter a pesquisa da Tabela de Lancamento
 *
 * @author Claudio Monteoliva
 * @since 1.0.0 - 25/07/2010
 *
 */
package br.com.monteoliva.adesp;

// imports da API do JAVA
import java.util.ArrayList;
import java.util.List;

// imports da API do ANDROID
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RepositorioLancamento {
   private static final String CATEGORIA = "adesp";

   // nome do banco e da tabela
   private static final String NOME_BANCO   = "adesp";
   public  static final String NOME_TABELA1 = "lancamento";
   public  static final String NOME_TABELA2 = "receita";
   public  static final String NOME_TABELA3 = "categoria";

   // pega o SQLite
   protected static SQLiteDatabase dba;

   // seta os totais
   public float totalPagar = 0;
   public float totalPago  = 0;
   public float totalGeral = 0;

   // constructor
   public RepositorioLancamento(Context context) {
      // abre o banco de dados
      dba = context.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
   }

   // salva os dados, insere novo ou atualiza
   public long salvar(TabelaLancamento lancamento) {
      long id = lancamento.id;
      
      // verifica o ID
      if (id != 0) {
         atualizar(lancamento);
      }
      else {
         id = inserir(lancamento);
      }
      
      // retorna o ID
      return id;
   }

   // Insere um novo lancamento
   public long inserir(TabelaLancamento lancamento) {
      // seta os valores
      ContentValues valores = new ContentValues();
      valores.put("id_categoria",lancamento.id_categoria);
      valores.put("descricao"   ,lancamento.descricao   );
      valores.put("valor"       ,lancamento.valor       );
      valores.put("vencimento"  ,lancamento.vencimento  );
      valores.put("pago"        ,lancamento.pago        );
      
      // insere os dados na tabela
      long id = dba.insert(NOME_TABELA1, "", valores);
   
      // retorna o ID gerado
      return id;
   }

   // Atualiza um lancamento no banco. O id eh utilizado.
   public int atualizar(TabelaLancamento lancamento) {
      // seta os valores
      ContentValues valores = new ContentValues();
      valores.put("id_categoria",lancamento.id_categoria);
      valores.put("descricao"   ,lancamento.descricao   );
      valores.put("valor"       ,lancamento.valor       );
      valores.put("vencimento"  ,lancamento.vencimento  );
      valores.put("pago"        ,lancamento.pago        );

      // seta o WHERE
      String where = "(_id=" + lancamento.id + ")";

      // atualiza a tabela
      int count = dba.update(NOME_TABELA1, valores, where, null);
      
      // retorna o numero de linhas atualizadas
      return count;
   }

   // Atualiza um lancamento no banco. O id eh utilizado.
   public int atualizarPagto(long id) {
	   // monta o lancamento
	   TabelaLancamento lancamento = new TabelaLancamento();
	   lancamento.pago = 1;
	   
	   // seta os valores
       ContentValues valores = new ContentValues();
       valores.put("pago",lancamento.pago);

       // seta o WHERE
       String where = "(_id=" + id + ")";
      
       // atualiza a tabela
       int count = dba.update(NOME_TABELA1, valores, where, null);
      
       // retorna o numero de linhas atualizadas
       return count;
   }

   // Apaga um lancamento com o id fornecido
   public int deletar(TabelaLancamento lancamento) {
      // seta o WHERE
      String where = "(_id=" + lancamento.id + ")";

      // atualiza a tabela
      int count = dba.delete(NOME_TABELA1, where, null);
      
      // retorna o numero de linhas atualizadas
      return count;
   }

   // retorna um Cursos com os lancamentos
   public Cursor getCursor(String dataIni, String dataFim, long id_categoria) {
      try {
          // seta o where
          String where = "(vencimento BETWEEN '" + dataIni + "' AND '" + dataFim + "')";

          // verifica a dataIni e dataFim
          if (dataIni == dataFim) {
        	  where += " AND (pago = 0)";
          }
          
          // verifica se foi passado o ID da Categoria
          if (id_categoria > 0) {
        	  where += " AND (id_categoria = " + id_categoria + ")";
          }

          // seta o Order By
          String orderBy = "vencimento ASC";
          
          // executa o SELECT
          return dba.query(NOME_TABELA1, TabelaLancamento.colunas, where, null, null, null, orderBy);
      
      } catch (SQLException e) {
          Log.e(CATEGORIA,"Erro ao buscar os lancamentos: " + e.toString());
        return null;
      }
   }

   // retorna um Cursos com os lancamentos
   public Cursor getCursor(long id) {
      try {
          // seta o where
          String where = "(_id = " + id + ")";
      
          // executa o SELECT
          return dba.query(NOME_TABELA1, TabelaLancamento.colunas, where, null, null, null, null);
      
      } catch (SQLException e) {
          Log.e(CATEGORIA,"Erro ao buscar o Lancamento selecionado: " + e.toString());
        return null;
      }
   }

   // retorna um Cursos com os lancamentos (tudo)
   public Cursor getCursor() {
      try {
          // executa o SELECT
          return dba.query(NOME_TABELA1, TabelaLancamento.colunas, null, null, null, null, null);
      
      } catch (SQLException e) {
          Log.e(CATEGORIA,"Erro ao buscar o Lancamento selecionado: " + e.toString());
        return null;
      }
   }

   // retorna um Cursos com a receita do mes
   public Cursor getCursorReceita(long mes_ano) {
      try {
          // seta o where
          String where = "(mes_ano = " + mes_ano + ")";
          
          // colunas
          String[] colunas = new String[] { "sum(valor) AS total" };

          // executa o SELECT
          return dba.query(NOME_TABELA2, colunas, where, null, null, null, null);
      
      } catch (SQLException e) {
          Log.e(CATEGORIA,"Erro ao buscar a Receita do mes: " + e.toString());
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
          return dba.query(NOME_TABELA3, colunas, where, null, null, null, null);
      
      } catch (SQLException e) {
          Log.e(CATEGORIA,"Erro ao buscar a Categoria selecionada: " + e.toString());
        return null;
      }
   }

   // retorna o lancamento por periodo
   public List<TabelaLancamento> listarLancamento(String dataIni, String dataFim, long id_categoria) {
      // pega os dados
      Cursor dados = getCursor(dataIni,dataFim, id_categoria);

      // monta a lista
      List<TabelaLancamento> lista = new ArrayList<TabelaLancamento>();

      // verifica o total delinhas
      if (dados.getCount() > 0) {
         // move para o primeiro registro
         dados.moveToFirst();
         
         // Loop
         do {
             // instancia
             TabelaLancamento tabela = new TabelaLancamento();
              
             // adiciona na lista
             lista.add(tabela);
             
             // recupera os atributos das colunas
             tabela.id              = dados.getLong(0);
             tabela.id_categoria    = dados.getLong(1);
             tabela.descricao       = dados.getString(2);
             tabela.valor           = dados.getFloat(3);
             tabela.vencimento      = dados.getString(4);
             tabela.pago            = dados.getInt(5);
             tabela.categ_descricao = this.getCategoriaDescricao(tabela.id_categoria);
         
             // seta os totais
             totalPagar += (tabela.pago == 0) ? tabela.valor : 0;
             totalPago  += (tabela.pago == 1) ? tabela.valor : 0;
         } while (dados.moveToNext());
      
         // seta o total geral
         totalGeral = totalPagar - totalPago;
      }
      
      // retorna a lista
      return lista;
   }

   // retorna o lancamento de um ID
   public TabelaLancamento editarLancamento(long id) {
      // pega os dados
      Cursor dados = getCursor(id);
      
      // instancia
      TabelaLancamento tabela = new TabelaLancamento();

      // verifica o total delinhas
      if (dados.getCount() > 0) {
         // move para o primeiro registro
         dados.moveToFirst();

         // recupera os atributos das colunas
         tabela.id           = dados.getLong(0);
         tabela.id_categoria = dados.getLong(1);
         tabela.descricao    = dados.getString(2);
         tabela.valor        = dados.getFloat(3);
         tabela.vencimento   = dados.getString(4);
         tabela.pago         = dados.getInt(5);
      }
      
      // retorna a lista
      return tabela;
   }


   // retorna o lancamento de um ID
   public float totalReceita(long mes_ano) {
      // pega os dados
      Cursor dados = getCursorReceita(mes_ano);
      
      // seta o retorno
      float retorno = 0;
      
      // verifica o total delinhas
      if (dados.getCount() > 0) {
         // move para o primeiro registro
         dados.moveToFirst();

         // recupera os atributos das colunas
         retorno  = dados.getFloat(0);
      }
      
      // retorna a lista
      return retorno;
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
   
   // retorna o lancamento por periodo
   public List<TabelaLancamento> listarTudo() {
      // pega os dados
      Cursor dados = getCursor();

      // monta a lista
      List<TabelaLancamento> lista = new ArrayList<TabelaLancamento>();

      // verifica o total delinhas
      if (dados.getCount() > 0) {
         // move para o primeiro registro
         dados.moveToFirst();
         
         // Loop
         do {
             // instancia
             TabelaLancamento tabela = new TabelaLancamento();
              
             // adiciona na lista
             lista.add(tabela);
             
             // recupera os atributos das colunas
             tabela.id           = dados.getLong(0);
             tabela.id_categoria = dados.getLong(1);
             tabela.descricao    = dados.getString(2);
             tabela.valor        = dados.getFloat(3);
             tabela.vencimento   = dados.getString(4);
             tabela.pago         = dados.getInt(5);
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