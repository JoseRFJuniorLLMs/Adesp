package br.com.monteoliva.adesp;

import java.util.Date;

import br.com.monteoliva.biblioteca.iLib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class aDespIncluirReceita extends Activity {
	// seta a LIB
	public static iLib lib;
	RepositorioReceita repositorio;

	// pega os parametros
	public int mesAtual;
	public int anoAtual;
	public long mesAno;

	// seta o Spinner
	private Spinner comboCateg;
	
	// pega o array de ID´s das Categorias
	private long[] idCateg;

	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// seta orientation default
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // seta a Activity principal
        setContentView(R.layout.receita);

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
		
		// seta o mes atual e ano atual
		String nomeMesAno = lib.nomeMes[mesAtual] + "/" + String.valueOf(anoAtual);

		// pega o mes_ano atual
		String smesAno = String.valueOf(anoAtual)+iLib.stMes[mesAtual];
		mesAno         = Long.parseLong(smesAno);

		// pega os campos
		final EditText dValor   = (EditText) findViewById(R.id.valor   );
        final TextView titBarra = (TextView) findViewById(R.id.titBarra);

        // seta o titulo
        titBarra.setText(getString(R.string.txtMenuPri01) + "  -  " + nomeMesAno);

		// seta o valor inicial
		String valor = "0";

		// seta a data de hoje no campo data
		dValor.setText(valor);

		// carrega o Combo de Categoria
		carregaCombo();
		
		// botao gravar e cancelat
		ImageButton btGravar = (ImageButton) findViewById(R.id.btnGravar  );  
		ImageButton btCancel = (ImageButton) findViewById(R.id.btnCancelar);  
		
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
               i.putExtra("paramTab",1);
        startActivity(i);
        finish();
	}

    public void gravarDados() {
		// instancia o repositorio
    	repositorio = new RepositorioReceita(this);

    	// pega os campos
		EditText dDescr = (EditText) findViewById(R.id.descricao  );
		EditText dValor = (EditText) findViewById(R.id.valor      );

		// pega os campos
		String nvalor    = dValor.getText().toString();
		String descricao = dDescr.getText().toString();
	    float  valor     = lib.formataValorGravar(nvalor);
		int gravar       = 1;

		// valida o campo valor
		if (valor < 0) {
			messageBox("Erro ao Gravar", "O Valor deve ser informado!");
			gravar = 0;
		}

		// pega o ID da categoria
		int posicao = comboCateg.getSelectedItemPosition();

		// verifica se grava ou nao
		if (gravar == 1) {
		    // instancia a tabela de receita
		    TabelaReceita receita = new TabelaReceita();

		    // seta os campos
		    receita.id_categoria = idCateg[posicao];
	        receita.valor        = valor;
		    receita.mes_ano      = mesAno;
		    receita.descricao    = descricao;

		    // grava os dados (upload)
            long count = repositorio.salvar(receita);

		    // mostra mensagem de gravação
            if (count > -1) {
                Toast.makeText(aDespIncluirReceita.this, "Dados incluídos com sucesso!!!", Toast.LENGTH_LONG).show();
            }
            else {
	    	    Log.e("aDesp - Gravar Novo","Erro ao gravar um novo regitsro!!!");
            }
		}
    }

    private void messageBox(String titulo, String mensagem) {
    	AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
    	dialogo.setTitle(titulo);
    	dialogo.setMessage(mensagem);
    	dialogo.setNeutralButton("OK", null);
    	dialogo.setIcon(R.drawable.icon);
    	dialogo.show();
    }

	public void carregaCombo() {
		// instancia o repositorio
    	RepositorioCategoria repositorio = new RepositorioCategoria(this);
    	
		// pega a lista de lancamento de hoje
    	final String[] lCategorias = repositorio.listarCategoriaCombo(3);	

    	// pega os ID´s
    	idCateg = repositorio.listarCategoriaComboID(3);
    	
        // seta o Adaptador
    	ArrayAdapter<String> adaptador;
    	adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lCategorias);
    	adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	
    	// pega o Spinner
    	comboCateg = (Spinner) findViewById(R.id.comboCategoria);
    	comboCateg.setAdapter(adaptador);
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