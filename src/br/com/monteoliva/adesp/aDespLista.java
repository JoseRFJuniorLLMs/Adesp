package br.com.monteoliva.adesp;

// imports da API do JAVA
import java.util.Date;
import java.util.List;

import br.com.monteoliva.biblioteca.iLib;

// imports da API do ANDROID
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class aDespLista<Item> extends Activity {
	public static RepositorioLancamento repositorio;
	private List<TabelaLancamento> lancamento;
	private ListView listaView;
    private iLib lib;
    private int posicaoLista;

	// pega os parametros
	public int mesAtual;
	public int anoAtual;

	// seta variaveis desta classe
    private String dtIni;  
    private String dtFim; 

    // seta o Spinner
	private Spinner comboCateg;
	
	// pega o array de ID´s das Categorias
	private long[] idCateg;

	// dialog
	ProgressDialog dialog;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.padrao_lista);

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

		// seta o ListView
		listaView = (ListView) findViewById(R.id.ListView01);

		// instancia a LIB
		lib = new iLib(this);

		// instancia o repositorio
        repositorio = new RepositorioLancamento(this);
	
        //atualiza a lista
        atualizarLista();
        
        // pega o iten clicado (selecionado)
        listaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> a, View v, int posicao, long id) { editar(posicao); }
        });
	
        // Click longo
        listaView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> a, View v, int posicao, long id) {
            	// pega a posicao da Lista
            	posicaoLista = posicao;

            	// monta o ContextMenu
            	montaContextMenu();

            	// retorna
            	return false;
            }
        });

		// botao pesquisar
		ImageButton btnNovo     = (ImageButton) findViewById(R.id.btnNovo    );
		ImageButton btPesq      = (ImageButton) findViewById(R.id.btnPesq    );  
        ImageButton btnSobre    = (ImageButton) findViewById(R.id.btnSobre   );
        ImageButton btnSair     = (ImageButton) findViewById(R.id.btnSair    );
		ImageButton btnAnterior = (ImageButton) findViewById(R.id.btnAnterior);
		ImageButton btnProximo  = (ImageButton) findViewById(R.id.btnProximo );
    
		// mostra o botao Novo, Anterior e Proximo
		btnNovo.setVisibility(View.VISIBLE);
		btnAnterior.setVisibility(View.VISIBLE);
		btnProximo.setVisibility(View.VISIBLE);

        // verifica o Click do botao novo
        btnNovo.setOnClickListener(new OnClickListener() {
			public void onClick(View view) { startMenuNovo(); }
		});

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
				iLib.getConfiguracao(aDespLista.this, mesAtual, anoAtual, 0);
				finish();
			}
		});
    
		// verifica o Click do botao sair
		btnSair.setOnClickListener(new OnClickListener() {
			public void onClick(View view) { getSair(); }
		});

		// verifica o Click do botao gravar
		btPesq.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// pega o ID da categoria
				int posicao = comboCateg.getSelectedItemPosition();
				atualizarListagem(idCateg[posicao]);
			}
		});
	}

	public void atualizarLista() {
        // seta o mes atual e ano atual
		String nomeMesAno = lib.nomeMes[mesAtual] + "/" + String.valueOf(anoAtual);
        
        // seta o titulo
        TextView titBarra = (TextView) findViewById(R.id.titBarra);
        titBarra.setText(getText(R.string.app_nome) + "  -  " + nomeMesAno);
    
        // carrega o combo de categorias
        carregaCombo();
        
	    // carrega a Listagem
        atualizarListagem(0);
	}
	
	/**
	 * Metodo para carregar a Listagem conforme o item selecionado no Combo
	 * 
	 * @param id_categoria
	 */
	public void atualizarListagem(final long id_categoria) {
		// inicia o dialogo
    	final ProgressDialog dialogo = ProgressDialog.show(this, "", getString(R.string.txtDialogo), false, true);
    	
    	// carrega
    	new Handler().postDelayed(new Runnable() {
    		    @Override
				public void run() {
    				// pega a data de inicio e fim
    		        dtIni = String.valueOf(anoAtual) + "-" + iLib.stMes[mesAtual] + "-01";  
    		        dtFim = String.valueOf(anoAtual) + "-" + iLib.stMes[mesAtual] + "-" + iLib.diasMes[mesAtual];  

    		        // pega a lista de lancamento de hoje
    		        lancamento = repositorio.listarLancamento(dtIni,dtFim, id_categoria);	
    			
    			    // seta o Adaptador com a lista
    		        listaView.setAdapter(new LancamentoListAdapter(aDespLista.this, lancamento));
			    	
			        // finaliza o dialogo
			    	dialogo.dismiss();
				}
		},1000);
	}

	/**
	 * Metodo que edita
	 */
	public void editar(final int posicao) {
		// inicia o dialogo
    	ProgressDialog.show(this, "", getString(R.string.txtDialogo), false, true);
    	
    	// carrega
    	new Handler().postDelayed(new Runnable() {
    		    @Override
				public void run() {
    				// pega os dados
    				TabelaLancamento tabela = lancamento.get(posicao);

    				// Abre Activity da aplicacao
    				Intent i = new Intent(aDespLista.this,aDespEditar.class);
    		               i.putExtra("paramMes",mesAtual );
    		               i.putExtra("paramAno",anoAtual );
    		               i.putExtra("id"      ,tabela.id);
    		        startActivity(i);
    		        finish();
				}
		},1000);
	}

	/**
	 * Metodo que abre a Lista de Novo
	 */
	public void startMenuNovo() {
		// inicia o dialogo
    	ProgressDialog.show(this, "", getString(R.string.txtDialogo), false, true);
    	
    	// carrega
    	new Handler().postDelayed(new Runnable() {
    		    @Override
				public void run() {
    				// Abre Activity da aplicacao
    				Intent i = new Intent(aDespLista.this,aDespIncluir.class);
    		               i.putExtra("paramMes",mesAtual);
    		               i.putExtra("paramAno",anoAtual);
    		        startActivity(i);
    		        finish();
				}
		},1000);
	}

	/**
	 * Metodo que vai para o proximo mes
	 */
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
               i.putExtra("paramTab",0);
        startActivity(i);
        finish();
    }

	/**
	 * Metodo que vai para o mes anterior
	 */
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
               i.putExtra("paramTab",0);
        startActivity(i);
        finish();
    }

    /**
     * Metodo que carrega o combo de categorias
     */
    public void carregaCombo() {
		// instancia o repositorio
    	RepositorioCategoria repositorio = new RepositorioCategoria(this);
    	
		// pega a lista de lancamento de hoje
    	final String[] lCategorias = repositorio.listarCategoriaCombo(0);	

    	// pega os ID´s
    	idCateg = repositorio.listarCategoriaComboID(0);
    	
        // seta o Adaptador
    	ArrayAdapter<String> adaptador;
    	adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lCategorias);
    	adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	
    	// pega o Spinner
    	comboCateg = (Spinner) findViewById(R.id.comboCategoria);
    	comboCateg.setAdapter(adaptador);
    	comboCateg.setSelection(0);
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
     * Pede confirmacao para Excluir Item (ContextMenu)
     * 
     * @param <int> posicao
     */
	private void confirmaExclusao(final int posicao) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(R.string.txtExcluir);
		dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface di, int arg) {
		    	// eclui os dados
		    	excluirDados(posicao);
		    	
		    	// recarrega a listagem
		    	atualizarListagem(0);
		    }
		});
		dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface di, int arg) { }
		});
		
		dialog.setTitle(R.string.btnExcluir);
		dialog.show();
	}

	/**
	 * Metodo que exclui um lancamento
	 * 
	 * @param posicao
	 */
    private void excluirDados(final int posicao) {
		// pega os dados
		TabelaLancamento tabela = lancamento.get(posicao);

		// exclui os dados no banco
		int count = repositorio.deletar(tabela);

		// mostra mensagem de gravação
        if (count > -1) {
            Toast.makeText(this, "Dados excluídos com sucesso!!!", Toast.LENGTH_LONG).show();
        }
        else {
        	Log.e("aDesp - Excluir","Erro ao excluir o regitsro!!!");
        }
    }

	/**
	 * Metodo que exclui um lancamento
	 * 
	 * @param posicao
	 */
    private void setPagamento(final int posicao) {
		// pega os dados
		TabelaLancamento tabela = lancamento.get(posicao);

		// exclui os dados no banco
		int count = repositorio.atualizarPagto(tabela.id);

		// mostra mensagem de gravação
        if (count > -1) {
            Toast.makeText(this, "Despesa paga com sucesso!!!", Toast.LENGTH_LONG).show();
        }
        else {
        	Log.e("aDesp - Update","Erro ao atualizar o regitsro!!!");
        }
    
        // atualiza a listagem
    	atualizarListagem(0);
    }
    
    /**
     * Evento KeyDown
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // executa o Sair
        	getSair();
        	
        	// retorna OK
        	return true;
        } else { 
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * Metodo que mostras as opcoes
     */
    private void montaContextMenu() {
		listaView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu conMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
				conMenu.setHeaderTitle("Opções");
				conMenu.add(0, 0, 0, getString(R.string.txtTitulo3));
				conMenu.add(0, 1, 0, getString(R.string.btnExcluir));
				
				// pega os dados
				final TabelaLancamento tabela = lancamento.get(posicaoLista);
				
				// verifica se ainda nao esta pago
				if (tabela.pago == 0) {
					// monta o menu de pagamento
					conMenu.add(0, 2, 0, getString(R.string.txtTitulo6));
				}
			}
		});
    }

	public boolean onContextItemSelected(MenuItem item) {
		// verifica opcap selecionada
		switch (item.getItemId()) {
			case 0:
				editar(posicaoLista); break;
			case 1:
				confirmaExclusao(posicaoLista); break;
			case 2:
				setPagamento(posicaoLista); break;
		}
		return false;
	}
}