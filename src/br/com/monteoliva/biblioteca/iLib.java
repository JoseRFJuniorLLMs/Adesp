package br.com.monteoliva.biblioteca;

// imports da API do JAVA
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//imports Local
import br.com.monteoliva.adesp.R;
import br.com.monteoliva.adesp.Sobre;
import br.com.monteoliva.adesp.aDespConfig;

// imports da API do ANDROID
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

public class iLib {
	// seta o nome mês
	public String[] nomeMes;

	// seta os dias de cada mês
	public static final String[] diasMes = new String[] {"","31","28","31","30","31","30","31","31","30","31","30","31"};

	// seta os dias de cada mês
	public static final String[] stMes = new String[] {"","01","02","03","04","05","06","07","08","09","10","11","12"};

	// seta os formatos de datas
	private static SimpleDateFormat dmyFormat;
	private static SimpleDateFormat ymdFormat;
	
	// seta o formato de valor
	private static DecimalFormat fDecimal;

	// pasta de Backup
	public static final String RECORD_PATH = "/sdcard/aDesp";
	
    // constructor
	public iLib(Context contexto) {
		// seta formato de datas
		dmyFormat = new SimpleDateFormat("dd/MM/yyyy");
		ymdFormat = new SimpleDateFormat("yyyy-MM-dd");

		// seta formato de valor
		fDecimal = new DecimalFormat("###,##0.00");
    
		// pega os meses
		this.nomeMes = contexto.getResources().getStringArray(R.array.meses);
	}

    // formata a data
    public String formataData(Date data) { return dmyFormat.format(data); }

    // formata a data
    public String formataData(String data) {
		// formata a data
    	String[] ttData = data.split("-");
		String retorno  = ttData[2] + "/" + ttData[1] + "/" + ttData[0];

	    // retorno
	    return retorno;
    }

    // formata a data para gravacao
    public String formataDataGravar(Date data) { return ymdFormat.format(data); }

    // formata a data
    public String formataDataGravar(String data) {
		// formata a data
    	String[] ttData = data.split("/");
		String retorno  = ttData[2] + "-" + ttData[1] + "-" + ttData[0];

	    // retorno
	    return retorno;
	}

    // formata o valor
    public String formataValor(float valor) {
    	// seta o valor
        String cvalor = fDecimal.format(valor);
             //cvalor = cvalor.replace(",","-");
             //cvalor = cvalor.replace(".",",");
             //cvalor = cvalor.replace("-",".");

	    // retorna
	    return cvalor;
    }

    // formata o valor
    public String formataValor(String valor) {
    	// casting
    	float fvalor = Float.parseFloat(valor);

        // seta o valor
        String cvalor = fDecimal.format(fvalor);
             //cvalor = cvalor.replace(",","-");
             //cvalor = cvalor.replace(".",",");
             //cvalor = cvalor.replace("-",".");

    	// retorna
    	return cvalor;
    }

    // formata o valor
    public float formataValorGravar(String valor) {
        // transforma
    	int decimal = valor.length() - 3;

    	// verifica o sinal decimal
    	if (valor.charAt(decimal) == ',') {
        	// limpa os dados do campo
        	valor = valor.replace(".","" );
        	valor = valor.replace(",",".");
    	}
    	
    	// verifica o sinal decimal
    	if (valor.charAt(decimal) == '.') {
        	// limpa os dados do campo
        	valor = valor.replace(",","");
    	}

    	// casting
    	float fvalor = Float.parseFloat(valor);

    	// retorna
    	return fvalor;
    }

    /**
     * Metodo que calcula uma data inicial e final e grava os valores no banco de dados
     * para recorrencia de despesa
     * 
     * @author Luis Claudio Monteoliva
     * @since 1.0 - 21/09/2010
     * @since 2.0 - 17/10/2010
     * 
     * @param dia
     * @param mesIni
     * @param anoIni
     * @param qtde
     */
	public String[] recorrencia(int dia, int mesIni, int anoIni, int qtde) {
	    // seta os dados necessarios
		String sDia    = (dia    < 10) ? "0"+dia    : ""+dia;
	    String smesAtu = "";

	    // seta a quantidade
	    qtde += 1;

	    // inicia array de datas
	    String[] datas = new String[qtde];

	    // percorre a quantidade
	    for(int ii=0; ii < qtde; ii++) {
	    	// pega a data atual
	        smesAtu = (mesIni < 10) ? "0"+mesIni : ""+mesIni;

	    	// monta a data que sera gravada no banco de dados
	    	datas[ii] = anoIni + "-" + smesAtu + "-" + sDia; 

	        // incrementa o mes
	    	mesIni += 1;
	    	
	    	// verifica se o mes passou de 12
	    	if (mesIni > 12) {
	    		mesIni  = 1;
	    		anoIni += 1;
	    	}
	    }
    
	    // retorna
	    return datas;
	}

    /**
     * Metodo estatico que mostra o Sobre
     * 
     * @param contexto
     */
    static public void getSobre(Context contexto) {
   	    Sobre sobre = new Sobre(contexto);
	    sobre.show();
    }

    /**
     * Metodo que abre a configuracao
     */
	static public void getConfiguracao(Context contexto, int mesAtual, int anoAtual, int paramTab) {
		// Abre Activity da aplicacao
		Intent i = new Intent(contexto,aDespConfig.class);
               i.putExtra("paramMes",mesAtual);
               i.putExtra("paramAno",anoAtual);
               i.putExtra("paramTab",paramTab);
        contexto.startActivity(i);
    }

    /**
     * Metodo que grava na Preferences a chave
     */
    static public void gravaConfig(Context contexto, String chave, String valor) {
    	SharedPreferences settings = contexto.getSharedPreferences("config", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(chave, valor);
    	editor.commit();
    }

    /**
     * Metodo que pega a chave do Preferences
     * 
     * @return
     */
    static public String getConfig(Context contexto, String chave) {
    	// pega a configuracao
        SharedPreferences settings = contexto.getSharedPreferences("config", 0);

    	// retorna
        return  settings.getString(chave, "S");
    }

	/**
	 * Metodo static que verifica o SD Card
	 * 
	 * @return
	 */
	static public boolean getSDCard() {
		// pega o estado do SD Card
        String state = android.os.Environment.getExternalStorageState();

		// retorna
		return state.equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * Metodo que cria a pasta de backup se nao existir
	 */
    static public void setPastaBackup() {
        // verifica o SDCARD
    	if (iLib.getSDCard()) {
    		// pega o File
    		File pasta = new File(RECORD_PATH);

    		// se nao existe, cria a pasta
            if (!pasta.exists()) { pasta.mkdirs(); }
    	}
    }

    /**
     * Medoto de sair
     * 
     * @param tela
     */
    static public void getSair(final Activity tela) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(tela);
		dialog.setMessage(R.string.txtSair);
		dialog.setPositiveButton(R.string.txtSim, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface di, int arg) { tela.finish(); }
		});
		dialog.setNegativeButton(R.string.txtNao, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface di, int arg) { }
		});
		dialog.show();
    }

    /**
     * Metodo que mostra uma mensagem na tela da Activity
     * 
     * @param titulo
     * @param mensagem
     */
    static public void msBox(Context contexto, String titulo, String mensagem) {
    	AlertDialog.Builder dialog = new AlertDialog.Builder(contexto);
    	// verifica o titulo
    	if (titulo.trim() != "") {
    		dialog.setTitle(titulo);
        	dialog.setIcon(R.drawable.icon);
    	}
    	dialog.setMessage(mensagem);
    	dialog.setNeutralButton("OK", null);
    	dialog.show();
    }

    /**
     * Verifica se tem conexao com a Internet
     * 
     * @return boolean
     */
    static public boolean getConexaoInternet(Context contexto) {
        // seta variavel de retorno
    	boolean retorno = false;
    	
    	// pega a conexao
    	ConnectivityManager connec = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
    
    	// conexao WiFI
    	android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        
    	// conexao 3G
    	android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    	
    	// verifica a conexao
        if      (wifi.isConnected()  ) { retorno = true; }
        else if (mobile.isConnected()) { retorno = true; }

       // retortna
    	return retorno;
    }
}