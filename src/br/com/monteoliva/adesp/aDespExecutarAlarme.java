package br.com.monteoliva.adesp;

// imports da API do JAVA
import java.util.Date;

import br.com.monteoliva.biblioteca.iLib;

// imports da API do ANDROID
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class aDespExecutarAlarme extends BroadcastReceiver {
   // variaveis
   int mes;
   int ano;
   
   @Override
   public void onReceive(Context contexto, Intent intent) {
   	    // pega a data
        Date data       = new Date();
   	    iLib lib        = new iLib(contexto);
   	    String cdata    = lib.formataDataGravar(data);
    	String[] ttData = cdata.split("-");
		ano             = Integer.parseInt(ttData[0]);
		mes             = Integer.parseInt(ttData[1]);

		// monta o repositorio
	    RepositorioAlarme repositorio = new RepositorioAlarme(contexto);

        // verifica se nao foi notificado
	    if (!repositorio.getNotificacao(cdata)) {
	    	// mostra a notificacao
	    	criaNotificacao(contexto);
	    }
   }

   /**
    * Metodo para criar uma notificacao para o usuario
    */
   private void criaNotificacao(Context contexto) {
       // seta titulo, mensagem
       CharSequence titulo   = "aDesp v2.5";
       CharSequence mensagem = "Despesa(s) Vencendo Hoje";

       // Recupera o servico do NotificationManager
       NotificationManager MyNotificacao = (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);
       Notification notification = new Notification(R.drawable.despesas, "Despesa(s) Vencendo Hoje", System.currentTimeMillis());

       // seta o ponto de partida
       Intent it = new Intent("ADESP_INICIO");
       it.putExtra("paramMes",mes);
       it.putExtra("paramAno",ano);
       it.putExtra("paramTab",0);
       it.putExtra("paramOpc",1);

       // PendingIntent para executar a aActivity se o usuário selecionar a Notificação
       PendingIntent pIntent = PendingIntent.getActivity(contexto, 0, it, Intent.FLAG_ACTIVITY_NEW_TASK);

       // informações
       notification.setLatestEventInfo(contexto, titulo, mensagem, pIntent);

       // id (numero único) que identifica esta notificação
       MyNotificacao.notify(R.string.app_name, notification);
   }
}
