package br.com.monteoliva.biblioteca;

// imports da API do ANDROID
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

// imports Local
import br.com.monteoliva.adesp.R;

public class BarraOpcoes extends LinearLayout {
	public BarraOpcoes(final Context context) {
		super(context);
	}
	
	public BarraOpcoes(final Context con, AttributeSet attrs) {
		super(con,attrs);		
		setOrientation(HORIZONTAL);
		setBackgroundColor(getResources().getColor(android.R.color.transparent));

		LayoutInflater inflater = (LayoutInflater) 
		con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.barra, this);
	}
}