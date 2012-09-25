package br.com.monteoliva.adesp;

// imports da API do JAVA
import java.util.Date;

import br.com.monteoliva.biblioteca.iLib;

// imports da API do ANDROID
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class aDespEditar extends Activity {
	// inicia o repositorio
	RepositorioLancamento repositorio;
	RepositorioAlarme repositorioAlarme;
	TabelaLancamento tabela;

	// seta a LIB
	public static iLib lib;

	// pega os parametros
	public int mesAtual;
	public int anoAtual;
	public long id;

	// seta os campos
	EditText dDescr;
	EditText dVenci;
	EditText dValor;
	TextView titBarra;
	RadioButton pagoSim;
	RadioButton pagoNao;

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
		setContentView(R.layout.editar);
	
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
			  id       = it.getLongExtra("id"     ,0                 );
		  }
		//--------------------------------------

		// instancia a LIB
		lib = new iLib(this);
		
		// seta o mes atual e ano atual
		String nomeMesAno = lib.nomeMes[mesAtual] + "/" + String.valueOf(anoAtual);

		// instancia o repositorio
		repositorio       = new RepositorioLancamento(this);
    	repositorioAlarme = new RepositorioAlarme(this);

    	// pega os campos
		dDescr   = (EditText) findViewById(R.id.descricao );
		dVenci   = (EditText) findViewById(R.id.vencimento);
		dValor   = (EditText) findViewById(R.id.valor     );
        titBarra = (TextView) findViewById(R.id.titBarra  );

		// pega o RadioButton
		pagoSim = (RadioButton) findViewById(R.id.pagoSim);
		pagoNao = (RadioButton) findViewById(R.id.pagoNao);

        // seta o titulo
        titBarra.setText(getString(R.string.btnEditar) + "  -  " + nomeMesAno);

        // edita os dados e coloca nos campos
		editarDados();
		
		// carrega o Combo de Categoria
		carregaCombo();

		// botao gravar e cancelar
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
		Intent i = new Intent("ADESP_INICIO");
               i.putExtra("paramMes",mesAtual);
               i.putExtra("paramAno",anoAtual);
               i.putExtra("paramTab",0);
        startActivity(i);
        finish();
	}

	public void editarDados() {
		// pega os dados do ID
		tabela = repositorio.editarLancamento(id);
	
		// pega o dia atual
		String data  = lib.formataData(tabela.vencimento);
		String dia   = data.substring(0,2);
		String valor = lib.formataValor(tabela.valor); 

		// seta os dados nos campos
		dDescr.setText(tabela.descricao);
		dVenci.setText(dia);
		dValor.setText(valor);

		// verifica se foi pago ou nao
		if(tabela.pago == 0) { pagoNao.setChecked(true); }
		if(tabela.pago == 1) { pagoSim.setChecked(true); }
	}
	
    public void gravarDados() {
		// seta a variaveis
		int pago    = 0;
		int diasMes = Integer.parseInt(iLib.diasMes[mesAtual]);
		
		// verifica o checked
		if (pagoSim.isChecked()) { pago = 1; }
		if (pagoNao.isChecked()) { pago = 0; }

		// pega os campos
		String nvalor    = dValor.getText().toString();
		float  valor     = lib.formataValorGravar(nvalor);
		String cdata     = dVenci.getText().toString();
		String descricao = dDescr.getText().toString();
		String data      = String.valueOf(anoAtual) + "-" + iLib.stMes[mesAtual] + "-" + cdata;
		int dia          = Integer.parseInt(cdata);
		int gravar       = 1;

		// valida o campo valor
		if (valor < 0) {
			iLib.msBox(this,"Erro ao Gravar", "O Valor deve ser informado!");
			gravar = 0;
		}
		
		// valida o campo dia
		if (dia < 0) {
			iLib.msBox(this,"Erro ao Gravar", "O Dia de Vencimento deve ser maior que 0!");
			gravar = 0;
		}
		if (dia > diasMes) {
			iLib.msBox(this,"Erro ao Gravar", "O Dia de Vencimento deve ser menor que o Dia máximo do mês!");
			gravar = 0;
		}

		// pega o ID da categoria
		int posicao = comboCateg.getSelectedItemPosition();

		// verifica se grava ou nao
		if (gravar == 1) {
			// inicia o dialogo
			ProgressDialog.show(this, "", getString(R.string.txtDialogo), false, true);
			
		    // instancia a tabela de lancamento
		    TabelaLancamento lancamento = new TabelaLancamento();

		    // seta os campos
		    lancamento.id           = id;
		    lancamento.id_categoria = idCateg[posicao];
		    lancamento.descricao    = descricao;
	        lancamento.valor        = valor;
		    lancamento.vencimento   = data;
		    lancamento.pago         = pago;

		    // grava os dados (upload)
            long count = repositorio.salvar(lancamento);

            // grava os dados do Alarme
        	repositorioAlarme.salvar(data);

		    // mostra mensagem de gravação
            if (count > -1) {
                Toast.makeText(aDespEditar.this, "Dados atualizados com sucesso!!!", Toast.LENGTH_LONG).show();
            }
            else {
        	    Log.e("aDesp - Gravar Editar","Erro ao gravar o regitsro editado!!!");
            }
		}
    }

    public void carregaCombo() {
		// instancia o repositorio
    	RepositorioCategoria repositorio = new RepositorioCategoria(this);
    	
		// pega a lista de lancamento de hoje
    	final String[] lCategorias = repositorio.listarCategoriaCombo(1);	

    	// pega os ID´s
    	idCateg = repositorio.listarCategoriaComboID(1);
    	
        // seta o Adaptador
    	ArrayAdapter<String> adaptador;
    	adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lCategorias);
    	adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	
    	// pega o Spinner
    	comboCateg = (Spinner) findViewById(R.id.comboCategoria);
    	comboCateg.setAdapter(adaptador);
    
    	// cria a posicao
    	int posicao = 0;
    	
        // percorre os ID´s
    	for(int i=0; i < idCateg.length; i++) {
    		if (idCateg[i] == tabela.id_categoria) { posicao = i; }
    	}
    
    	// seta a posicao do Spinner
    	comboCateg.setSelection(posicao);
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