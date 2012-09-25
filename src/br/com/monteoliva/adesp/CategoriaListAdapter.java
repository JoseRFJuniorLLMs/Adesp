package br.com.monteoliva.adesp;

// imports da API do JAVA
import java.util.List;

import br.com.monteoliva.biblioteca.iLib;

// imports da API do ANDROID
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoriaListAdapter extends BaseAdapter {
	   private Context context;
	   private List<TabelaCategoria> lista;

	   public CategoriaListAdapter(Context context, List<TabelaCategoria> lista) {
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
		      TabelaCategoria c = lista.get(position);

		      // seta a LIB
	          iLib lib = new iLib(context);

		      // seta detalhesa do Layout
		      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		      // seta o View
		      View view = inflater.inflate(R.layout.lista_categoria,null);

		      // pega o tipo
		      final String tipo = (c.st_tipo == 0) ? "Despesas" : "Receitas";
		      
		      // Atualiza os campos da lista
		      TextView descricao = (TextView) view.findViewById(R.id.descricao);
	          TextView total     = (TextView) view.findViewById(R.id.total    );
	          TextView categoria = (TextView) view.findViewById(R.id.categoria);
		      descricao.setText(c.descricao);
		      categoria.setText(tipo);

		      // pega a imagem definida pelo recurso @drawable
		      ImageView icone = (ImageView) view.findViewById(R.id.icone);

              // verifica o icone
		      if (c.st_tipo == 0) { icone.setImageResource(R.drawable.despesas); }
		      if (c.st_tipo == 1) { icone.setImageResource(R.drawable.receitas); }
		      
		      // verifica se tem total
		      if (c.mostra == 'S') {
		  		  // mostra e seta o Total
		    	  total.setVisibility(View.VISIBLE);
		          total.setText("R$ " + lib.formataValor(c.total));
		      }

		      // retorna a view
		      return view;
		}
}