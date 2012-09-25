package br.com.monteoliva.adesp;

// imports da API do JAVA
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// imports da API do ANDROID
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

// imports Local
import br.com.monteoliva.backup.Backup;
import br.com.monteoliva.backup.ListaBackup;
import br.com.monteoliva.biblioteca.iLib;

public class aDespConfig extends Activity {
	private List<Configuracao> lista;
	private ListView listaView;

	// pega os parametros
	public int mesAtual;
	public int anoAtual;
	public int tabDefault;

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
        setContentView(R.layout.config);
        
		// seta o ListView
		listaView = (ListView) findViewById(R.id.ListConfig);

		// botoes
		ImageButton btnVoltar = (ImageButton) findViewById(R.id.btnVoltar);
		ImageButton btnSair   = (ImageButton) findViewById(R.id.btnSair  );  

		// carrega os dados
		preencheLista();
		
        // pega o iten clicado (selecionado)
        listaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> a, View v, int posicao, long id) {
        		// verifica a posicao
        		switch(posicao) {
        		   case 0:
        			   iLib.getSobre(aDespConfig.this);
        			   break;
        		   case 1:
        			   gravarBackup();
        			   break;
        		   case 2:
        			   getRestore();
        			   break;
        		   case 3:
        			   // abre a janela para definicao da senha
        			   getDefineSenha();

        			   break;
        		}
        	}
        });

        // verifica o Click do botao voltar
        btnVoltar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) { getVoltar(); }
		});

        // verifica o Click do botao sair
        btnSair.setOnClickListener(new OnClickListener() {
			public void onClick(View view) { iLib.getSair(aDespConfig.this); }
		});
	}

	/**
	 * Metodo que cria a lista do SimpleAdapter
	 */
    private void preencheLista() {
		// inicia o dialogo
    	final ProgressDialog dialogo = ProgressDialog.show(this, "", getString(R.string.txtDialogo), false, true);
    	
    	// carrega
    	new Handler().postDelayed(new Runnable() {
    		    @Override
				public void run() {
    		    	int[] items = new int[] { R.string.txtMenuPri05,
    		    			                  R.string.txtMenuPri07,
    		    			                  R.string.txtMenuPri09,
    		    			                  R.string.txtMenuPri08};

    		    	// seta a descricao de senha
    		    	String txtSenha = "";
    		    	
    		    	// inicia a Lista
    		    	lista = new ArrayList<Configuracao>();
    		    	
    		    	// verifica se gravou a senha
    		        if (iLib.getConfig(aDespConfig.this, "flag_senha") == "sim") {
    		            // seta a descricao da senha
    		            txtSenha = getString(R.string.txtSenhaSim);
    		        } else {
    		            // altera a chave
    		            iLib.gravaConfig(aDespConfig.this, "flag_senha" , "nao");
    		            iLib.gravaConfig(aDespConfig.this, "logado"     , "nao");
    		            iLib.gravaConfig(aDespConfig.this, "grava_senha", "nao");
    		            iLib.gravaConfig(aDespConfig.this, "senha"      , "");
    		            
    		            // seta a descricao da senha
    		            txtSenha = getString(R.string.txtSenhaNao);
    		        }

    		    	// percorre os itens
    		        for(int ii=0; ii < items.length; ii++) {
    		        	// seta a descricao
    		        	String descricao = (ii == 3) ? txtSenha : "";

    		        	// seta os itens
        		    	Configuracao item = new Configuracao();
    		    	    item.configuracao = getString(items[ii]);
    		    		item.descricao    = descricao;
    		    		
    		    		// pega os itens
    		            lista.add(item);
    		    	}

    			    // seta o Adaptador com a lista
    		        listaView.setAdapter(new ConfiguracaoAdapter(aDespConfig.this, lista));

    		        // finaliza o dialogo
			    	dialogo.dismiss();
				}
		},1000);
    }

    /**
	 * Metodo volta
	 */
    private void getVoltar() {
		// Abre Activity da aplicacao
		Intent i = new Intent(this,aDespInicio.class);
               i.putExtra("paramMes",mesAtual);
               i.putExtra("paramAno",anoAtual);
               i.putExtra("paramTab",tabDefault);
        startActivity(i);
        finish();
    }

    /**
	 * Metodo volta
	 */
    private void getDefineSenha() {
		// Abre Activity da aplicacao
    	startActivityForResult(new Intent(this,ConfiguracaoSenha.class), 1);
    }

    /**
     * Evento KeyDown
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // executa o Sair
        	getVoltar();
        	
        	// retorna OK
        	return true;
        } else { 
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // verifica se o resutado foi OK
        if (resultCode == RESULT_OK && requestCode == 1) {
        	// recarrega a Lista
        	preencheLista();
        }
    }

    /**
     * Metodo que realiza o backup
     */
    private void gravarBackup() {
		// inicia o dialogo
    	final ProgressDialog dialogo = ProgressDialog.show(this, "", getString(R.string.txtDialogBkp), false, true);
    	
    	// carrega
    	new Handler().postDelayed(new Runnable() {
    		    @Override
				public void run() {
    		    	// pega a classe de backup
    		    	Backup bkp = new Backup(aDespConfig.this);
    		    	       bkp.setBackup();
			    	
			        // finaliza o dialogo
			    	dialogo.dismiss();
			    	
			    	// mostra mensagem de finalizacao do backup
			    	iLib.msBox(aDespConfig.this, getString(R.string.txtMenuPri07), getString(R.string.txtBkpOK));
				}
		},1500);
    }

    /**
     * Metodo que restaura um Backup
     */
    private void getRestore() {
		// Abre Activity da listagem de backup
    	startActivity(new Intent(this,ListaBackup.class));
   }
}