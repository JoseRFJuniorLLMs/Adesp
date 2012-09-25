package br.com.monteoliva.adesp;

// imports da API do JAVA
import java.util.Date;
import java.util.List;

import br.com.monteoliva.biblioteca.iLib;

// imports da API do ANDROID
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class aDespResumo extends Activity {
	public static RepositorioLancamento repositorio;
	public static RepositorioCategoria repositorioCategoria;
	private List<TabelaCategoria> categorias;
    private iLib lib;

	// pega os parametros
	public int mesAtual;
	public int anoAtual;

	// pega o mesano atual
	private long mesAno;

	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// seta orientation default
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // seta a Activity principal
		setContentView(R.layout.resumo);
		
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

		// instancia a LIB
		lib = new iLib(this);

		// pega o mes_ano atual
		String smesAno = String.valueOf(anoAtual)+iLib.stMes[mesAtual];
		mesAno         = Long.parseLong(smesAno);

		// instancia o repositorio
        repositorio          = new RepositorioLancamento(this);
        repositorioCategoria = new RepositorioCategoria(this);

        // pega o total da receita
        float gTotalReceita = repositorio.totalReceita(mesAno);
	
        // pega a data de inicio e fim
        String dtIni = String.valueOf(anoAtual) + "-" + iLib.stMes[mesAtual] + "-01";  
        String dtFim = String.valueOf(anoAtual) + "-" + iLib.stMes[mesAtual] + "-" + iLib.diasMes[mesAtual];  

        // pega a lista de lancamento de hoje
        repositorio.listarLancamento(dtIni,dtFim, 0);	

        // pega a lista de categporias de hoje
        categorias = repositorioCategoria.listarCategoriasTotal(dtIni,dtFim);	
        
		// seta o ListView
		ListView listaView = (ListView) findViewById(R.id.ListView01);

	    // seta o Adaptador com a lista
        listaView.setAdapter(new CategoriaListAdapter(this, categorias));

        // seta o mes atual e ano atual
		String nomeMesAno = lib.nomeMes[mesAtual] + "/" + String.valueOf(anoAtual);
        
        // pega os campos
        TextView titBarra       = (TextView) findViewById(R.id.titBarra    );
        TextView totalReceita   = (TextView) findViewById(R.id.totalReceita);
        TextView totalPagar     = (TextView) findViewById(R.id.totalPagar  );
        TextView totalPago      = (TextView) findViewById(R.id.totalPago   );
        TextView totalGeral     = (TextView) findViewById(R.id.totalBalanco);
        TextView totalGeralReal = (TextView) findViewById(R.id.totalBalancoReal);

        // pega o balanco
        float totalBalancoProjecao = gTotalReceita - repositorio.totalPagar - repositorio.totalPago;
        float totalBalancoReal     = gTotalReceita - repositorio.totalPago;
        
        // seta os dados nos campos
        titBarra.setText(getText(R.string.app_nome) + "  -  " + nomeMesAno);
        totalReceita.setText("  " + lib.formataValor(gTotalReceita));
        totalPagar.setText("  " + lib.formataValor(repositorio.totalPagar));
        totalPago.setText("  " + lib.formataValor(repositorio.totalPago));
        totalGeral.setText("  " + lib.formataValor(totalBalancoProjecao));
        totalGeralReal.setText("  " + lib.formataValor(totalBalancoReal));
	
		// botao pesquisar
        ImageButton btnSobre    = (ImageButton) findViewById(R.id.btnSobre   );
        ImageButton btnSair     = (ImageButton) findViewById(R.id.btnSair    );
		ImageButton btnAnterior = (ImageButton) findViewById(R.id.btnAnterior);
		ImageButton btnProximo  = (ImageButton) findViewById(R.id.btnProximo );
		
        // verifica o Click do botao Anterior
        btnAnterior.setOnClickListener(new OnClickListener() {
			public void onClick(View view) { anteriorMes(); }
		});

        // verifica o Click do botao Proximo
        btnProximo.setOnClickListener(new OnClickListener() {
			public void onClick(View view) { proximoMes(); }
		});

        // verifica o Click do botao sobre
        btnSobre.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				iLib.getConfiguracao(aDespResumo.this, mesAtual, anoAtual, 3);
				finish();
			}
		});
    
		// verifica o Click do botao sair
		btnSair.setOnClickListener(new OnClickListener() {
			public void onClick(View view) { getSair(); }
		});
	}
	
	private void proximoMes() {
    	// seta o proximo mes
    	int mesNovo = mesAtual + 1;
    	int anoNovo = anoAtual;
    	
    	// verifica
    	if (mesNovo > 12) { mesNovo = 1; anoNovo = anoAtual + 1; }
    
		// Abre Activity da aplicacao
		Intent i = new Intent(this,aDespInicio.class);
               i.putExtra("paramMes",mesNovo);
               i.putExtra("paramAno",anoNovo);
               i.putExtra("paramTab",1);
        startActivity(i);
        finish();
   }

    private void anteriorMes() {
      	// seta o proximo mes
    	int mesNovo = mesAtual - 1;
    	int anoNovo = anoAtual;
    	
    	// verifica
    	if (mesNovo < 1) { mesNovo = 12; anoNovo = anoAtual - 1; }
    
		// Abre Activity da aplicacao
		Intent i = new Intent(this,aDespInicio.class);
               i.putExtra("paramMes",mesNovo);
               i.putExtra("paramAno",anoNovo);
               i.putExtra("paramTab",3);
        startActivity(i);
        finish();
    }

    /**
     * Metodo que pede confirmacao
     */
    private void getSair() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(R.string.txtSair);
		dialog.setPositiveButton(R.string.txtSim, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface di, int arg) { finish(); }
		});
		dialog.setNegativeButton(R.string.txtNao, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface di, int arg) { }
		});
		dialog.show();
    }

    /**
     * Evento KeyDown
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // executa o Sair
        	getSair();
        	
        	// retorna OK
        	return true;
        } else { 
            return super.onKeyDown(keyCode, event);
        }
    }
}