package br.com.monteoliva.adesp;

// imports da API do JAVA
import java.util.Date;

// imports da API do ANDROID
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

// imports Local
import br.com.monteoliva.biblioteca.iLib;
import br.com.monteoliva.dao.criaBanco;

public class aDesp extends Activity implements Runnable {
	// seta paranmetros
	String nomeItnet;

	// seta o banco
	criaBanco banco;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // seta orientation default
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        // seta a Activity principal
        setContentView(R.layout.main);

        // coloca a mensagem na tela
        ProgressDialog.show(this, "", getString(R.string.txtInicio), false, true);
        
        // cria o banco de dados
        if (iLib.getConfig(this, "novo_banco") == "S") {
        	// cria o banco
            banco = new criaBanco(this);
            
            // altera a chave
            iLib.gravaConfig(this, "novo_banco", "N");
        }

        // pega os dados da configuracao
        String flag_senha  = iLib.getConfig(this, "flag_senha" );
        String grava_senha = iLib.getConfig(this, "grava_senha");
        
        // verifica se gravou a senha
    	nomeItnet = (flag_senha == "sim" && grava_senha == "nao") ? "ADESP_SENHA" : "ADESP_INICIO";

        // seta em alguns segundos a abertura da proxima Activity
        Handler tempo = new Handler();

        // executa o tempo (cinco segundos de delay)
        tempo.postDelayed(this,1000);
    }

    public void run() {
    	// verifica se existe a pasta, senao cria
    	iLib.setPastaBackup();

    	// pega a data
        Date data       = new Date();
    	iLib lib        = new iLib(this);
    	String cdata    = lib.formataDataGravar(data);
    	String[] ttData = cdata.split("-");
		int ano         = Integer.parseInt(ttData[0]);
		int mes         = Integer.parseInt(ttData[1]);

		// Abre Activity da aplicacao
		Intent it = new Intent(nomeItnet);
		       it.putExtra("paramMes",mes);
		       it.putExtra("paramAno",ano);
		       it.putExtra("paramTab",0);
        startActivity(it);

        // Finaliza esta Activity
        finish();
    }

    /**
     * Evento KeyDown
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) || (keyCode == KeyEvent.KEYCODE_HOME)) { return true; }
        else { 
            return super.onKeyDown(keyCode, event);
        }
    }
}