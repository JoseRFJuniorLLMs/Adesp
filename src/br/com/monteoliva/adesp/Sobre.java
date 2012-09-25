package br.com.monteoliva.adesp;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Sobre extends Dialog implements OnClickListener {
	Button okButton;
	
	// constructor
	public Sobre(Context context) {
		super(context);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.sobre);
		okButton = (Button) findViewById(R.id.OkButton);
		okButton.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		if (v == okButton) { dismiss(); }
	}
}
