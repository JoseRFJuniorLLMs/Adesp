package br.com.monteoliva.adesp;

// imports da API do ANDROID
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RepositorioAlarme {
       private static final String CATEGORIA = "adesp";

       // nome do banco e da tabela
	   private static final String NOME_BANCO  = "adesp";
	   public  static final String NOME_TABELA = "alarme";

	   // pega o SQLite
	   protected SQLiteDatabase dba;

	   // constructor
	   public RepositorioAlarme(Context context) {
	      // abre o banco de dados
	      dba = context.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
	   }

	   public void salvar(String data) {
		   // pega o retorno
		   boolean gravar = this.getAlarmeGravado(data);
		   
		   // verifica
		   if (!gravar) { this.inserir(data); }
	   }
	   
	   // Insere novos dados para o agendamento
	   public long inserir(String data) {
		  // seta os valores
	      ContentValues valores = new ContentValues();
	      valores.put("data"       ,data);
	      valores.put("notificacao",0);
	      
	      // insere os dados na tabela
	      long id = dba.insert(NOME_TABELA, "", valores);
	   
	      // retorna o ID gerado
	      return id;
	   }

	   // Insere novos dados para o agendamento
	   public int atualizacao(String data) {
		  // seta os valores
	      ContentValues valores = new ContentValues();
	      valores.put("notificacao",1);

	      // seta o WHERE
	      String where = "(data = '" + data + "')";

	      // atualiza a tabela
	      int count = dba.update(NOME_TABELA, valores, where, null);
	   
	      // retorna o ID gerado
	      return count;
	   }
	   
	   // retorna um Cursor
	   public Cursor getCursor(String data) {
	      try {
	          // seta o where
	          String where = "(data = '" + data + "')";

	          // seta as colunas
	          String[] colunas = new String[]{ "_id", "data", "notificacao"};
	          
	          // executa o SELECT
	          return dba.query(NOME_TABELA, colunas, where, null, null, null, null);
	      
	      } catch (SQLException e) {
	          Log.e(CATEGORIA,"Erro ao pegar dados da Tabela Alarme: " + e.toString());
	        return null;
	      }
	   }

	   // retorna o Alarme
	   public boolean getAlarmeGravado(String data) {
	      // pega os dados
	      Cursor dados = getCursor(data);

	      // verifica o total delinhas
	      if (dados.getCount() > 0) { return true;  }
	      else                      { return false; }
	   }

	   // retorna o Alarme
	   public boolean getNotificacao(String data) {
	      // pega os dados
	      Cursor dados = getCursor(data);

	      // verifica o total delinhas
	      if (dados.getCount() > 0) {
		      // move para o primeiro registro
		      dados.moveToFirst();

		      // recupera os atributos das colunas
	          int notificacao = dados.getInt(2);

	    	  // verifica a notificacao
	          if (notificacao == 1) { return true; }
	          else {
	        	  // atualiza os dados
	        	  atualizacao(data);

	        	  // retorna
	        	  return false;
	          }
	      }
	      else { return true; }
	   }
}