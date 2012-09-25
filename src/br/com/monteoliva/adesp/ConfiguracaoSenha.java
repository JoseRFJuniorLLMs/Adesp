package br.com.monteoliva.adesp;

// imports da API do ANDROID
import br.com.monteoliva.biblioteca.iLib;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ConfiguracaoSenha extends Activity implements OnClickListener {
	private Button btnGravar;
	private Button btnCancel;
	private Intent tela;
	private EditText dSenha;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// pega o intent
		tela = getIntent();
		
        // seta a Activity principal
        setContentView(R.layout.cadastro_senha);

        // pega o campo de senha
		dSenha = (EditText) findViewById(R.id.senha);

		// pega os dados da configuracao
        String senha  = iLib.getConfig(this, "senha");

		// pega a senha
		if (senha != null) { dSenha.setText(senha); }
		
		// botao gravar e cancelar
		btnGravar = (Button) findViewById(R.id.btnEntrar  );
		btnCancel = (Button) findViewById(R.id.btnCancelar);
		
		// implementa o Listener
        btnGravar.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
	}

	/**
	 * Metodo que grava as informacoes da senha digitada
	 */
    private void setSenha() {
		// pega a senha digitada
    	String digitada = dSenha.getText().toString();
    	
    	// verifica a senha digitada
    	if (digitada.length() > 0) {
        	// grava as informacoes da senha
    		iLib.gravaConfig(this, "flag_senha" , "sim");
    		iLib.gravaConfig(this, "grava_senha", "nao");
    		iLib.gravaConfig(this, "senha"      , digitada.toString());
    	}
    	else {
        	// grava as informacoes da senha
    		iLib.gravaConfig(this, "flag_senha" , "nao");
    		iLib.gravaConfig(this, "grava_senha", "nao");
    		iLib.gravaConfig(this, "senha"      , null);
    	}
		
        // finaliza
		setResult(RESULT_OK, tela);

		// fecha a tela atual
		finish();
		
    }

	@Override
	public void onClick(View v) {
		if (v == btnGravar) { setSenha(); }
		if (v == btnCancel) { finish();   }
	}
}