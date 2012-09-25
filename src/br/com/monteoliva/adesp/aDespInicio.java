package br.com.monteoliva.adesp;

// imports da API do JAVA
import java.util.Calendar;
import java.util.Date;

import br.com.monteoliva.biblioteca.iLib;

// imports da API do ANDROID
import android.app.TabActivity;
import android.app.NotificationManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class aDespInicio extends TabActivity {
    // pega os parametros
	public int mesAtual;
	public int anoAtual;
	public int tabDefault;
	public int ondeVeio = 0;

	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override protected void onCreate(Bundle savedInstanceState) {
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
			  ondeVeio   = it.getIntExtra("paramOpc",0);
		  }
		//--------------------------------------

        // se vier do Notification cancela a notificacao
        if (ondeVeio == 1) {
            NotificationManager mn = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mn.cancel(R.string.app_name);
        }

        // incia
        getInicio();

		// cria as imagens
		Drawable img1 = this.getResources().getDrawable(R.drawable.despesas  );
		Drawable img2 = this.getResources().getDrawable(R.drawable.receitas  );
		Drawable img3 = this.getResources().getDrawable(R.drawable.categorias);
		Drawable img4 = this.getResources().getDrawable(R.drawable.resumo    );
		
		// seta a Itent de Lista de Despesas
		Intent iTab1 = new Intent(this, aDespLista.class);
		iTab1.putExtra("paramMes",mesAtual);
		iTab1.putExtra("paramAno",anoAtual);

		// seta a Itent de Lista de Despesas
		Intent iTab2 = new Intent(this, aDespListaReceita.class);
		iTab2.putExtra("paramMes",mesAtual);
		iTab2.putExtra("paramAno",anoAtual);

		// seta a Itent de Cadastro de Categorias
		Intent iTab3 = new Intent(this, aDespListaCategoria.class);
		iTab3.putExtra("paramMes",mesAtual);
		iTab3.putExtra("paramAno",anoAtual);

		// seta a Itent de Resumo
		Intent iTab4 = new Intent(this, aDespResumo.class);
		iTab4.putExtra("paramMes",mesAtual);
		iTab4.putExtra("paramAno",anoAtual);

		// pega o TabHost da TabActivity
		TabHost tabHost = getTabHost();
	
		// Lista de Despesas
		TabSpec tab1 = tabHost.newTabSpec("tab1");
		tab1.setIndicator(getString(R.string.ToolBar1), img1);
		tab1.setContent(iTab1);
		tabHost.addTab(tab1);

	    // Lista de Receitas
		TabSpec tab2 = tabHost.newTabSpec("tab2");
		tab2.setIndicator(getString(R.string.ToolBar3), img2);
		tab2.setContent(iTab2);
		tabHost.addTab(tab2);

	    // Cadastro de Categorias
		TabSpec tab3 = tabHost.newTabSpec("tab3");
		tab3.setIndicator(getString(R.string.ToolBar4), img3);
		tab3.setContent(iTab3);
		tabHost.addTab(tab3);

	    // Resumo
		TabSpec tab4 = tabHost.newTabSpec("tab4");
		tab4.setIndicator(getString(R.string.ToolBar5), img4);
		tab4.setContent(iTab4);
		tabHost.addTab(tab4);

		// seta a tab default
		tabHost.setCurrentTab(tabDefault);
	}

    /**
     * Metodo para verificar o inicio
     */
    private void getInicio() {
        // verifica se eh a primeira vez
        if (iLib.getConfig(this, "primeiravez") == "S") {
            // grava a primeira vez
            iLib.gravaConfig(this, "primeiravez", "N");

            // executa o agendamento
            agendar(10);
        }
    }

    /**
     * Metodo para gerar o agendamernto
     * 
     * @param segundos
     */
    private void agendar(int segundos) {
        // Repetir a cada 5 minutos
        final int tempoRepetir = 300 * 1000;
    
        // seta o ponto de partida
        Intent it = new Intent("ADESP_EXECUTAR_ALARME");

        // PendingIntent para executar
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, it, 0);
        
        // Para executar o alarme depois de 5 segundos a partir de agora
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, segundos);

        // Agenda o alarme
        AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
        long tempo = c.getTimeInMillis();

        // Repetir a cada 15 minutos
        alarme.setRepeating(AlarmManager.RTC_WAKEUP, tempo, tempoRepetir, pIntent);
    }
}