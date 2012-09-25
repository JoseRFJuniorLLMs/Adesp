package br.com.monteoliva.adesp;

// inports da API do JAVA
import java.util.Date;

import br.com.monteoliva.biblioteca.iLib;

// imports da API do ANDROID
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class aDespSenha extends Activity  implements OnClickListener {
	// pega os parametros
	public int mesAtual;
	public int anoAtual;
	public int tabDefault;

	// botao entrar
	private Button btnEntrar;

	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        // seta orientation default
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // pega a data atual para default
		Date defData = new Date();

		//--------------------------------------
		// recebe os parametros passados
		//--------------------------------------
		  Intent it = this.getIntent();
		
		  // verifica o retorno e pega os parametros
		  if (it != null) {
			  mesAtual   = it.getIntExtra("paramMes",defData.getMonth());
			  anoAtual   = it.getIntExtra("paramAno",defData.getYear() );
			  tabDefault = it.getIntExtra("paramTab",0);
		  }
		//--------------------------------------

        // seta a Activity principal
        setContentView(R.layout.senha);
        
		// botao entrar
		btnEntrar = (Button) findViewById(R.id.btnEntrar);

		// implementa o Listener
        btnEntrar.setOnClickListener(this);
	}

    /**
     * Evento KeyDown
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // executa o Sair
        	iLib.getSair(this);
        	
        	// retorna OK
        	return true;
        } else { 
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * Metodo para autenticar a senha digitada
     */
    private void autentica() {
    	// pega os campos
		EditText dSenha = (EditText) findViewById(R.id.senha);
		
		// pega a senha gravada
		String gSenha = iLib.getConfig(this, "senha");
		
		// verifica a senha digitada com a senha gravada
        if (gSenha.equals(dSenha.getText().toString())) {
        	// pega a data
            Date data       = new Date();
        	iLib lib        = new iLib(this);
        	String cdata    = lib.formataDataGravar(data);
        	String[] ttData = cdata.split("-");
    		int ano         = Integer.parseInt(ttData[0]);
    		int mes         = Integer.parseInt(ttData[1]);

    		// Abre Activity da aplicacao
    		Intent it = new Intent("ADESP_INICIO");
    		       it.putExtra("paramMes",mes);
    		       it.putExtra("paramAno",ano);
    		       it.putExtra("paramTab",0);
            startActivity(it);

            // Finaliza esta Activity
            finish();
        }
        else {
        	// mostra mensagem de erro
        	iLib.msBox(this, "", getString(R.string.txtSenhaIncorreta));
        }
    }

	@Override
	public void onClick(View v) {
		if (v == btnEntrar) { autentica(); }
	}
}