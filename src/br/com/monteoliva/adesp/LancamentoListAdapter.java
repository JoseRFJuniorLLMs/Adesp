/**
 * Classe List Adapter
 *
 * @author Claudio Monteoliva
 * @since 1.0.0 - 25/07/2010
 *
 */
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

public class LancamentoListAdapter extends BaseAdapter {
   private Context context;
   private List<TabelaLancamento> lista;
   
   public LancamentoListAdapter(Context context, List<TabelaLancamento> lista) {
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
      TabelaLancamento c = lista.get(position);

      // seta detalhesa do Layout
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      // seta o View
      View view = inflater.inflate(R.layout.lista,null);

      // Atualiza os campos da lista
      TextView venci     = (TextView) view.findViewById(R.id.dia      );
      TextView descricao = (TextView) view.findViewById(R.id.descricao);
      TextView valor     = (TextView) view.findViewById(R.id.valor    );
      TextView categoria = (TextView) view.findViewById(R.id.categoria);

      // pega a imagem definida pelo recurso @drawable
      ImageView status = (ImageView) view.findViewById(R.id.status);

      // seta a LIB
      iLib lib = new iLib(context);

      // pega o valor e data
      String fData  = lib.formataData(c.vencimento);
      String fValor = lib.formataValor(c.valor);
      
      // seta os valores
      descricao.setText(c.descricao);
      venci.setText(fData.substring(0,2));
      valor.setText("R$ " + fValor);
      categoria.setText(c.categ_descricao);

      // seta a imagem
      switch (c.pago) {
         case 0:
        	  status.setImageResource(R.drawable.pagar);
        	  break;
         case 1:
    	      status.setImageResource(R.drawable.pago);
    	      break;
       	 default:
    	      status.setImageResource(R.drawable.pagar);
    	      break;
      }
      
      // retorna a view
      return view;
   }
}
