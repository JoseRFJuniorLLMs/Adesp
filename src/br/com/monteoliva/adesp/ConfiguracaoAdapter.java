package br.com.monteoliva.adesp;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConfiguracaoAdapter extends BaseAdapter {
	private Context context;
	private List<Configuracao> lista;

	public ConfiguracaoAdapter(Context context, List<Configuracao> lista) {
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
		// Recupera a posicao
		Configuracao c = lista.get(position);

		// seta detalhesa do Layout
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// seta o View
		View view = inflater.inflate(R.layout.lista_config,null);

		// Atualiza os campos da lista
		TextView configuracao = (TextView) view.findViewById(R.id.configuracao);
		TextView descricao    = (TextView) view.findViewById(R.id.descricao   );

		// seta os valores
		configuracao.setText(c.configuracao);
		descricao.setText(c.descricao);
		
		
		
		// retorna a view
		return view;
	}
}