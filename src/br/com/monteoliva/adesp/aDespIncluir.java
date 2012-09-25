package br.com.monteoliva.adesp;

// imports da API do JAVA
import java.util.Date;

import br.com.monteoliva.biblioteca.iLib;

// imports da API do ANDROID
import android.app.Activity;
import android.app.AlertDialog;
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

public class aDespIncluir extends Activity {
	// seta a LIB
	public static iLib lib;
	RepositorioLancamento repositorio;
	RepositorioAlarme repositorioAlarme;

	// pega os parametros
	public int mesAtual;
	public int anoAtual;

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
        setContentView(R.layout.cadastro);
		
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

		// pega os campos
		final EditText dVenci       = (EditText) findViewById(R.id.vencimento );
		final EditText dValor       = (EditText) findViewById(R.id.valor      );
		final EditText dRecorrencia = (EditText) findViewById(R.id.recorrencia);
        final TextView titBarra     = (TextView) findViewById(R.id.titBarra   );

        // seta o titulo
        titBarra.setText(getString(R.string.txtMenuPri01) + "  -  " + nomeMesAno);

		// seta o valor inicial
		String valor = "0";

		// pega o dia atual
		String data = lib.formataData(new Date());
		String dia  = data.substring(0,2);
		
		// seta a data de hoje no campo data
		dVenci.setText(dia);
		dValor.setText(valor);
		dRecorrencia.setText(valor);

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
		Intent i = new Intent("ADESP_INICIO");
               i.putExtra("paramMes",mesAtual);
               i.putExtra("paramAno",anoAtual);
               i.putExtra("paramTab",0);
        startActivity(i);
        finish();
	}

    public void gravarDados() {
		// instancia o repositorio
    	repositorio       = new RepositorioLancamento(this);
    	repositorioAlarme = new RepositorioAlarme(this);

    	// pega os campos
		EditText dDescr       = (EditText) findViewById(R.id.descricao  );
		EditText dVenci       = (EditText) findViewById(R.id.vencimento );
		EditText dValor       = (EditText) findViewById(R.id.valor      );
		EditText dRecorrencia = (EditText) findViewById(R.id.recorrencia);

		// pega o RadioButton
		RadioButton pagoSim = (RadioButton) findViewById(R.id.pagoSim);
		RadioButton pagoNao = (RadioButton) findViewById(R.id.pagoNao);

		// seta a variavel
		int pago    = 0;
		int diasMes = Integer.parseInt(iLib.diasMes[mesAtual]);
		long count  = 0;
		
		// verifica o checked
		if (pagoSim.isChecked()) { pago = 1; }
		if (pagoNao.isChecked()) { pago = 0; }

		// pega os campos
		String nvalor     = dValor.getText().toString();
		float  valor      = Float.parseFloat(nvalor.replace(",","."));
		String cdata      = dVenci.getText().toString();
		String descricao  = dDescr.getText().toString();
		String data       = String.valueOf(anoAtual) + "-" + iLib.stMes[mesAtual] + "-" + cdata;
		int dia           = Integer.parseInt(cdata);
		int recorrencia   = Integer.parseInt(dRecorrencia.getText().toString());
		int gravar        = 1;
		
		// valida o campo valor
		if (valor < 0) {
			messageBox("Erro ao Gravar", "O Valor deve ser informado!");
			gravar = 0;
		}
		
		// valida o campo dia
		if (dia < 0) {
			messageBox("Erro ao Gravar", "O Dia de Vencimento deve ser maior que 0!");
			gravar = 0;
		}
		if (dia > diasMes) {
			messageBox("Erro ao Gravar", "O Dia de Vencimento deve ser menor que o Dia máximo do mês!");
			gravar = 0;
		}
		
		// pega o ID da categoria
		int posicao = comboCateg.getSelectedItemPosition();

		// seta array de datas
		String[] datas = null;

		// verifica se grava ou nao
		if (gravar == 1) {
			// inicia o dialogo
			ProgressDialog.show(this, "", getString(R.string.txtDialogo), false, true);
			
			// pega o ID da categoria
			long id_categoria = idCateg[posicao];
			
			// verifica a oroccerincia
			if (recorrencia > 0) {
				// pega as datas criadas
				datas = lib.recorrencia(dia, mesAtual, anoAtual, recorrencia);
			
			    // percorre o resultado
				for(int ii=0; ii < datas.length; ii++) {
					// realiza a gravacao
					count = gravacao(id_categoria, descricao, valor, datas[ii], pago);

					// verifica o retorno
					if (count <= 0) { break; }
				}
			}
			else {
				// realiza a gravacao
				count = gravacao(id_categoria, descricao, valor, data, pago);
			}

			// mostra mensagem de gravação
	        if (count > -1) {
	            Toast.makeText(aDespIncluir.this, "Dados incluídos com sucesso!!!", Toast.LENGTH_LONG).show();
	        }
	        else {
	    	    Log.e("aDesp - Gravar Novo","Erro ao gravar um novo regitsro!!!");
	        }
		}
    }

    /**
     * Metodo para realizar a gravacao
     * 
     * @param id_categoria
     * @param descricao
     * @param valor
     * @param data
     * @param pago
     */
    public long gravacao(long id_categoria, String descricao, float valor, String data, int pago) {
        // grava os dados do Alarme
    	repositorioAlarme.salvar(data);
    	
    	// instancia a tabela de lancamento
	    TabelaLancamento lancamento = new TabelaLancamento();

	    // seta os campos
	    lancamento.id_categoria = id_categoria;
	    lancamento.descricao    = descricao;
        lancamento.valor        = valor;
	    lancamento.vencimento   = data;
	    lancamento.pago         = pago;

        // grava os dados e retorna
        return repositorio.salvar(lancamento);
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