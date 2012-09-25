package br.com.monteoliva.adesp;

// imports da API do JAVA
import java.util.Date;

// imports da API do ANDROID
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class aDespIncluirCategoria extends Activity {
	// pega os parametros
	public int mesAtual;
	public int anoAtual;

    // RadioButton
	RadioButton TipoDespesa;
	RadioButton TipoReceita;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// seta orientation default
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // seta a Activity principal
		setContentView(R.layout.cadastro_categoria);

		// pega a data atual para default
		Date defData = new Date();

		//--------------------------------------
		// recebe os parametros passados
		//--------------------------------------
		  Intent it = this.getIntent();
		
		  // verifica o retorno e pega os parametros
		  if (it != null) {
			  mesAtual = it.getIntExtra("paramMes",defData.getMonth());
			  anoAtual = it.getIntExtra("paramAno",defData.getYear() );
		  }
		//--------------------------------------
	    
		// barra de titulo
		final TextView titBarra = (TextView) findViewById(R.id.titBarra);

		// seta o titulo
        titBarra.setText(getString(R.string.txtTitulo1));

		// botao gravar e cancelat
		ImageButton btGravar = (ImageButton) findViewById(R.id.btnGravar  );  
		ImageButton btCancel = (ImageButton) findViewById(R.id.btnCancelar);  

        // pega o RadioButton
		TipoDespesa = (RadioButton) findViewById(R.id.TipoDespesa);
		TipoReceita = (RadioButton) findViewById(R.id.TipoReceita);
		
		// verifica o Click do botao gravar
		btGravar.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					// grava os dados no banco
					gravarDados();

					// retorna ao menu
					startMenuVoltar();
				}
		});
			
		// verifica o Click do botao cancelar
		btCancel.setOnClickListener(new OnClickListener() {
				public void onClick(View view) { startMenuVoltar(); }
		});
	}

	/**
	 * Metodo que abre a Lista de Hoje
	 */
	public void startMenuVoltar() {
		// Abre Activity da aplicacao
		Intent i = new Intent(this,aDespInicio.class);
               i.putExtra("paramMes",mesAtual);
               i.putExtra("paramAno",anoAtual);
               i.putExtra("paramTab",2);
        startActivity(i);
        finish();
	}

	/**
	 * Metodo para gravar os dados
	 */
    public void gravarDados() {
		// instancia o repositorio
    	RepositorioCategoria repositorio = new RepositorioCategoria(aDespIncluirCategoria.this);

    	// pega os campos
		EditText dDescr = (EditText) findViewById(R.id.descricao);

		// pega os campos
		int st_tipo      = 0;
		String descricao = dDescr.getText().toString();

		// verifica o checked
		if (TipoDespesa.isChecked()) { st_tipo = 0; }
		if (TipoReceita.isChecked()) { st_tipo = 1; }

		// instancia a tabela de lancamento
		TabelaCategoria lancamento = new TabelaCategoria();

		// seta os campos
		lancamento.descricao = descricao;
		lancamento.st_tipo   = st_tipo;

		// grava os dados
        long count = repositorio.salvar(lancamento);
    
		// mostra mensagem de gravação
        if (count > -1) {
            Toast.makeText(aDespIncluirCategoria.this, "Dados incluídos com sucesso!!!", Toast.LENGTH_LONG).show();
        }
        else {
        	Log.e("aDesp - Gravar Nova Categoria","Erro ao gravar um novo regitsro!!!");
        }
    }

    /**
     * Evento KeyDown
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { return true; }
        else { 
            return super.onKeyDown(keyCode, event);
        }
    }
}