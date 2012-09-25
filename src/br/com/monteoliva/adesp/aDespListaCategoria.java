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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class aDespListaCategoria extends Activity {
	public static RepositorioCategoria repositorio;
	private List<TabelaCategoria> lancamento;
    private int posicaoLista;
    private ListView listaView;

	// pega os parametros
	public int mesAtual;
	public int anoAtual;

	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.padrao_lista_categoria);

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

		// instancia o repositorio
        repositorio = new RepositorioCategoria(this);

        // atualiza a listagem
        atualizarListagem();
        
        // pega o iten clicado (selecionado)
        listaView.setOnItemClickListener(new OnItemClickListener() {
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
		ImageButton btnNovo  = (ImageButton) findViewById(R.id.btnNovo );
        ImageButton btnSobre = (ImageButton) findViewById(R.id.btnSobre);
        ImageButton btnSair  = (ImageButton) findViewById(R.id.btnSair );
		
        // mostra o botao Novo
		btnNovo.setVisibility(View.VISIBLE);

        // verifica o Click do botao novo
        btnNovo.setOnClickListener(new OnClickListener() {
			public void onClick(View view) { startMenuNovo(); }
		});

        // verifica o Click do botao sobre
        btnSobre.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				iLib.getConfiguracao(aDespListaCategoria.this, mesAtual, anoAtual, 2);
				finish();
			}
		});
    
		// verifica o Click do botao sair
		btnSair.setOnClickListener(new OnClickListener() {
			public void onClick(View view) { getSair(); }
		});
    }

	private void atualizarListagem() {
		// pega a lista de lancamento de hoje
        lancamento = repositorio.listarCategorias();	

	    // seta o Adaptador com a lista
        listaView.setAdapter(new CategoriaListAdapter(this, lancamento));
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
    				TabelaCategoria tabela = lancamento.get(posicao);

    				// Abre Activity da aplicacao
    				Intent i = new Intent(aDespListaCategoria.this,aDespEditarCategoria.class);
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
    				Intent i = new Intent(aDespListaCategoria.this,aDespIncluirCategoria.class);
    		               i.putExtra("paramMes",mesAtual);
    		               i.putExtra("paramAno",anoAtual);
    		        startActivity(i);
    		        finish();
				}
		},1000);
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
        if ((keyCode == KeyEvent.KEYCODE_BACK) || (keyCode == KeyEvent.KEYCODE_HOME)) {
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
				conMenu.add(0, 0, 0, getString(R.string.txtTitulo2     ));
				conMenu.add(0, 1, 0, getString(R.string.btnExcluirCateg));
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
		}
		return false;
	}

    public void excluirDados(final int posicao) {
		// pega os dados
    	TabelaCategoria tabela = lancamento.get(posicao);

		// exclui os dados no banco
		int count = repositorio.deletar(tabela);

		// mostra mensagem de gravação
        if (count > -1) {
            Toast.makeText(this, "Dados excluídos com sucesso!!!", Toast.LENGTH_LONG).show();
        }
        else {
        	Log.e("aDesp - Excluir Categoria","Erro ao excluir o regitsro!!!");
        }
    }

	public void confirmaExclusao(final int posicao) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(R.string.txtExcluirCategoria);
		dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface di, int arg) {
		    	// eclui os dados
		    	excluirDados(posicao);
		    	
		    	// recarrega a listagem
		    	atualizarListagem();
		    }
		});
		dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface di, int arg) { }
		});
		
		dialog.setTitle(R.string.btnExcluirCateg);
		dialog.show();
	}
}