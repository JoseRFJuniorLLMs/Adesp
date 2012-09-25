package br.com.monteoliva.adesp;

//imports da API do JAVA
import java.util.List;

import br.com.monteoliva.biblioteca.iLib;

// imports da API do ANDROID
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReceitaListAdapter extends BaseAdapter {
	   private Context context;
	   private List<TabelaReceita> lista;

	   public ReceitaListAdapter(Context context, List<TabelaReceita> lista) {
		      this.context = context;
		      this.lista   = lista;
	   }

		public int getCount() { 
		      return lista.size();
		}

		public Object getItem(int position) {
		      return lista.get(position);
		}

		public long getItemId(int position) {
		      return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			  // Recupera a posicao do lancamento
		      TabelaReceita c = lista.get(position);

		      // seta a LIB
	          iLib lib = new iLib(context);

		      // seta detalhesa do Layout
		      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		      // seta o View
		      View view = inflater.inflate(R.layout.lista_receita,null);

		      // Atualiza os campos da lista
		      TextView descricao = (TextView) view.findViewById(R.id.descricao);
		      TextView valor     = (TextView) view.findViewById(R.id.valor    );
		      TextView categoria = (TextView) view.findViewById(R.id.categoria);

		      // seta os valores
		      descricao.setText(c.descricao);
		      valor.setText("R$ " + lib.formataValor(c.valor));
		      categoria.setText(c.categ_descricao);

		      // retorna a view
		      return view;
		}
}